package dev.quantumlearn.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CorrectionsTest {
    @Test fun everyAuthoredQuestionHasAnAcceptedAnswerAndEachDistractorHasSpecificFeedback() {
        QuestionBank.validate(Curriculum.modules)
        assertEquals(listOf(6, 6, 8, 8, 6, 9, 9, 6, 5, 5, 6), Curriculum.modules.map { it.lessons.size })
        for (question in Curriculum.questions) {
            when (val expected = question.expected) {
                is AnswerSpec.Choice -> {
                    val notes = mutableSetOf<String>()
                    for (index in expected.options.indices) {
                        val feedback = Corrections.grade(question, Answer.Selection(index))
                        assertEquals(expected.options[index].label, feedback.answer)
                        assertEquals(expected.options[expected.correctIndex].label, feedback.correctAnswer)
                        assertTrue(feedback.steps.size >= 2)
                        assertTrue(feedback.example.isNotBlank())
                        if (index == expected.correctIndex) assertEquals(Outcome.Correct, feedback.outcome)
                        else {
                            assertEquals(Outcome.Incorrect, feedback.outcome)
                            assertNotNull(feedback.errorKind)
                            assertNotNull(feedback.confusion)
                            assertTrue(notes.add(feedback.explanation))
                        }
                    }
                }
                is AnswerSpec.Numeric -> {
                    val solution = listOfNotNull(expected.expression, expected.unit).joinToString(" ")
                    assertEquals(question.id, Outcome.Correct, Corrections.grade(question, Answer.Text(solution)).outcome)
                }
                is AnswerSpec.Sequence -> {
                    val right = expected.order.joinToString(",")
                    val wrong = expected.order.reversed().joinToString(",")
                    assertEquals(Outcome.Correct, Corrections.grade(question, Answer.Text(right)).outcome)
                    assertEquals(Outcome.Incorrect, Corrections.grade(question, Answer.Text(wrong)).outcome)
                }
                is AnswerSpec.Matching -> {
                    val right = expected.pairs.indices.joinToString(",")
                    val wrong = expected.pairs.indices.reversed().joinToString(",")
                    assertEquals(Outcome.Correct, Corrections.grade(question, Answer.Text(right)).outcome)
                    val feedback = Corrections.grade(question, Answer.Text(wrong))
                    assertEquals(Outcome.Incorrect, feedback.outcome)
                    assertTrue(expected.pairs.first().explanation in feedback.explanation)
                }
                is AnswerSpec.Written -> {
                    assertEquals(Outcome.Correct, Corrections.grade(question, Answer.Text(expected.model)).outcome)
                    for (answer in expected.accepted) {
                        assertEquals(Outcome.Correct, Corrections.grade(question, Answer.Text(answer)).outcome)
                    }
                }
            }
        }
    }

    @Test fun incorrectChoiceReturnsTheSelectedMisconceptionAndDetailedSolution() {
        val question = Curriculum.byId.getValue("operations").questions.first()
        val earlySum = Corrections.grade(question, Answer.Selection(0))
        val onlyProduct = Corrections.grade(question, Answer.Selection(3))
        assertEquals("20", earlySum.answer)
        assertEquals("14", earlySum.correctAnswer)
        assertEquals("Prioridade da multiplicação", earlySum.confusion)
        assertNotEquals(earlySum.explanation, onlyProduct.explanation)
        assertEquals(listOf("Calcule 3 × 4 = 12.", "Some 2 + 12 = 14."), earlySum.steps)
    }

    @Test fun writtenAnswersThatAreAmbiguousOrContradictoryAskForClarification() {
        val question = Curriculum.byId.getValue("double-slit").questions.last()
        for (answer in listOf("Acho que são as probabilidades.", "Não são as amplitudes dos caminhos.",
            "Somam as amplitudes das fendas depois das probabilidades")) {
            val feedback = Corrections.grade(question, Answer.Text(answer))
            assertEquals(Outcome.Clarify, feedback.outcome)
            assertNull(feedback.errorKind)
            assertTrue(feedback.steps.isEmpty())
        }
        assertEquals(Outcome.Correct,
            Corrections.grade(question, Answer.Text("AS amplitudes das fendas.")).outcome)
    }

    @Test fun recognizesMathematicalMistakeAndUnitMistakeSeparately() {
        val question = Curriculum.byId.getValue("waves").questions.last()
        assertEquals(ErrorKind.Unit, Corrections.grade(question, Answer.Text("8 m")).errorKind)
        assertEquals(ErrorKind.Formula, Corrections.grade(question, Answer.Text("0,5 m/s")).errorKind)
        val unfamiliar = Corrections.grade(question, Answer.Text("9 m/s"))
        assertEquals(Outcome.Incorrect, unfamiliar.outcome)
        assertNull(unfamiliar.errorKind)
        assertEquals(Outcome.Clarify, Corrections.grade(question, Answer.Text("abc")).outcome)
    }

    @Test fun generatedExercisesAreReproducibleAndGradeTheirSolutions() {
        val topics = Curriculum.lessons.map(Lesson::id).filter { Curriculum.track(it) == Track.Science }
        for (topic in topics) for (seed in 0..99) {
            val question = requireNotNull(Practice.generate(topic, seed))
            assertEquals(question, Practice.restore(question.id))
            val spec = question.expected as AnswerSpec.Numeric
            val value = listOfNotNull(spec.expression, spec.unit).joinToString(" ")
            assertEquals(Outcome.Correct, Corrections.grade(question, Answer.Text(value)).outcome)
            for (mistake in spec.mistakes.keys) {
                val wrong = listOfNotNull(mistake, spec.unit).joinToString(" ")
                assertEquals(spec.mistakes.getValue(mistake).errorKind, Corrections.grade(question, Answer.Text(wrong)).errorKind)
            }
        }
    }
}
