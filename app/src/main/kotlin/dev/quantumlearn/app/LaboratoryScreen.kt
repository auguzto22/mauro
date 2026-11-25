package dev.quantumlearn.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import dev.quantumlearn.core.Curriculum
import dev.quantumlearn.core.Laboratory
import dev.quantumlearn.core.Reference
import java.util.Locale
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt

private data class Control(
    val label: String, val range: ClosedFloatingPointRange<Float>, val initial: Double,
    val suffix: String = "", val steps: Int = 0, val decimalPlaces: Int = 2
)

private enum class Experiment(
    val title: String, val context: String, val assumptions: String, val source: Reference,
    val first: Control, val second: Control? = null
) {
    Slit("Dupla fenda", "Veja como as franjas mudam com a geometria e a cor da luz.",
        "Usamos difração de campo distante, luz monocromática coerente, fendas idênticas com 12 µm de largura e sem informação de caminho. A linha mostra intensidade relativa ao centro.",
        Curriculum.feynman, Control("Comprimento de onda", 400f..700f, 550.0, " nm", decimalPlaces = 0),
        Control("Distância entre fendas", 30f..160f, 65.0, " µm", decimalPlaces = 0)),
    Interference("Interferência de ondas", "Mude a fase relativa e veja a soma de duas ondas.",
        "Somamos ondas senoidais com amplitude unitária e mesmo comprimento de onda em um instante. As duas curvas finas são as ondas individuais e a principal é a soma.",
        Curriculum.waves, Control("Fase relativa", 0f..360f, 90.0, "°", decimalPlaces = 0)),
    Probability("Distribuição de probabilidade", "Mude o centro e a largura de uma distribuição normal.",
        "Integre a densidade em um intervalo para obter uma probabilidade. O gráfico recorta o domínio de −4 a 4, enquanto a distribuição se estende à reta toda.",
        Curriculum.statistics, Control("Média", -2f..2f, 0.0), Control("Desvio padrão", .3f..1.6f, .8)),
    Wave("Função de onda", "Compare a amplitude real e a densidade em um poço.",
        "Corte temporal de um estado estacionário real de um poço 1D ideal. A curva roxa mostra ψ e a verde mostra |ψ|², cada uma em sua própria escala vertical.",
        Curriculum.mit, Control("Estado n", 1f..5f, 2.0, steps = 3, decimalPlaces = 0)),
    Tunneling("Tunelamento quântico", "Teste a transmissão de uma partícula contra uma barreira.",
        "Solução estacionária 1D de uma barreira retangular com potencial zero fora dela. E é a energia incidente e V₀ é a altura. A largura é expressa em unidades de ℏ/√(2mV₀). A curva usa 0,1 ≤ E/V₀ ≤ 0,9.",
        Curriculum.mit, Control("Razão E/V₀", 0.1f..0.9f, .5), Control("Largura adimensional", .5f..5f, 2.0)),
    Box("Partícula em uma caixa", "Compare a energia e a forma da densidade em um poço infinito.",
        "Modelo de elétron não relativístico em uma caixa unidimensional com paredes infinitas. O gráfico usa x/L no eixo horizontal e densidade relativa ao pico no eixo vertical.",
        Curriculum.mit, Control("Estado n", 1f..5f, 1.0, steps = 3, decimalPlaces = 0),
        Control("Comprimento da caixa", .5f..4f, 1.0, " nm")),
    Oscillator("Oscilador harmônico", "Observe a forma da densidade e os níveis de energia.",
        "Oscilador harmônico quântico unidimensional ideal em estado estacionário. A posição é dada em unidades de √(ℏ/mω). A janela mostra o intervalo de −4 a 4.",
        Curriculum.mit, Control("Estado n", 0f..3f, 0.0, steps = 2, decimalPlaces = 0)),
    Spin("Estados de spin", "Gire o eixo de medida e compare as probabilidades dos dois sinais.",
        "Uma partícula de spin 1/2 é preparada no estado puro positivo em z. O detector é ideal e o parâmetro é o ângulo entre a direção de preparo e o eixo de medida.",
        Curriculum.mitSpin, Control("Ângulo do detector", 0f..180f, 90.0, "°", decimalPlaces = 0)),
    Bloch("Qubits e esfera de Bloch", "Gire o estado e observe as chances de medir 0 ou 1.",
        "Projeção 2D da esfera de Bloch para um qubit puro. θ altera a população na base de z e φ altera a fase relativa. Estados mistos ficariam no interior.",
        Curriculum.mitSpin, Control("Ângulo polar θ", 0f..180f, 60.0, "°", decimalPlaces = 0),
        Control("Ângulo azimutal φ", 0f..360f, 45.0, "°", decimalPlaces = 0))
}

@Composable
internal fun LaboratoryScreen() {
    var selected by rememberSaveable { mutableStateOf(Experiment.Slit) }
    LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { ScreenHeading("Laboratório", "Ajuste os parâmetros e observe o que muda.") }
        item { Picker("Experimento", selected, Experiment.entries, { it.title }) { selected = it } }
        item(selected.name) { key(selected) { ExperimentPanel(selected) } }
        item {
            Panel {
                SectionTitle("Modelo e simplificações")
                Body(selected.assumptions)
                References(listOf(selected.source))
            }
        }
    }
}

@Composable
private fun ExperimentPanel(experiment: Experiment) {
    var first by rememberSaveable { mutableDoubleStateOf(experiment.first.initial) }
    var second by rememberSaveable { mutableDoubleStateOf(experiment.second?.initial ?: 0.0) }
    Panel {
        SectionTitle(experiment.title)
        Body(experiment.context)
        val theta = first * PI / 180
        val phi = second * PI / 180
        val spin = Laboratory.positiveSpinProbability(theta)
        val n = first.roundToInt()
        val metric = when (experiment) {
            Experiment.Slit -> "Separação de franjas em sen θ · ${decimal(first / (1000 * second), 4)}"
            Experiment.Interference -> "Amplitude da soma · ${decimal(abs(2 * cos(theta / 2)))}"
            Experiment.Probability -> "Pico da densidade · ${decimal(Laboratory.gaussian(first, first, second), 3)}"
            Experiment.Wave -> "Estado n = $n · ${n - 1} nós internos"
            Experiment.Tunneling -> "Probabilidade de transmissão · ${percent(Laboratory.transmission(first, second))}"
            Experiment.Box -> "Energia do elétron · ${decimal(Laboratory.wellEnergyEv(n, second), 3)} eV"
            Experiment.Oscillator -> "Energia · ${decimal(n + .5, 1)} ℏω"
            Experiment.Spin -> "P(+) = ${percent(spin)} · P(−) = ${percent(1 - spin)}"
            Experiment.Bloch -> "P(0) = ${percent(spin)} · P(1) = ${percent(1 - spin)}"
        }
        when (experiment) {
            Experiment.Bloch -> BlochPlot(theta, phi)
            Experiment.Spin -> {
                Body("Resultado +ℏ/2")
                ProgressLine(spin.toFloat(), Modifier.fillMaxWidth())
                Body("Resultado −ℏ/2")
                ProgressLine((1f - spin.toFloat()), Modifier.fillMaxWidth(), tint = MaterialTheme.colorScheme.tertiary)
            }
            else -> ExperimentPlot(experiment, first, second, metric)
        }
        Text(metric, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Adjust(experiment.first, first) { first = it }
        experiment.second?.let { Adjust(it, second) { value -> second = value } }
    }
}

@Composable
private fun Adjust(control: Control, value: Double, adjust: (Double) -> Unit) {
    val displayed = decimal(value, control.decimalPlaces) + control.suffix
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(control.label, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
            Text(displayed, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
        }
        Slider(value = value.toFloat(), onValueChange = {
            adjust(if (control.steps > 0) it.roundToInt().toDouble() else it.toDouble())
        }, modifier = Modifier.description("${control.label}, $displayed"), valueRange = control.range, steps = control.steps)
    }
}

@Composable
private fun ExperimentPlot(experiment: Experiment, a: Double, b: Double, metric: String) {
    val colors = MaterialTheme.colorScheme
    Canvas(Modifier.fillMaxWidth().height(183.dp).background(colors.surfaceVariant, RoundedCornerShape(12.dp))
        .description("Gráfico de ${experiment.title}. $metric")) {
        val axis = when (experiment) {
            Experiment.Interference -> .5f
            Experiment.Wave -> .63f
            else -> .84f
        }
        drawLine(colors.outline, Offset(.07f * size.width, axis * size.height),
            Offset(.93f * size.width, axis * size.height), 1.dp.toPx())
        val n = a.roundToInt()
        when (experiment) {
            Experiment.Slit -> curve(colors.primary, axis) { x -> .70 * Laboratory.doubleSlit(.054 * (x - .5), a, b) }
            Experiment.Interference -> {
                val phase = a * PI / 180
                curve(colors.outline, axis) { x -> .17 * Laboratory.interference(x * 2, 0.0) }
                curve(colors.tertiary, axis) { x -> .17 * Laboratory.interference(x * 2, phase) }
                curve(colors.primary, axis) { x ->
                    .17 * (Laboratory.interference(x * 2, 0.0) + Laboratory.interference(x * 2, phase))
                }
            }
            Experiment.Probability -> curve(colors.primary, axis) { x -> .51 * Laboratory.gaussian(x * 8 - 4, a, b) }
            Experiment.Wave -> {
                curve(colors.tertiary, axis) { x -> val wave = Laboratory.wellWave(x, n, 1.0); .22 * wave * wave }
                curve(colors.primary, axis) { x -> .21 * Laboratory.wellWave(x, n, 1.0) }
            }
            Experiment.Tunneling -> {
                curve(colors.primary, axis) { x -> .70 * Laboratory.transmission(.1 + .8 * x, b) }
                val x = .07f + .86f * ((a - .1) / .8).toFloat()
                val y = axis - .70f * Laboratory.transmission(a, b).toFloat()
                drawCircle(colors.tertiary, 5.dp.toPx(), Offset(size.width * x, size.height * y))
            }
            Experiment.Box -> curve(colors.primary, axis) { x ->
                val wave = Laboratory.wellWave(x, n, b)
                .70 * wave * wave * b / 2
            }
            Experiment.Oscillator -> curve(colors.primary, axis) { x -> Laboratory.oscillatorDensity(8 * x - 4, n) }
            else -> Unit
        }
    }
}

@Composable
private fun BlochPlot(theta: Double, phi: Double) {
    val colors = MaterialTheme.colorScheme
    val vector = Laboratory.bloch(theta, phi)
    val description = "Vetor na esfera de Bloch. x ${decimal(vector.x)}, y ${decimal(vector.y)}, z ${decimal(vector.z)}."
    Canvas(Modifier.fillMaxWidth().height(185.dp).background(colors.surfaceVariant, RoundedCornerShape(12.dp))
        .description(description)) {
        val radius = size.minDimension * .37f
        drawCircle(colors.outline, radius, center, style = Stroke(1.dp.toPx()))
        drawOval(colors.outline, center - Offset(radius, radius * .28f), Size(radius * 2, radius * .56f),
            style = Stroke(1.dp.toPx()))
        val horizontal = Offset(radius, 0f)
        val vertical = Offset(0f, radius)
        drawLine(colors.outline, center - horizontal, center + horizontal, 1.dp.toPx())
        drawLine(colors.outline, center - vertical, center + vertical, 1.dp.toPx())
        val y = (-.958 * vector.z + .287 * vector.y).toFloat()
        val tip = center + Offset(radius * vector.x.toFloat(), radius * y)
        drawLine(colors.primary, center, tip, 3.dp.toPx(), StrokeCap.Round)
        drawCircle(colors.primary, 5.dp.toPx(), tip)
        drawCircle(colors.tertiary, 2.dp.toPx(), center)
    }
}

private val pt = Locale.forLanguageTag("pt-BR")
private fun decimal(value: Double, places: Int = 2) = String.format(pt, "%.${places}f", value)
private fun percent(value: Double) = "${decimal(100 * value, 1)}%"
