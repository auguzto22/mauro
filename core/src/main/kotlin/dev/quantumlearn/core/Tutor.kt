package dev.quantumlearn.core

enum class TutorAction(val label: String) {
    Again("Explique novamente"),
    Simple("Explique de maneira mais simples"),
    Math("Explique matematicamente"),
    Example("Mostre outro exemplo"),
    Error("Por que minha resposta está errada?"),
    Application("Qual é a aplicação real?"),
    Practice("Crie um exercício semelhante"),
    Status("Qual é a evidência e quais são os limites?")
}

enum class Evidence(val label: String) {
    Mathematical("Resultado matemático"), Established("Modelo físico estabelecido"),
    Interpretation("Interpretação"), Hypothesis("Hipótese"), Speculation("Especulação"), Grammar("Regra gramatical")
}

data class TutorRequest(val lesson: Lesson, val action: TutorAction, val lastFeedback: Feedback? = null)
data class TutorReply(val text: String, val evidence: Evidence, val references: List<Reference>, val formula: Formula? = null)

fun interface TutorProvider {
    suspend fun explain(request: TutorRequest): TutorReply
}

object OfflineTutor : TutorProvider {
    override suspend fun explain(request: TutorRequest): TutorReply {
        val (lesson, action, feedback) = request
        val math = Curriculum.modules.take(3).any { lesson in it.lessons }
        val language = Curriculum.track(lesson.id) == Track.English
        var evidence = when {
            language -> Evidence.Grammar
            math -> Evidence.Mathematical
            else -> Evidence.Established
        }
        val text = when (action) {
            TutorAction.Again -> lesson.introduction
            TutorAction.Simple -> "${lesson.objective}\n\n${lesson.example}"
            TutorAction.Math -> lesson.detail
            TutorAction.Example -> lesson.questions.first().alternativeExample
            TutorAction.Error -> feedback?.let {
                "Sua resposta foi ${it.answer}. ${it.explanation}\n\nResultado esperado: ${it.correctAnswer}. " +
                    it.steps.joinToString(" ")
            } ?: "Conclua uma questão deste assunto para revisar o diagnóstico de uma resposta."
            TutorAction.Application -> lesson.application
            TutorAction.Practice -> lesson.objective
            TutorAction.Status -> when {
                language -> "Regra do inglês padrão, usada em textos, provas e conversas. " + lesson.detail + "\n\n" +
                    "Na fala informal surgem contrações e atalhos, mas a estrutura da lição é a forma que todo falante reconhece."
                math -> "O resultado segue das definições apresentadas. Confira as condições de validade. " + lesson.detail
                lesson.id in setOf("double-slit", "decoherence", "entanglement") -> {
                    evidence = Evidence.Interpretation
                    "Resultado observável. As probabilidades de medida seguem o modelo descrito na lição.\n\n" +
                        "Interpretação. Explicar o que a função de onda representa é uma questão diferente de calcular as probabilidades. " +
                        "Interpretações que reproduzem as mesmas previsões precisam de alguma diferença testável para ser distinguidas pelo experimento."
                }
                else -> "Modelo estabelecido sob condições específicas. " + lesson.detail + "\n\n" +
                    "As hipóteses de modelagem delimitam quando a previsão se aplica. Uma extensão além dessas condições precisaria de evidência própria."
            }
        }
        return TutorReply(text, evidence, lesson.references, lesson.equation.takeIf { action == TutorAction.Math })
    }
}
