package dev.quantumlearn.core

import kotlin.random.Random

/** Um símbolo da fórmula com o que ele significa e a unidade em que costuma aparecer. */
data class FormulaSymbol(val symbol: String, val meaning: String, val unit: String = "")

/** Uma etapa que mostra como a fórmula funciona por dentro. */
data class FormulaStep(val latex: String, val text: String)

/** Ambiente do exemplo real, usado para escolher o ícone e a cor do cartão. */
enum class Scene(val label: String) {
    Home("Em casa"), Phone("No celular"), Health("Saúde"), Space("Espaço"), Transport("Transporte"),
    Money("Dinheiro"), Sport("Esporte"), Light("Luz e cor"), Nature("Natureza"), Lab("Laboratório"),
    Computer("Tecnologia"), Music("Som e música")
}

/** Uma conta de aplicação direta: substituir valores na fórmula e calcular. */
data class Plug(
    val prompt: String,
    val result: String,
    val substitute: String,
    val compute: String,
    val unit: String? = null,
    val trap: String? = null,
    val trapWhy: String = "",
    val tolerance: Double = 1e-7,
    val format: NumberFormat = NumberFormat.Value
)

/** Onde a fórmula aparece de verdade, com a conta e, se houver, uma pergunta para resolver. */
data class RealCase(val scene: Scene, val place: String, val story: String, val math: String, val ask: Plug? = null)

/** Pergunta de estrutura da fórmula: isolar uma grandeza, prever um efeito ou ordenar operações. */
data class Isolation(val prompt: String, val answer: String, val why: String, val traps: List<Pair<String, String>>)

data class FormulaGuide(
    val lessonId: String,
    val latex: String,
    val text: String,
    val reading: String,
    val symbols: List<FormulaSymbol>,
    val build: List<FormulaStep>,
    val cases: List<RealCase>,
    val isolation: Isolation?,
    val plug: (Int, Int) -> Plug
) {
    val formula get() = Formula(latex, symbols.joinToString(" · ") { symbol ->
        symbol.symbol + ": " + symbol.meaning + if (symbol.unit.isBlank()) "" else " (${symbol.unit})"
    })
}

/**
 * Questões focadas só na fórmula. Elas treinam, em ordem crescente:
 * o papel de cada símbolo, a estrutura (isolar ou prever), contas diretas e aplicações reais.
 */
object FormulaDrills {
    private val format = Regex("""^(.+)-formula-([a-z]+)-(\d+)$""")
    private const val SEEDS = 64

    fun guide(lessonId: String): FormulaGuide? = FormulaGuides.byId[lessonId]

    fun isDrill(question: Question) = format.matches(question.id)

    fun restore(id: String): Question? {
        val match = format.matchEntire(id) ?: return null
        val n = match.groupValues[3].toIntOrNull() ?: return null
        return generate(match.groupValues[1], match.groupValues[2], n)
    }

    /** Sessão de treino: símbolos, estrutura, duas contas e os casos reais, nessa ordem. */
    fun session(lessonId: String, random: Random = Random.Default): List<Question> {
        val guide = guide(lessonId) ?: return emptyList()
        val symbols = guide.symbols.indices.shuffled(random)
        val calculations = List(SEEDS) { it }.shuffled(random).asSequence()
            .mapNotNull { generate(lessonId, "conta", it) }.distinctBy(Question::prompt).take(2).toList()
        return listOfNotNull(
            symbols.getOrNull(0)?.let { generate(lessonId, "simbolo", it) },
            symbols.getOrNull(1)?.let { generate(lessonId, "letra", it) },
            generate(lessonId, "estrutura", 0),
        ) + calculations + guide.cases.indices.mapNotNull { generate(lessonId, "real", it) }.shuffled(random).take(2)
    }

    /** Uma conta nova da mesma fórmula, usada para reforçar depois de um erro. */
    fun calculation(lessonId: String, random: Random = Random.Default) =
        generate(lessonId, "conta", random.nextInt(SEEDS))

    /** Todas as questões possíveis, para validação. */
    fun all(lessonId: String): List<Question> {
        val guide = guide(lessonId) ?: return emptyList()
        return guide.symbols.indices.flatMap { listOfNotNull(generate(lessonId, "simbolo", it), generate(lessonId, "letra", it)) } +
            listOfNotNull(generate(lessonId, "estrutura", 0)) +
            (0 until SEEDS).mapNotNull { generate(lessonId, "conta", it) } +
            guide.cases.indices.mapNotNull { generate(lessonId, "real", it) }
    }

    fun generate(lessonId: String, kind: String, n: Int): Question? {
        if (n < 0) return null
        val guide = guide(lessonId) ?: return null
        val lesson = Curriculum.byId[lessonId] ?: return null
        val id = "$lessonId-formula-$kind-$n"
        fun question(prompt: String, expected: AnswerSpec, steps: List<String>, example: String, difficulty: Difficulty) =
            Question(id, lessonId, lesson.title, lesson.prerequisites, difficulty, prompt, expected, steps, example,
                guide.formula, lesson.references, lesson.questions.map(Question::id))
        return when (kind) {
            "simbolo" -> {
                val target = guide.symbols.getOrNull(n) ?: return null
                val others = guide.symbols.filter { it.meaning != target.meaning }.take(3)
                if (others.isEmpty()) return null
                val options = rotate(listOf(Option(target.meaning, "Isso mesmo. ${target.symbol} é ${target.meaning}${unitNote(target)}.")) +
                    others.map { other ->
                        Option(other.meaning, "Esse é o papel de ${other.symbol}. Em ${guide.text}, ${target.symbol} representa ${target.meaning}.",
                            ErrorKind.Formula, "Papel de cada símbolo na fórmula")
                    }, n)
                question("Em ${guide.text}, o que ${target.symbol} representa?",
                    AnswerSpec.Choice(options.first, options.second), listOf(
                        "Localize ${target.symbol} na fórmula ${guide.text}.",
                        "Leia a fórmula em voz alta: ${guide.reading}"
                    ), guide.symbols.joinToString(" · ") { "${it.symbol} = ${it.meaning}" }, Difficulty.Introductory)
            }
            "letra" -> {
                val target = guide.symbols.getOrNull(n) ?: return null
                val others = guide.symbols.filter { it.symbol != target.symbol && it.meaning != target.meaning }.take(3)
                if (others.isEmpty()) return null
                val options = rotate(listOf(Option(target.symbol, "Certo. ${target.meaning.replaceFirstChar(Char::uppercase)} é ${target.symbol}${unitNote(target)}.")) +
                    others.map { other ->
                        Option(other.symbol, "${other.symbol} representa ${other.meaning}. O símbolo pedido é ${target.symbol}.",
                            ErrorKind.Formula, "Papel de cada símbolo na fórmula")
                    }, n + 1)
                question("Qual símbolo de ${guide.text} representa ${target.meaning}?",
                    AnswerSpec.Choice(options.first, options.second), listOf(
                        "Pense no que a grandeza mede${unitNote(target)}.",
                        "Encontre na fórmula a letra que tem esse papel: ${target.symbol}."
                    ), guide.reading, Difficulty.Introductory)
            }
            "estrutura" -> {
                val isolation = guide.isolation ?: return null
                val options = rotate(listOf(Option(isolation.answer, isolation.why)) + isolation.traps.map { (label, why) ->
                    Option(label, why, ErrorKind.Formula, "Estrutura da fórmula")
                }, lessonId.length)
                question(isolation.prompt, AnswerSpec.Choice(options.first, options.second),
                    listOf("Escreva a fórmula: ${guide.text}.", isolation.why),
                    guide.build.last().text, Difficulty.Intermediate)
            }
            "conta" -> {
                val plug = seeded(guide, n % SEEDS)
                val other = seeded(guide, (n + 9) % SEEDS)
                question(plug.prompt, numeric(plug), listOf(plug.substitute, plug.compute),
                    "Com outros valores: ${other.substitute} ${other.compute}", Difficulty.Intermediate)
            }
            "real" -> {
                val case = guide.cases.getOrNull(n) ?: return null
                val plug = case.ask ?: return null
                question("${case.place} · ${plug.prompt}", numeric(plug), listOf(plug.substitute, plug.compute),
                    case.math, Difficulty.Advanced)
            }
            else -> null
        }
    }

    internal fun seeded(guide: FormulaGuide, n: Int): Plug = guide.plug(2 + n % 8, 2 + n / 8 % 8)

    private fun numeric(plug: Plug): AnswerSpec.Numeric {
        val unit = plug.unit?.takeIf(MathValidation::supportsUnit)
        val expected = MathValidation.evaluate(plug.result)
        val trap = plug.trap?.takeIf { trap ->
            val value = MathValidation.evaluate(trap)
            value != null && expected != null && kotlin.math.abs(value - expected) > kotlin.math.abs(expected) * 1e-6 + 1e-12
        }
        return AnswerSpec.Numeric(plug.result, unit, plug.tolerance,
            mistakes = trap?.let { mapOf(it to Option(it, plug.trapWhy, ErrorKind.Formula, "Uso da fórmula")) }.orEmpty(),
            format = plug.format)
    }

    private fun unitNote(symbol: FormulaSymbol) = if (symbol.unit.isBlank()) "" else " (unidade: ${symbol.unit})"

    private fun rotate(options: List<Option>, n: Int): Pair<List<Option>, Int> {
        val shift = n % options.size
        val rotated = options.drop(shift) + options.take(shift)
        return rotated to rotated.indexOf(options.first())
    }
}

internal fun br(value: Double): String {
    val text = if (value == Math.rint(value) && kotlin.math.abs(value) < 1e15) value.toLong().toString()
    else "%.4f".format(java.util.Locale.ROOT, value).trimEnd('0').trimEnd('.')
    return text.replace('.', ',').replace('-', '−')
}

/** Número em formato que o avaliador lê: ponto decimal e sem zeros sobrando. */
internal fun num(value: Double): String =
    if (value == Math.rint(value) && kotlin.math.abs(value) < 1e15) value.toLong().toString()
    else "%.6f".format(java.util.Locale.ROOT, value).trimEnd('0').trimEnd('.')
