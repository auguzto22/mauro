package dev.quantumlearn.core

import java.time.Instant
import java.time.ZoneId
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LearningTest {
    private val day = 86_400_000L
    private val now = Instant.parse("2026-11-25T16:00:00Z").toEpochMilli()
    private val zone = ZoneId.of("America/Sao_Paulo")

    private fun attempt(id: String, right: Boolean, at: Long, lesson: String = "operations",
                        difficulty: Difficulty = Difficulty.Introductory) = Attempt(
        "$id-$at", id, lesson, right, difficulty,
        ErrorKind.Formula.takeUnless { right }, "Prioridade".takeUnless { right },
        at, 45_000, Learning.xp(right, difficulty)
    )

    @Test fun masteryNeedsDifferentQuestionsAndRespondsToNewEvidence() {
        val one = List(8) { attempt("same", true, now + it) }
        assertEquals(25, Learning.mastery(one).percent)
        val distinct = (1..4).map { attempt("q$it", true, now + it) }
        assertEquals(100, Learning.mastery(distinct).percent)
        val afterError = distinct + attempt("q2", false, now + 10)
        assertEquals(75, Learning.mastery(afterError).percent)
        assertEquals(Difficulty.Advanced, Learning.difficulty(Learning.mastery(afterError)))
        assertEquals(Difficulty.Introductory, Learning.difficulty(null))
    }

    @Test fun reviewsAdvanceOnlyAfterIntervalAndErrorsResetIt() {
        val first = attempt("q1", true, now)
        val early = attempt("q2", true, now + day / 2)
        assertEquals(now + day, Learning.mastery(listOf(first, early)).nextReviewAt)
        assertEquals(0, Learning.mastery(listOf(first, early)).retainedChecks)
        val review = attempt("q2", true, now + day)
        val next = Learning.mastery(listOf(first, review))
        assertEquals(now + 4 * day, next.nextReviewAt)
        assertEquals(1, next.retainedChecks)
        val gap = attempt("q3", false, now + 4 * day)
        assertEquals(now + 5 * day, Learning.mastery(listOf(first, review, gap)).nextReviewAt)
    }

    @Test fun calculatesDailyXpStreakStudyTimeAndAccuracy() {
        val history = listOf(attempt("a", true, now - 2 * day), attempt("b", false, now - day),
            attempt("c", true, now, difficulty = Difficulty.Intermediate))
        val result = Learning.progress(Curriculum.lessons, history, listOf(Completion("operations", now)), now, zone)
        assertEquals(25, result.xp)
        assertEquals(15, result.xpToday)
        assertEquals(3, result.streak)
        assertEquals(2, result.minutes)
        assertEquals(67, result.accuracy)
        assertEquals(setOf("operations"), result.completions)
        assertEquals(0, Learning.progress(Curriculum.lessons, history, emptyList(), now + 2 * day, zone).streak)
    }

    @Test fun recommendsPrerequisitesWhenErrorsRepeat() {
        val history = (1..3).map { attempt("wave-$it", false, now - it, "wave-function") }
        val progress = Learning.progress(Curriculum.lessons, history, emptyList(), now, zone)
        assertEquals("Prioridade" to 3, progress.errors.first())
        assertTrue(progress.recommendations.any { it.id == "complex" })
        assertTrue(progress.recommendations.any { it.id == "functions" })
    }

    @Test fun challengeFiltersCountsAndAvoidsTheLatestQuestion() {
        val choices = Curriculum.byId.getValue("operations").questions
        val recent = choices.first()
        val history = listOf(attempt(recent.id, false, now - 100))
        val next = QuestionSelection.select(choices, ChallengeSettings(lessonId = "operations", count = 1),
            history, emptyMap(), now = now, random = Random(1)).single()
        assertNotEquals(recent.id, next.id)
        val numerical = QuestionSelection.select(Curriculum.questions, ChallengeSettings(count = 5, mode = ChallengeMode.Calculation),
            history, emptyMap(), now = now, random = Random(2))
        assertEquals(5, numerical.distinctBy(Question::id).size)
        assertTrue(numerical.all { it.expected is AnswerSpec.Numeric })
    }

    @Test fun reviewPrefersOverdueTopicsEvenIfTheirQuestionsWereSeen() {
        val question = Curriculum.byId.getValue("fractions").questions.first()
        val seen = attempt(question.id, true, now - 3 * day, question.lessonId)
        val mastery = mapOf(question.lessonId to Learning.mastery(listOf(seen)))
        val chosen = QuestionSelection.select(listOf(question, Curriculum.questions.first()),
            ChallengeSettings(mode = ChallengeMode.Review, count = 1), listOf(seen), mastery,
            now = now, random = Random(4)).single()
        assertEquals(question.id, chosen.id)
    }

    @Test fun followUpExcludesPreviouslyUsedPromptsAndKeepsTheConcept() {
        val bank = Curriculum.byId.getValue("operations").questions
        val first = bank.first()
        val related = requireNotNull(QuestionSelection.followUp(first, bank, setOf(bank[1].id)))
        assertEquals(first.lessonId, related.lessonId)
        assertEquals(bank[2].id, related.id)
        assertNull(QuestionSelection.followUp(first, bank, bank.map(Question::id).toSet()))
    }
}
