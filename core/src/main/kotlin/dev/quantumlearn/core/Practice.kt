package dev.quantumlearn.core

object Practice {
    private val format = Regex("""^(.+)-practice-(\d+)$""")

    fun restore(id: String): Question? {
        val match = format.matchEntire(id) ?: return null
        val seed = match.groupValues[2].toIntOrNull() ?: return null
        return generate(match.groupValues[1], seed)
    }

    fun generate(lessonId: String, seed: Int): Question? {
        if (seed < 0) return null
        val lesson = Curriculum.byId[lessonId] ?: return null
        val size = when (lessonId) {
            "operations", "fractions", "equations" -> 280
            "waves" -> 56
            "scientific" -> 40
            "spin", "qubit" -> 3
            else -> 8
        }
        val variant = seed % size
        val a = variant % 8 + 2
        val b = variant / 8 % 7 + 2
        val c = if (lessonId == "scientific") variant / 8 + 2 else variant / 56 + 2
        val draft = when (lessonId) {
            "operations" -> Draft("$a + $b × $c = ?", "${a + b * c}",
                "Multiplique $b × $c = ${b * c}.", "Some $a + ${b * c} = ${a + b * c}.",
                ErrorKind.Calculation, "Prioridade da multiplicação", "${(a + b) * c}",
                "Você somou $a e $b antes de multiplicar. A expressão não contém parênteses.")
            "fractions" -> {
                val numerator = a * c
                val denominator = (a + b) * c
                val divisor = numerator.toBigInteger().gcd(denominator.toBigInteger()).toInt()
                val simplified = "${numerator / divisor}/${denominator / divisor}"
                Draft("Escreva $numerator/$denominator como fração irredutível.", simplified,
                    "O maior divisor comum é $divisor.", "Divida os dois termos por $divisor e obtenha $simplified.",
                    ErrorKind.Formula, "Simplificação dos dois termos", "${numerator / divisor}/$denominator",
                    "Dividir apenas o numerador altera a fração. Aplique a mesma divisão nos dois termos.")
            }
            "powers" -> Draft("Que número completa $a² = □?", "${a * a}",
                "A base $a aparece duas vezes como fator.", "Multiplique $a × $a = ${a * a}.",
                ErrorKind.Formula, "Expoente e fator", "${a * 2}".takeUnless { a == 2 },
                "O expoente 2 pede duas cópias da base no produto.")
            "roots" -> Draft("Calcule √${a * a}.", "$a",
                "Reconheça que $a × $a = ${a * a}.", "Como a raiz principal é não negativa, escolha $a.",
                ErrorKind.Interpretation, "Raiz principal", "-$a", "O radical representa a raiz não negativa.")
            "equations" -> Draft("Resolva ${a}x + $b = ${a * c + b}.", "$c",
                "Subtraia $b de ambos os lados. Resta ${a}x = ${a * c}.", "Divida por $a para achar x = $c.",
                ErrorKind.Formula, "Operação inversa", "(${a * c + b}+$b)/$a", "Para cancelar +$b, subtraia $b dos dois lados.")
            "scientific" -> Draft("Escreva o valor $a × 10^−$c como número ou expressão.", "${a}e-$c",
                "O expoente −$c indica divisão por 10^$c.", "Aplique $a ÷ 10^$c, equivalente a ${a}e-$c.",
                ErrorKind.Formula, "Sinal do expoente", "${a}e$c", "O expoente negativo reduz o valor em $c ordens de grandeza.")
            "waves" -> Draft("Uma onda tem λ = $a m e f = $b Hz. Calcule v com unidade.", "${a * b}",
                "Use v = λf e substitua as duas grandezas.", "$a m × $b Hz = ${a * b} m/s.",
                ErrorKind.Formula, "Relação entre frequência e comprimento", "$a/$b", "A velocidade é λ multiplicado por f.", "m/s")
            "photons" -> Draft("Calcule a energia em joules para f = $a × 10¹⁴ Hz. Use h = 6,62607015 × 10⁻³⁴ J·s ou o símbolo h.",
                "h*${a}e14", "Substitua a frequência $a × 10¹⁴ Hz em E = hf.",
                "Multiplique os coeficientes e some os expoentes. O resultado é $a × 6,62607015 × 10⁻²⁰ J.",
                ErrorKind.Formula, "Energia por fóton", "h/${a}e14", "Para esta relação, multiplique a constante pela frequência.", "J")
            else -> reinforcement(lessonId, a) ?: return null
        }
        val expected = AnswerSpec.Numeric(draft.result, draft.unit, if (lessonId == "photons") 0.005 else 1e-7,
            mistakes = draft.wrongValue?.let { mapOf(it to Option(it, draft.explanation, draft.error, draft.confusion)) }.orEmpty(),
            format = if (lessonId == "fractions") NumberFormat.ReducedFraction else NumberFormat.Value)
        val solution = listOfNotNull(expected.expression, expected.unit).joinToString(" ")
        if (MathValidation.compare(solution, expected) != MathValidation.Result.Equivalent) return null
        val advanced = lessonId == "photons" || Curriculum.modules.last().lessons.any { it.id == lessonId }
        return Question("$lessonId-practice-$variant", lessonId, lesson.title, lesson.prerequisites,
            if (advanced) Difficulty.Advanced else Difficulty.Intermediate,
            draft.prompt, expected,
            listOf(draft.step1, draft.step2), lesson.example, lesson.equation, lesson.references, lesson.questions.map(Question::id))
    }

    internal data class Draft(
        val prompt: String, val result: String, val step1: String, val step2: String,
        val error: ErrorKind, val confusion: String, val wrongValue: String?, val explanation: String,
        val unit: String? = null
    )
}
