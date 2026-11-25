package dev.quantumlearn.core

import kotlin.math.E
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

object MathValidation {
    private data class UnitScale(val dimension: String, val scale: Double)

    private val units = mapOf(
        "m" to UnitScale("length", 1.0), "km" to UnitScale("length", 1e3),
        "cm" to UnitScale("length", 1e-2), "mm" to UnitScale("length", 1e-3),
        "µm" to UnitScale("length", 1e-6), "nm" to UnitScale("length", 1e-9),
        "s" to UnitScale("time", 1.0), "ms" to UnitScale("time", 1e-3),
        "kg" to UnitScale("mass", 1.0), "g" to UnitScale("mass", 1e-3),
        "J" to UnitScale("energy", 1.0), "eV" to UnitScale("energy", 1.602176634e-19),
        "Hz" to UnitScale("frequency", 1.0), "kHz" to UnitScale("frequency", 1e3),
        "N" to UnitScale("force", 1.0), "V" to UnitScale("voltage", 1.0),
        "A" to UnitScale("current", 1.0), "mA" to UnitScale("current", 1e-3),
        "Ω" to UnitScale("resistance", 1.0), "kΩ" to UnitScale("resistance", 1e3), "m/s" to UnitScale("speed", 1.0),
        "km/h" to UnitScale("speed", 1.0 / 3.6)
    )
    private val quantity = Regex("""(.+?)\s+([a-zA-ZµμΩ]+(?:/[a-zA-Z]+)?)""")
    private val number = Regex("""(?:\d+(?:\.\d*)?|\.\d+)(?:[eE][+-]?\d+)?""")
    private val fractionForm = Regex("""([+-]?\d+)\s*/\s*(\d+)""")
    private val scientificForm = Regex("""([+-]?\d+(?:[.,]\d+)?)(?:[eE]|\s*[×*]\s*10\s*\^\s*)[+−-]?\d+""")
    private val identifier = Regex("""[a-zA-Zπℏ]+""")
    private val constants = mapOf(
        "e" to E, "pi" to Math.PI, "π" to Math.PI, "h" to 6.62607015e-34,
        "hbar" to 6.62607015e-34 / (2 * Math.PI), "ℏ" to 6.62607015e-34 / (2 * Math.PI),
        "c" to 299792458.0
    )

    enum class Result { Equivalent, ValueDiffers, UnitDiffers, FormatDiffers, Unreadable }

    fun evaluate(input: String): Double? {
        if (input.length > 160 || input.isBlank()) return null
        val normalized = input.trim().replace(',', '.').replace('−', '-')
            .replace('×', '*').replace('·', '*').replace('÷', '/')
            .replace("**", "^")
        return runCatching { Parser(normalized).parse() }.getOrNull()?.takeIf(Double::isFinite)
    }

    fun compare(input: String, expected: AnswerSpec.Numeric): Result {
        if (input.length > 160) return Result.Unreadable
        val expectedValue = evaluate(expected.expression) ?: return Result.Unreadable
        val match = quantity.matchEntire(input.trim())
        val directValue = evaluate(input)
        val inputUnit = match?.groupValues?.get(2)?.replace('μ', 'µ').takeIf { directValue == null }
        val value = directValue ?: evaluate(match?.groupValues?.get(1).orEmpty()) ?: return Result.Unreadable
        val expectedUnit = expected.unit?.let(units::get)
        val givenUnit = inputUnit?.let(units::get)
        if ((inputUnit == null) != (expected.unit == null) ||
            (inputUnit != null && (givenUnit == null || givenUnit.dimension != expectedUnit?.dimension))
        ) return Result.UnitDiffers
        val converted = if (givenUnit != null && expectedUnit != null) {
            value * givenUnit.scale / expectedUnit.scale
        } else value
        if (!converted.isFinite()) return Result.ValueDiffers
        val tolerance = max(expected.absoluteTolerance, abs(expectedValue) * expected.relativeTolerance)
        if (abs(converted - expectedValue) > tolerance) return Result.ValueDiffers
        val expression = if (inputUnit == null) input.trim() else match?.groupValues?.get(1).orEmpty()
        return if (matchesFormat(expression, expected.format)) Result.Equivalent else Result.FormatDiffers
    }

    private fun matchesFormat(input: String, format: NumberFormat): Boolean = when (format) {
        NumberFormat.Value -> true
        NumberFormat.ReducedFraction -> fractionForm.matchEntire(input)?.destructured?.let { (top, bottom) ->
            top.toBigInteger().gcd(bottom.toBigInteger()) == java.math.BigInteger.ONE
        } == true
        NumberFormat.Scientific -> scientificForm.matchEntire(input)?.groupValues?.get(1)
            ?.replace(',', '.')?.toDoubleOrNull()?.let { abs(it) >= 1 && abs(it) < 10 } == true
    }

    fun supportsUnit(unit: String) = unit in units

    private class Parser(private val source: String) {
        private var position = 0
        private var depth = 0

        fun parse(): Double {
            val result = sum()
            skipSpace()
            require(position == source.length)
            return result
        }

        private fun sum(): Double {
            var result = product()
            while (true) {
                result = when {
                    accept('+') -> result + product()
                    accept('-') -> result - product()
                    else -> return result
                }
            }
        }

        private fun product(): Double {
            var result = signed()
            while (true) {
                result = when {
                    accept('*') -> result * signed()
                    accept('/') -> result / signed()
                    else -> return result
                }
            }
        }

        private fun signed(): Double {
            require(++depth <= 32)
            try {
                return when {
                    accept('+') -> signed()
                    accept('-') -> -signed()
                    else -> {
                        val base = atom()
                        if (accept('^')) base.pow(signed()) else base
                    }
                }
            } finally {
                depth--
            }
        }

        private fun atom(): Double {
            if (accept('(')) {
                val value = sum()
                require(accept(')'))
                return value
            }
            skipSpace()
            token(number)?.let { return it.toDouble() }
            val name = requireNotNull(token(identifier))
            if (name == "sqrt") {
                require(accept('('))
                val root = sqrt(sum())
                require(accept(')'))
                return root
            }
            return requireNotNull(constants[name])
        }

        private fun token(pattern: Regex): String? =
            pattern.matchAt(source, position)?.also { position = it.range.last + 1 }?.value

        private fun accept(character: Char): Boolean {
            skipSpace()
            if (source.getOrNull(position) != character) return false
            position++
            return true
        }

        private fun skipSpace() {
            while (source.getOrNull(position)?.isWhitespace() == true) position++
        }
    }
}
