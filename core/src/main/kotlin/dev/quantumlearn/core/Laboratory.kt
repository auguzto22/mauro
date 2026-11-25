package dev.quantumlearn.core

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.sinh
import kotlin.math.sqrt

object Laboratory {
    private const val h = 6.62607015e-34
    private const val electronCharge = 1.602176634e-19
    private const val electronMass = 9.1093837139e-31

    fun doubleSlit(sineAngle: Double, wavelengthNm: Double, separationUm: Double): Double {
        val slitWidthNm = 12_000.0
        val single = PI * slitWidthNm * sineAngle / wavelengthNm
        val pair = PI * separationUm * 1000 * sineAngle / wavelengthNm
        val envelope = if (single == 0.0) 1.0 else sin(single) / single
        return envelope * envelope * cos(pair) * cos(pair)
    }

    fun interference(coordinate: Double, relativePhase: Double) =
        sin(2 * PI * coordinate + relativePhase)

    fun gaussian(position: Double, mean: Double, sigma: Double): Double {
        val standardized = (position - mean) / sigma
        return exp(-standardized * standardized / 2) / (sqrt(2 * PI) * sigma)
    }

    fun wellWave(relativePosition: Double, n: Int, lengthNm: Double) =
        sqrt(2 / lengthNm) * sin(n * PI * relativePosition)

    fun wellEnergyEv(n: Int, lengthNm: Double): Double {
        val length = lengthNm * 1e-9
        return n * n * h * h / (8 * electronMass * length * length * electronCharge)
    }

    fun transmission(energyRatio: Double, width: Double): Double {
        val missing = 1 - energyRatio
        val decay = sinh(width * sqrt(missing))
        return 1 / (1 + decay * decay / (4 * energyRatio * missing))
    }

    fun oscillatorDensity(position: Double, n: Int): Double {
        val polynomial = when (n) {
            0 -> 1.0
            1 -> 2 * position
            2 -> 4 * position * position - 2
            3 -> 8 * position * position * position - 12 * position
            else -> error("A simulação oferece n de 0 a 3.")
        }
        val normalization = sqrt(PI) * listOf(1, 2, 8, 48)[n]
        return polynomial * polynomial * exp(-position * position) / normalization
    }

    fun positiveSpinProbability(theta: Double): Double {
        val projection = cos(theta / 2)
        return projection * projection
    }

    data class BlochVector(val x: Double, val y: Double, val z: Double)

    fun bloch(theta: Double, phi: Double) = BlochVector(sin(theta) * cos(phi), sin(theta) * sin(phi), cos(theta))
}
