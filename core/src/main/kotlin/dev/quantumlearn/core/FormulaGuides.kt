package dev.quantumlearn.core

object FormulaGuides {
    val all: List<FormulaGuide> by lazy { mathGuides() + physicsGuides() + quantumGuides() + englishGuides() + wordGuides() }
    val byId: Map<String, FormulaGuide> by lazy { all.associateBy(FormulaGuide::lessonId) }
}

internal fun guide(
    id: String, latex: String, text: String, reading: String,
    symbols: List<FormulaSymbol>, build: List<FormulaStep>, cases: List<RealCase>,
    isolation: Isolation?, plug: (Int, Int) -> Plug
) = FormulaGuide(id, latex, text, reading, symbols, build, cases, isolation, plug)

internal fun sym(symbol: String, meaning: String, unit: String = "") = FormulaSymbol(symbol, meaning, unit)
internal fun step(latex: String, text: String) = FormulaStep(latex, text)
internal fun real(scene: Scene, place: String, story: String, math: String, ask: Plug? = null) =
    RealCase(scene, place, story, math, ask)
internal fun iso(prompt: String, answer: String, why: String, vararg traps: Pair<String, String>) =
    Isolation(prompt, answer, why, traps.toList())
internal fun plug(
    prompt: String, result: String, substitute: String, compute: String, unit: String? = null,
    trap: String? = null, trapWhy: String = "", tolerance: Double = 1e-7, format: NumberFormat = NumberFormat.Value
) = Plug(prompt, result, substitute, compute, unit, trap, trapWhy, tolerance, format)

internal fun gcd(a: Int, b: Int): Int = if (b == 0) kotlin.math.abs(a) else gcd(b, a % b)

/** Ternos pitagóricos usados em contas com raiz exata. */
internal val triples = listOf(
    Triple(3, 4, 5), Triple(6, 8, 10), Triple(5, 12, 13), Triple(8, 15, 17),
    Triple(9, 12, 15), Triple(12, 16, 20), Triple(7, 24, 25), Triple(20, 21, 29)
)
