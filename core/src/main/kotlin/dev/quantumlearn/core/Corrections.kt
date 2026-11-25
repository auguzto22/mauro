package dev.quantumlearn.core

import java.text.Normalizer
import java.util.Locale

object Corrections {
    fun grade(question: Question, answer: Answer): Feedback {
        val shown = when (answer) {
            is Answer.Selection -> (question.expected as? AnswerSpec.Choice)
                ?.options?.getOrNull(answer.index)?.label.orEmpty()
            is Answer.Text -> answer.value.trim()
        }
        return when (val expected = question.expected) {
            is AnswerSpec.Choice -> {
                val option = (answer as? Answer.Selection)?.let { expected.options.getOrNull(it.index) }
                val correct = expected.options[expected.correctIndex].label
                when {
                    option == null -> clarify(shown, "Selecione uma das alternativas para conferir.", correct)
                    answer.index == expected.correctIndex -> feedback(question, shown, correct, option, true)
                    else -> feedback(question, shown, correct, option, false)
                }
            }
            is AnswerSpec.Numeric -> gradeNumber(question, expected, shown)
            is AnswerSpec.Sequence -> {
                val indices = indices(shown, expected.steps.size)
                val solution = expected.order.joinToString(" → ") { expected.steps[it] }
                if (indices == null || indices.distinct().size != indices.size) {
                    clarify(shown, "Escolha todas as etapas uma vez, na ordem em que acontecem.", solution)
                } else {
                    val supplied = indices.joinToString(" → ") { expected.steps[it] }
                    val correct = indices == expected.order
                    val note = if (correct) Option(supplied, "Cada operação cancela um termo por vez na ordem certa.") else expected.mismatch
                    feedback(question, supplied, solution, note, correct)
                }
            }
            is AnswerSpec.Matching -> {
                val indices = indices(shown, expected.pairs.size)
                val solution = expected.pairs.joinToString(" · ") { "${it.term} → ${it.definition}" }
                if (indices == null) {
                    clarify(shown, "Escolha uma definição para cada termo.", solution)
                } else {
                    val reply = expected.pairs.mapIndexed { index, pair ->
                        "${pair.term} → ${expected.pairs[indices[index]].definition}"
                    }.joinToString(" · ")
                    val mismatches = expected.pairs.filterIndexed { index, _ -> index != indices[index] }
                    val note = if (mismatches.isEmpty()) Option(reply, "Todas as associações estão corretas.")
                    else Option(reply, mismatches.joinToString(" ") { it.explanation },
                        ErrorKind.Interpretation, "Amplitude e probabilidade de medida")
                    feedback(question, reply, solution, note, mismatches.isEmpty())
                }
            }
            is AnswerSpec.Written -> {
                val normalized = normalize(shown)
                val supported = normalized == normalize(expected.model) ||
                    expected.accepted.any { normalized == normalize(it) }
                if (supported) {
                    feedback(question, shown, expected.model,
                        Option(shown, "Sua resposta inclui os conceitos pedidos. Compare com a formulação de referência."), true)
                } else {
                    clarify(shown, expected.clarification, expected.model)
                }
            }
        }
    }

    private fun indices(text: String, size: Int): List<Int>? {
        val indices = text.split(',').map { it.toIntOrNull() ?: return null }
        return indices.takeIf { it.size == size && it.all { index -> index in 0 until size } }
    }

    private fun gradeNumber(question: Question, expected: AnswerSpec.Numeric, shown: String): Feedback {
        val correct = listOfNotNull(expected.expression, expected.unit).joinToString(" ")
        return when (MathValidation.compare(shown, expected)) {
            MathValidation.Result.Equivalent ->
                feedback(question, shown, correct, Option(shown, "O valor é equivalente ao resultado esperado."), true)
            MathValidation.Result.Unreadable ->
                clarify(shown, "Escreva o valor como número ou expressão, por exemplo 3/4 ou 2,5 × 10^3.", correct)
            MathValidation.Result.UnitDiffers -> feedback(question, shown, correct, Option(
                shown,
                expected.unit?.let { "O resultado representa uma grandeza que pode ser expressa em $it. Separe valor e unidade por um espaço e confirme que a unidade mede a mesma grandeza." }
                    ?: "O resultado é adimensional. Informe somente o número ou a expressão.",
                ErrorKind.Unit, "Dimensão e unidade da grandeza"
            ), false)
            MathValidation.Result.FormatDiffers -> {
                val note = when (expected.format) {
                    NumberFormat.ReducedFraction -> "O valor está certo. Escreva uma fração irredutível, com numerador e denominador sem divisor comum maior que 1."
                    NumberFormat.Scientific -> "O valor está certo. Use um coeficiente de módulo de pelo menos 1 e menor que 10, seguido de e e do expoente, ou de × 10^expoente."
                    NumberFormat.Value -> "O valor numérico está correto."
                }
                clarify(shown, note, correct)
            }
            MathValidation.Result.ValueDiffers -> {
                val diagnosed = expected.mistakes.entries.firstOrNull { (value, _) ->
                    MathValidation.compare(shown, expected.copy(expression = value, format = NumberFormat.Value)) == MathValidation.Result.Equivalent
                }?.value
                feedback(question, shown, correct, diagnosed ?: Option(
                    shown, "Sua expressão resulta em um valor diferente de $correct. Confira a substituição e as operações na resolução. O resultado sozinho não identifica em qual etapa surgiu a diferença.",
                    null
                ), false)
            }
        }
    }

    private fun feedback(question: Question, shown: String, correct: String, option: Option, passed: Boolean) =
        Feedback(if (passed) Outcome.Correct else Outcome.Incorrect, shown, option.explanation, correct,
            option.errorKind, option.confusion.ifBlank { null }, question.steps, question.alternativeExample)

    private fun clarify(shown: String, explanation: String, correct: String) =
        Feedback(Outcome.Clarify, shown, explanation, correct, null, null, emptyList(), "")

    private fun normalize(text: String): String =
        Normalizer.normalize(text.lowercase(Locale.ROOT), Normalizer.Form.NFD)
            .replace(Regex("\\p{M}+"), "").replace(Regex("[^a-z0-9]+"), " ").trim()
}

object QuestionBank {
    fun validate(modules: List<StudyModule>) {
        val lessons = modules.flatMap { it.lessons }
        val questions = lessons.flatMap { it.questions }
        val lessonIds = lessons.map { it.id }.toSet()
        val byQuestion = questions.associateBy { it.id }
        require(lessonIds.size == lessons.size)
        require(byQuestion.size == questions.size)
        val seenLessons = mutableSetOf<String>()
        for (lesson in lessons) {
            require(lesson.prerequisites.all { it in seenLessons }) { "Pré-requisito de ${lesson.id}" }
            seenLessons += lesson.id
            require(lesson.questions.size >= 2)
            require(lesson.references.isNotEmpty() && lesson.references.all { it.url.startsWith("https://") })
            for (question in lesson.questions) {
                require(question.lessonId == lesson.id)
                require(question.prerequisites == lesson.prerequisites)
                require(question.steps.size >= 2 && question.steps.none(String::isBlank))
                require(question.prompt.isNotBlank() && question.alternativeExample.isNotBlank())
                require(question.formula.latex.isNotBlank() && question.formula.symbols.isNotBlank())
                require(question.references.isNotEmpty())
                require(question.related.isNotEmpty() && question.related.all {
                    it != question.id && byQuestion[it]?.lessonId == lesson.id
                })
                when (val expected = question.expected) {
                    is AnswerSpec.Choice -> {
                        require(expected.options.size >= 2 && expected.correctIndex in expected.options.indices)
                        require(expected.options.map { it.label }.distinct().size == expected.options.size)
                        require(expected.options.all { it.label.isNotBlank() && it.explanation.isNotBlank() })
                        require(expected.options.filterIndexed { index, _ -> index != expected.correctIndex }.all {
                            it.errorKind != null && it.confusion.isNotBlank()
                        })
                    }
                    is AnswerSpec.Numeric -> {
                        require(expected.unit == null || MathValidation.supportsUnit(expected.unit))
                        require(expected.relativeTolerance.isFinite() && expected.relativeTolerance >= 0)
                        require(expected.absoluteTolerance.isFinite() && expected.absoluteTolerance >= 0)
                        val solution = listOfNotNull(expected.expression, expected.unit).joinToString(" ")
                        require(MathValidation.compare(solution, expected) == MathValidation.Result.Equivalent)
                        require(expected.mistakes.all { (value, note) ->
                            MathValidation.evaluate(value) != null && note.errorKind != null && note.confusion.isNotBlank() &&
                                MathValidation.compare(listOfNotNull(value, expected.unit).joinToString(" "), expected) ==
                                MathValidation.Result.ValueDiffers
                        })
                    }
                    is AnswerSpec.Sequence -> {
                        require(expected.steps.size >= 2 && expected.steps.distinct().size == expected.steps.size)
                        require(expected.order.sorted() == expected.steps.indices.toList())
                        require(expected.mismatch.errorKind != null && expected.mismatch.explanation.isNotBlank())
                    }
                    is AnswerSpec.Matching -> {
                        require(expected.pairs.size >= 2)
                        require(expected.pairs.map { it.term }.distinct().size == expected.pairs.size)
                        require(expected.pairs.map { it.definition }.distinct().size == expected.pairs.size)
                        require(expected.pairs.all { it.term.isNotBlank() && it.definition.isNotBlank() && it.explanation.isNotBlank() })
                    }
                    is AnswerSpec.Written -> require(expected.model.isNotBlank() && expected.clarification.isNotBlank() &&
                        expected.accepted.isNotEmpty() && expected.accepted.none(String::isBlank))
                }
            }
        }
    }
}
