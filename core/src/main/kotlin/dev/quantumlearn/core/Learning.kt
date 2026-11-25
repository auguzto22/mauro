package dev.quantumlearn.core

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.roundToInt
import kotlin.random.Random

data class Mastery(
    val percent: Int,
    val demonstrated: Int,
    val correct: Int,
    val attempts: Int,
    val nextReviewAt: Long?,
    val retainedChecks: Int
)

data class StudyDay(val date: LocalDate, val answers: Int, val correct: Int)

data class Progress(
    val xp: Int,
    val xpToday: Int,
    val streak: Int,
    val minutes: Int,
    val accuracy: Int,
    val mastery: Map<String, Mastery>,
    val errors: List<Pair<String, Int>>,
    val recommendations: List<Lesson>,
    val completions: Set<String>,
    val recentDays: List<StudyDay>
) {
    val level get() = 1 + xp / 100
    val overall get() = if (mastery.isEmpty()) 0 else mastery.values.map { it.percent }.average().roundToInt()
}

object Learning {
    private const val day = 86_400_000L

    fun mastery(attempts: List<Attempt>): Mastery {
        val chronological = attempts.sortedBy(Attempt::atMillis)
        val latest = chronological.asReversed().distinctBy(Attempt::questionId).take(12)
        val demonstrated = latest.size
        val correct = latest.count(Attempt::correct)
        var due: Long? = null
        var successfulReviews = 0
        var retained = 0
        for (attempt in chronological) {
            val scheduled = due
            when {
                !attempt.correct -> {
                    successfulReviews = 0
                    due = attempt.atMillis + day
                }
                scheduled == null -> due = attempt.atMillis + day
                attempt.atMillis >= scheduled -> {
                    retained++
                    successfulReviews++
                    val interval = listOf(3, 7, 14, 30)[(successfulReviews - 1).coerceAtMost(3)]
                    due = attempt.atMillis + day * interval
                }
            }
        }
        val percent = if (demonstrated == 0) 0 else {
            val weights = latest.map { 1 + it.difficulty.ordinal }
            val score = latest.zip(weights).sumOf { (attempt, weight) -> if (attempt.correct) weight else 0 }
            (100.0 * score / weights.sum() * ((demonstrated + retained.coerceAtMost(2)) / 4.0).coerceAtMost(1.0)).roundToInt()
        }
        return Mastery(percent, demonstrated, chronological.count(Attempt::correct), chronological.size, due, retained)
    }

    fun difficulty(mastery: Mastery?) = when (mastery?.percent ?: 0) {
        in 0..39 -> Difficulty.Introductory
        in 40..74 -> Difficulty.Intermediate
        else -> Difficulty.Advanced
    }

    fun progress(
        lessons: List<Lesson>, attempts: List<Attempt>, completions: List<Completion>,
        now: Long, zone: ZoneId
    ): Progress {
        val byLesson = attempts.groupBy(Attempt::lessonId)
        val mastery = lessons.associate { it.id to mastery(byLesson[it.id].orEmpty()) }
        val today = Instant.ofEpochMilli(now).atZone(zone).toLocalDate()
        fun date(attempt: Attempt) = Instant.ofEpochMilli(attempt.atMillis).atZone(zone).toLocalDate()
        val byDate = attempts.groupBy(::date)
        val recentDays = (6 downTo 0).map { offset ->
            val date = today.minusDays(offset.toLong())
            val evidence = byDate[date].orEmpty()
            StudyDay(date, evidence.size, evidence.count(Attempt::correct))
        }
        val mistakes = attempts.asSequence().sortedByDescending(Attempt::atMillis).take(40)
            .filterNot(Attempt::correct).mapNotNull { it.confusion ?: it.error?.label }
            .groupingBy { it }.eachCount().toList().sortedByDescending { it.second }
        val targeted = byLesson.entries.filter { (_, evidence) ->
            evidence.sortedByDescending(Attempt::atMillis).take(4).count { !it.correct } >= 2
        }.flatMap { (id, _) ->
            lessons.firstOrNull { it.id == id }?.let { lesson -> lesson.prerequisites.sortedBy { mastery[it]?.percent } + id }
                .orEmpty()
        }
        val due = mastery.filter { (_, value) -> value.nextReviewAt?.let { it <= now } == true }.keys
        val byId = lessons.associateBy(Lesson::id)
        val completed = completions.map(Completion::lessonId).toSet()
        val ready = lessons.firstOrNull { lesson -> lesson.id !in completed }
        val recommendations = (targeted + due + listOfNotNull(ready?.id)).distinct().take(5).mapNotNull(byId::get)
        val xp = attempts.sumOf(Attempt::xp)
        val accuracy = if (attempts.isEmpty()) 0 else
            (100.0 * attempts.count(Attempt::correct) / attempts.size).roundToInt()
        return Progress(xp, byDate[today].orEmpty().sumOf(Attempt::xp), streak(byDate.keys, today),
            (attempts.sumOf(Attempt::durationMillis) / 60_000).toInt(), accuracy, mastery, mistakes, recommendations,
            completed, recentDays)
    }

    private fun streak(days: Set<LocalDate>, today: LocalDate): Int {
        var date = if (today in days) today else today.minusDays(1)
        var count = 0
        while (date in days) {
            date = date.minusDays(1)
            count++
        }
        return count
    }

    fun xp(correct: Boolean, difficulty: Difficulty) = if (correct) 10 + difficulty.ordinal * 5 else 0
}

enum class ChallengeMode(val label: String) {
    Random("Aleatório"), Review("Revisão"), Exam("Prova"), Infinite("Infinito"), Calculation("Cálculo"),
    Daily("Desafio diário")
}

data class ChallengeSettings(
    val lessonId: String? = null,
    val difficulty: Difficulty? = null,
    val count: Int = 5,
    val mode: ChallengeMode = ChallengeMode.Random
)

object QuestionSelection {
    fun followUp(question: Question, bank: List<Question>, excluded: Set<String>): Question? {
        val related = bank.filter { it.id in question.related && it.id !in excluded && it.id != question.id }
        return related.minByOrNull { kotlin.math.abs(it.difficulty.ordinal - question.difficulty.ordinal) }
    }

    fun select(
        bank: List<Question>, settings: ChallengeSettings, attempts: List<Attempt>,
        mastery: Map<String, Mastery>, exclude: Set<String> = emptySet(), now: Long,
        random: Random = Random.Default
    ): List<Question> {
        val recent = attempts.sortedByDescending(Attempt::atMillis).take(12).map(Attempt::questionId).toSet()
        val eligible = bank.filter {
            (settings.lessonId == null || settings.lessonId == it.lessonId) &&
                (settings.difficulty == null || settings.difficulty == it.difficulty) &&
                (settings.mode != ChallengeMode.Calculation || it.expected is AnswerSpec.Numeric) &&
                it.id !in exclude
        }
        return eligible.shuffled(random).sortedWith(
            compareBy<Question> {
                if (settings.mode != ChallengeMode.Review) 0 else {
                    val record = mastery[it.lessonId]
                    when {
                        record?.nextReviewAt?.let { date -> date <= now } == true -> 0
                        record != null && record.attempts > 0 -> 1
                        else -> 2
                    }
                }
            }.thenBy {
                if (settings.mode == ChallengeMode.Daily) 0 else if (it.id in recent) 1 else 0
            }.thenBy {
                if (settings.difficulty != null || settings.mode == ChallengeMode.Daily) 0
                else kotlin.math.abs(it.difficulty.ordinal - Learning.difficulty(mastery[it.lessonId]).ordinal)
            }
        ).take(settings.count.coerceIn(1, 30))
    }
}
