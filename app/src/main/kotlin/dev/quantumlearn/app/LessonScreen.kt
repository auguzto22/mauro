package dev.quantumlearn.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.quantumlearn.core.Formula
import dev.quantumlearn.core.FormulaDrills
import dev.quantumlearn.core.Lesson
import dev.quantumlearn.core.Reference
import dev.quantumlearn.core.TutorAction

internal fun Modifier.description(description: String): Modifier = semantics { contentDescription = description }

@Composable
internal fun DetailHeader(text: String, onBack: () -> Unit, backLabel: String = "Voltar", trailing: @Composable () -> Unit = {}) {
    val colors = MaterialTheme.colorScheme
    Column(Modifier.background(colors.background).statusBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(start = 7.dp, end = 14.dp, top = 6.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onBack, modifier = Modifier.description(backLabel)) {
                Mark(Glyph.Back, colors.onSurface)
            }
            Text(text, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
            trailing()
        }
        HorizontalDivider(color = colors.outlineVariant)
    }
}

@Composable
internal fun LessonScreen(
    lesson: Lesson, onBack: () -> Unit, begin: () -> Unit, drill: () -> Unit, tutor: (TutorAction) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val guide = remember(lesson.id) { FormulaDrills.guide(lesson.id) }
    Column(Modifier.fillMaxSize().navigationBarsPadding()) {
        DetailHeader("Lição", onBack)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)) {
            Reveal(0) {
                Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Eyebrow("OBJETIVO DA LIÇÃO")
                    Text(lesson.title, style = MaterialTheme.typography.headlineLarge)
                    Body(lesson.objective)
                }
            }
            Reveal(1) {
                Panel { SectionTitle("Entenda a ideia"); Body(lesson.introduction) }
            }
            Reveal(2) {
                Panel {
                    SectionTitle("Exemplo")
                    Body(lesson.example)
                    LessonDiagram(lesson)
                    Body(lesson.diagramCaption)
                }
            }
            if (guide != null) {
                Reveal(3) { FormulaAnatomy(guide) }
                Reveal(4) { FormulaWalkthrough(guide) }
                if (guide.language) SpeakPractice(guide.phrases)
            }
            Reveal(5) {
                Panel(Modifier.animateContentSize()) {
                    SectionTitle("Aprofundamento")
                    Equation(lesson.equation)
                    OutlinedButton(onClick = { expanded = !expanded }) {
                        Text(if (expanded) "Ocultar detalhes" else "Ver explicação detalhada")
                    }
                    AnimatedVisibility(expanded) { Body(lesson.detail) }
                }
            }
            if (guide != null) RealWorld(guide, drill)
            Panel {
                SectionTitle("O que você aprendeu")
                Body(lesson.summary)
                Body(lesson.application)
                TextButton(onClick = { tutor(TutorAction.Simple) }) { Text("Pedir ajuda ao professor") }
            }
            References(lesson.references)
        }
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                if (guide != null) {
                    OutlinedButton(onClick = drill, modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp)) {
                        Text(if (guide.language) "Treinar estrutura" else "Treinar fórmula", maxLines = 1)
                    }
                }
                PrimaryButton("Praticar e testar", begin, Modifier.weight(1f))
            }
        }
    }
}

@Composable
internal fun References(references: List<Reference>) {
    val handler = LocalUriHandler.current
    var error by remember { mutableStateOf(false) }
    if (references.isEmpty()) return
    Column {
        Eyebrow("REFERÊNCIAS")
        references.distinctBy(Reference::url).forEach { source ->
            TextButton(onClick = { error = runCatching { handler.openUri(source.url) }.isFailure },
                contentPadding = PaddingValues(start = 0.dp, end = 8.dp)) { Text(source.name) }
        }
        if (error) Body("Um navegador é necessário para abrir esta referência.")
    }
}

internal sealed interface MathPart {
    data class Label(val text: String) : MathPart
    data class Fraction(val numerator: List<MathPart>, val denominator: List<MathPart>) : MathPart
    data class Radical(val value: List<MathPart>) : MathPart
    data class Script(val value: List<MathPart>, val upper: Boolean) : MathPart
}

internal class FormulaParser(private val latex: String) {
    private var at = 0
    private val symbols = mapOf(
        "times" to " × ", "cdot" to " · ", "quad" to "   ", "pm" to " ± ", "Rightarrow" to " ⇒ ",
        "lambda" to "λ", "psi" to "ψ", "rho" to "ρ", "hbar" to "ℏ", "rangle" to "⟩", "propto" to " ∝ ",
        "Delta" to "Δ", "theta" to "θ", "phi" to "φ", "varphi" to "φ", "kappa" to "κ",
        "omega" to "ω", "pi" to "π", "partial" to "∂", "langle" to "⟨", "approx" to " ≈ ",
        "ge" to " ≥ ", "to" to " → ", "int" to "∫", "lim" to "lim", "cos" to "cos", "sin" to "sin", "log" to "log",
        "le" to " ≤ ", "Leftrightarrow" to " ⇔ ", "cdots" to "…", "ldots" to "…", "sum" to "Σ", "uparrow" to "↑",
        "downarrow" to "↓", "infty" to "∞", "neq" to " ≠ ", "mu" to "μ", "sigma" to "σ", "alpha" to "α", "ln" to "ln",
        "exp" to "exp", "left" to "", "right" to "", "Omega" to "Ω", "nu" to "ν", "div" to " ÷ ", "sim" to " ~ "
    )

    fun read(group: Boolean = false): List<MathPart> = buildList {
        val text = StringBuilder()
        fun flush() {
            if (text.isNotEmpty()) {
                add(MathPart.Label(text.toString()))
                text.clear()
            }
        }
        while (at < latex.length) {
            when (val c = latex[at++]) {
                '}' -> if (group) break else text.append(c)
                '{' -> {
                    flush()
                    addAll(read(true))
                }
                '^', '_' -> {
                    flush()
                    add(MathPart.Script(argument(), c == '^'))
                }
                '\\' -> {
                    val start = at
                    while (at < latex.length && latex[at].isLetter()) at++
                    val name = latex.substring(start, at)
                    when (name) {
                        "" -> {
                            val punctuation = latex.getOrNull(at++)
                            text.append(if (punctuation == ',') ' ' else punctuation ?: ' ')
                        }
                        "mathrm" -> {
                            flush()
                            addAll(argument())
                        }
                        "frac" -> {
                            flush()
                            add(MathPart.Fraction(argument(), argument()))
                        }
                        "sqrt" -> {
                            flush()
                            add(MathPart.Radical(argument()))
                        }
                        else -> text.append(symbols[name] ?: name)
                    }
                }
                '=' -> text.append(" = ")
                '-' -> text.append('−')
                else -> text.append(c)
            }
        }
        flush()
    }

    private fun argument(): List<MathPart> {
        val character = latex.getOrNull(at++) ?: return emptyList()
        return if (character == '{') read(true) else listOf(MathPart.Label(character.toString()))
    }
}

@Composable
internal fun Equation(formula: Formula) {
    val parts = remember(formula.latex) { FormulaParser(formula.latex).read() }
    val colors = MaterialTheme.colorScheme
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(13.dp), color = colors.primaryContainer,
        contentColor = colors.onPrimaryContainer, border = BorderStroke(1.dp, colors.outlineVariant)) {
        Column(Modifier.padding(11.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).clearAndSetSemantics {
                contentDescription = "Equação em LaTeX. " + formula.latex
            }) { FormulaParts(parts, 21) }
            Text(formula.symbols, style = MaterialTheme.typography.bodySmall)
        }
    }
}

/** Linha de matemática sem moldura, usada no passo a passo das fórmulas. */
@Composable
internal fun MathLine(latex: String, modifier: Modifier = Modifier, font: Int = 20, color: Color = Color.Unspecified) {
    val parts = remember(latex) { FormulaParser(latex).read() }
    val tint = if (color == Color.Unspecified) MaterialTheme.colorScheme.onSurface else color
    CompositionLocalProvider(LocalContentColor provides tint) {
        Row(modifier.horizontalScroll(rememberScrollState()).clearAndSetSemantics {
            contentDescription = "Equação. " + latex
        }) { FormulaParts(parts, font, tint) }
    }
}

@Composable
private fun FormulaParts(parts: List<MathPart>, font: Int, rule: Color = MaterialTheme.colorScheme.onPrimaryContainer) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (part in parts) {
            when (part) {
                is MathPart.Label -> Text(part.text, fontSize = font.sp, lineHeight = (font + 3).sp, softWrap = false)
                is MathPart.Fraction -> Column(Modifier.padding(horizontal = 4.dp).width(IntrinsicSize.Max),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    FormulaParts(part.numerator, font - 4, rule)
                    HorizontalDivider(Modifier.fillMaxWidth(), color = rule)
                    FormulaParts(part.denominator, font - 4, rule)
                }
                is MathPart.Radical -> {
                    Text("√", fontSize = (font + 2).sp, softWrap = false)
                    Column(Modifier.width(IntrinsicSize.Max)) {
                        HorizontalDivider(Modifier.fillMaxWidth(), color = rule)
                        FormulaParts(part.value, font, rule)
                    }
                }
                is MathPart.Script -> Column(Modifier.height((font + 5).dp),
                    verticalArrangement = if (part.upper) Arrangement.Top else Arrangement.Bottom) {
                    FormulaParts(part.value, font - 7, rule)
                    Spacer(Modifier.size(1.dp))
                }
            }
        }
    }
}
