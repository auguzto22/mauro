package dev.quantumlearn.core

import kotlin.math.PI
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LaboratoryTest {
    @Test fun centerFringeIsMaximumAndFirstMinimumCanBeLocated() {
        assertEquals(1.0, Laboratory.doubleSlit(0.0, 550.0, 65.0), 1e-12)
        assertEquals(0.0, Laboratory.doubleSlit(550.0 / (2 * 65000), 550.0, 65.0), 1e-12)
    }

    @Test fun oppositeWavesCancelAndGaussianIntegratesToOne() {
        val point = .13
        assertEquals(0.0, Laboratory.interference(point, 0.0) + Laboratory.interference(point, PI), 1e-12)
        val integrated = (0..2000).sumOf { Laboratory.gaussian(it * .01 - 10, .3, .6) * .01 }
        assertEquals(1.0, integrated, 1e-10)
    }

    @Test fun fixingEnergyAndIncreasingBarrierWidthReducesTransmission() {
        val thin = Laboratory.transmission(.5, .5)
        val thick = Laboratory.transmission(.5, 4.0)
        assertTrue(thin < 1 && thick > 0 && thin > thick)
    }

    @Test fun boxAndOscillatorRespectBoundaryConditionsAndNormalization() {
        assertEquals(0.0, Laboratory.wellWave(0.0, 3, 2.0), 1e-12)
        assertEquals(0.0, Laboratory.wellWave(1.0, 3, 2.0), 1e-12)
        assertEquals(4.0, Laboratory.wellEnergyEv(2, 1.0) / Laboratory.wellEnergyEv(1, 1.0), 1e-12)
        for (n in 0..3) {
            val integral = (0..2000).sumOf { Laboratory.oscillatorDensity(it * .01 - 10, n) * .01 }
            assertEquals(1.0, integral, 1e-10)
        }
    }

    @Test fun spinAndBlochAgreeOnComputationalBasisProbabilities() {
        assertEquals(1.0, Laboratory.positiveSpinProbability(0.0), 1e-12)
        assertEquals(.5, Laboratory.positiveSpinProbability(PI / 2), 1e-12)
        assertEquals(0.0, Laboratory.positiveSpinProbability(PI), 1e-12)
        val state = Laboratory.bloch(PI / 3, PI / 4)
        assertEquals(1.0, state.x * state.x + state.y * state.y + state.z * state.z, 1e-12)
        assertEquals((1 + state.z) / 2, Laboratory.positiveSpinProbability(PI / 3), 1e-12)
    }
}
