package dev.quantumlearn.core

import dev.quantumlearn.core.MathValidation.Result
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MathValidationTest {
    @Test fun acceptsEquivalentFractionsAndScientificNotation() {
        assertEquals(Result.Equivalent, MathValidation.compare("10/15", AnswerSpec.Numeric("2/3")))
        assertEquals(Result.Equivalent, MathValidation.compare("3,2 × 10^−4", AnswerSpec.Numeric("3.2e-4")))
        assertEquals(Result.Equivalent, MathValidation.compare("2.5e+3", AnswerSpec.Numeric("2500")))
    }

    @Test fun evaluatesPrecedenceRootsConstantsAndRightAssociativePowers() {
        assertEquals(14.0, MathValidation.evaluate("2+3*4")!!, 1e-12)
        assertEquals(20.0, MathValidation.evaluate("(2+3)*4")!!, 1e-12)
        assertEquals(-4.0, MathValidation.evaluate("-2^2")!!, 1e-12)
        assertEquals(512.0, MathValidation.evaluate("2^3^2")!!, 1e-12)
        assertEquals(3.0, MathValidation.evaluate("sqrt(9)")!!, 1e-12)
        assertEquals(6.62607015e-34, MathValidation.evaluate("h")!!, 1e-41)
    }

    @Test fun convertsCompatibleUnitsAndRejectsMissingOrIncompatibleOnes() {
        assertEquals(Result.Equivalent, MathValidation.compare("36 km/h", AnswerSpec.Numeric("10", "m/s")))
        assertEquals(Result.Equivalent, MathValidation.compare("500 nm", AnswerSpec.Numeric("5e-7", "m")))
        assertEquals(Result.Equivalent, MathValidation.compare("1.602176634e-19 J", AnswerSpec.Numeric("1", "eV")))
        assertEquals(Result.UnitDiffers, MathValidation.compare("10", AnswerSpec.Numeric("10", "m/s")))
        assertEquals(Result.UnitDiffers, MathValidation.compare("10 J", AnswerSpec.Numeric("10", "m/s")))
    }

    @Test fun validatesToleranceAtVerySmallScales() {
        val strict = AnswerSpec.Numeric("1e-30", relativeTolerance = 0.01)
        assertEquals(Result.Equivalent, MathValidation.compare("1.001e-30", strict))
        assertEquals(Result.ValueDiffers, MathValidation.compare("2e-30", strict))
        assertEquals(Result.ValueDiffers, MathValidation.compare("0", strict))
        assertEquals(Result.Equivalent, MathValidation.compare("0.005", AnswerSpec.Numeric("0", absoluteTolerance = .01)))
    }

    @Test fun checksRepresentationWhenTheLearningGoalRequiresIt() {
        val fraction = AnswerSpec.Numeric("2/3", format = NumberFormat.ReducedFraction)
        val scientific = AnswerSpec.Numeric("3.2e-4", format = NumberFormat.Scientific)
        assertEquals(Result.FormatDiffers, MathValidation.compare("6/9", fraction))
        assertEquals(Result.Equivalent, MathValidation.compare("2 / 3", fraction))
        assertEquals(Result.FormatDiffers, MathValidation.compare("0,00032", scientific))
        assertEquals(Result.FormatDiffers, MathValidation.compare("32e-5", scientific))
        assertEquals(Result.Equivalent, MathValidation.compare("3,2 × 10^−4", scientific))
    }

    @Test fun rejectsUndefinedAndMalformedExpressions() {
        for (source in listOf("1/0", "sqrt(-1)", "1+", "2xs", "1;2", "(".repeat(45) + "1" + ")".repeat(45))) {
            assertNull(source, MathValidation.evaluate(source))
        }
    }
}
