package dev.quantumlearn.core

data class Reference(val name: String, val url: String)

data class Formula(val latex: String, val symbols: String)

enum class Difficulty(val label: String) {
    Introductory("Introdução"), Intermediate("Intermediária"), Advanced("Avançada")
}

enum class ErrorKind(val label: String) {
    Formula("Fórmula"), Calculation("Cálculo"), Unit("Unidade"), Interpretation("Interpretação")
}

enum class ChoiceType { Multiple, TrueFalse, Graph }
enum class NumberFormat { Value, ReducedFraction, Scientific }

data class Option(
    val label: String,
    val explanation: String,
    val errorKind: ErrorKind? = null,
    val confusion: String = ""
)

sealed interface AnswerSpec {
    data class Choice(
        val options: List<Option>,
        val correctIndex: Int,
        val type: ChoiceType = ChoiceType.Multiple
    ) : AnswerSpec

    data class Numeric(
        val expression: String,
        val unit: String? = null,
        val relativeTolerance: Double = 1e-7,
        val absoluteTolerance: Double = 0.0,
        val mistakes: Map<String, Option> = emptyMap(),
        val format: NumberFormat = NumberFormat.Value
    ) : AnswerSpec

    data class Sequence(val steps: List<String>, val order: List<Int>, val mismatch: Option) : AnswerSpec

    data class Matching(val pairs: List<Relationship>) : AnswerSpec

    data class Written(
        val accepted: Set<String>,
        val clarification: String,
        val model: String
    ) : AnswerSpec
}

data class Relationship(val term: String, val definition: String, val explanation: String)

sealed interface Answer {
    data class Selection(val index: Int) : Answer
    data class Text(val value: String) : Answer
}

data class Question(
    val id: String,
    val lessonId: String,
    val concept: String,
    val prerequisites: List<String>,
    val difficulty: Difficulty,
    val prompt: String,
    val expected: AnswerSpec,
    val steps: List<String>,
    val alternativeExample: String,
    val formula: Formula,
    val references: List<Reference>,
    val related: List<String>
)

enum class Diagram {
    Groups, Parts, Growth, Square, Balance, Scale, Line, Wave, Vector, Probability, Energy,
    Photon, Interference, Parabola, Area, Decay, Modes, Circuit, Levels, Threshold, Bell, Barrier, Matrix, Sentence
}

data class Lesson(
    val id: String,
    val title: String,
    val objective: String,
    val introduction: String,
    val example: String,
    val equation: Formula,
    val detail: String,
    val diagram: Diagram,
    val diagramCaption: String,
    val summary: String,
    val application: String,
    val references: List<Reference>,
    val prerequisites: List<String>,
    val questions: List<Question>
)

data class StudyUnit(val title: String, val lessons: List<Lesson>)
/** Trilha de estudo: ciências exatas ou inglês. */
enum class Track(val label: String) { Science("Física quântica"), English("Inglês") }

data class StudyModule(val title: String, val units: List<StudyUnit>, val track: Track = Track.Science) {
    val lessons get() = units.flatMap { it.lessons }
}

enum class Outcome { Correct, Incorrect, Clarify }

data class Feedback(
    val outcome: Outcome,
    val answer: String,
    val explanation: String,
    val correctAnswer: String,
    val errorKind: ErrorKind?,
    val confusion: String?,
    val steps: List<String>,
    val example: String
)

data class Attempt(
    val submissionId: String,
    val questionId: String,
    val lessonId: String,
    val correct: Boolean,
    val difficulty: Difficulty,
    val error: ErrorKind?,
    val confusion: String?,
    val atMillis: Long,
    val durationMillis: Long,
    val xp: Int,
    val answer: Answer? = null
)

data class Completion(val lessonId: String, val atMillis: Long)
