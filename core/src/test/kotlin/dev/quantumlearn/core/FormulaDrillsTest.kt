package dev.quantumlearn.core

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FormulaDrillsTest {
    @Test
    fun everyLessonHasAGuide() {
        val missing = Curriculum.lessons.map(Lesson::id).filter { FormulaGuides.byId[it] == null }
        assertTrue("Lições sem guia: $missing", missing.isEmpty())
        assertEquals(Curriculum.lessons.size, FormulaGuides.all.size)
    }

    @Test
    fun guidesAreComplete() {
        for (guide in FormulaGuides.all) {
            assertTrue(guide.lessonId, guide.symbols.size >= 2)
            assertTrue(guide.lessonId, guide.symbols.map { it.symbol }.distinct().size == guide.symbols.size)
            assertTrue(guide.lessonId, guide.symbols.map { it.meaning }.distinct().size == guide.symbols.size)
            assertTrue(guide.lessonId, guide.build.size >= 3 && guide.build.none { it.text.isBlank() || it.latex.isBlank() })
            assertTrue(guide.lessonId, guide.cases.size >= 2)
            assertTrue(guide.lessonId, guide.cases.any { it.ask != null || it.pick != null })
            assertTrue(guide.lessonId, (guide.plug == null) != (guide.pick == null))
            assertNotNull(guide.lessonId, guide.isolation)
            assertTrue(guide.lessonId, guide.reading.isNotBlank() && guide.text.isNotBlank())
        }
    }

    @Test
    fun everyGeneratedQuestionIsSolvable() {
        for (guide in FormulaGuides.all) {
            val questions = FormulaDrills.all(guide.lessonId)
            assertTrue(guide.lessonId, questions.size >= 10)
            for (question in questions) {
                assertTrue(question.id, question.steps.size >= 2 && question.steps.none(String::isBlank))
                assertTrue(question.id, question.prompt.isNotBlank() && question.alternativeExample.isNotBlank())
                when (val expected = question.expected) {
                    is AnswerSpec.Choice -> {
                        assertTrue(question.id, expected.options.size >= 2)
                        assertEquals(question.id, expected.options.size, expected.options.map { it.label }.distinct().size)
                        expected.options.forEachIndexed { index, option ->
                            assertTrue(question.id, option.explanation.isNotBlank())
                            if (index != expected.correctIndex) assertNotNull(question.id, option.errorKind)
                        }
                        val graded = Corrections.grade(question, Answer.Selection(expected.correctIndex))
                        assertEquals(question.id, Outcome.Correct, graded.outcome)
                    }
                    is AnswerSpec.Numeric -> {
                        assertTrue("${question.id} ${expected.unit}", expected.unit?.let(MathValidation::supportsUnit) != false)
                        val solution = listOfNotNull(expected.expression, expected.unit).joinToString(" ")
                        assertEquals("${question.id}: $solution", MathValidation.Result.Equivalent,
                            MathValidation.compare(solution, expected))
                        for ((value, note) in expected.mistakes) {
                            val wrong = listOfNotNull(value, expected.unit).joinToString(" ")
                            assertEquals("${question.id}: armadilha $value", Outcome.Incorrect,
                                Corrections.grade(question, Answer.Text(wrong)).outcome)
                            assertTrue(question.id, note.explanation.isNotBlank())
                        }
                    }
                    else -> error("Tipo inesperado em ${question.id}")
                }
                assertEquals(question.id, question, FormulaDrills.restore(question.id))
            }
        }
    }

    @Test
    fun sessionStartsWithSymbolsAndEndsWithRealCases() {
        for (guide in FormulaGuides.all) {
            val session = FormulaDrills.session(guide.lessonId, Random(7))
            assertTrue(guide.lessonId, session.size >= 6)
            assertEquals(session.size, session.map(Question::prompt).distinct().size)
            assertTrue(session.first().id.contains("-formula-simbolo-"))
            assertTrue(session.last().id.contains("-formula-real-"))
            assertTrue(session.all(FormulaDrills::isDrill))
        }
    }

    @Test
    fun unknownIdsAreRejected() {
        assertEquals(null, FormulaDrills.restore("waves-formula-conta--1"))
        assertEquals(null, FormulaDrills.restore("inexistente-formula-conta-1"))
        assertEquals(null, FormulaDrills.restore("waves-formula-outro-1"))
    }
}
