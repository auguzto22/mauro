package dev.quantumlearn.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.quantumlearn.core.FormulaGuide
import dev.quantumlearn.core.RealCase
import dev.quantumlearn.core.Scene

/** Faz o conteúdo surgir deslizando de baixo, com um atraso para criar uma cascata entre seções. */
@Composable
internal fun Reveal(order: Int, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val state = remember { MutableTransitionState(false).apply { targetState = true } }
    Reveal(order, state, modifier, content)
}

/** Versão com estado externo, para listas preguiçosas: a animação toca uma vez só. */
@Composable
internal fun Reveal(order: Int, state: MutableTransitionState<Boolean>, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    AnimatedVisibility(state, modifier,
        enter = fadeIn(tween(420, delayMillis = 70 * order)) +
            slideInVertically(tween(480, delayMillis = 70 * order, easing = FastOutSlowInEasing)) { it / 5 }) {
        content()
    }
}

/** Fórmula grande com cada símbolo tocável. Tocar mostra o papel e a unidade do símbolo. */
@Composable
internal fun FormulaAnatomy(guide: FormulaGuide) {
    val colors = MaterialTheme.colorScheme
    var selected by rememberSaveable(guide.lessonId) { mutableIntStateOf(0) }
    val glow = rememberInfiniteTransition(label = "Brilho da fórmula")
    val shimmer by glow.animateFloat(0f, 1f, infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Reverse),
        label = "Brilho")
    Panel {
        SectionTitle("Anatomia da fórmula")
        Body("Toque em cada símbolo para descobrir o que ele significa.")
        Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Brush.linearGradient(
            listOf(colors.primaryContainer, colors.primaryContainer.copy(alpha = .55f + .25f * shimmer), colors.tertiaryContainer.copy(alpha = .5f))
        )).padding(vertical = 18.dp, horizontal = 14.dp), contentAlignment = Alignment.Center) {
            MathLine(guide.latex, font = 26, color = colors.onPrimaryContainer)
        }
        Text("“${guide.reading}”", style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant,
            fontWeight = FontWeight.Medium)
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            guide.symbols.forEachIndexed { index, symbol ->
                SymbolChip(symbol.symbol, index == selected) { selected = index }
            }
        }
        AnimatedContent(selected.coerceIn(guide.symbols.indices), label = "Símbolo",
            transitionSpec = {
                val direction = if (targetState > initialState) 1 else -1
                (slideInHorizontally(tween(320)) { direction * it / 3 } + fadeIn(tween(320))) togetherWith
                    (slideOutHorizontally(tween(260)) { -direction * it / 3 } + fadeOut(tween(200)))
            }) { index ->
            val symbol = guide.symbols[index]
            Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = colors.surfaceVariant) {
                Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.height(52.dp).widthIn(min = 52.dp).clip(RoundedCornerShape(14.dp)).background(colors.primary)
                        .padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
                        Text(symbol.symbol, color = colors.onPrimary, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(symbol.meaning.replaceFirstChar(Char::uppercase), style = MaterialTheme.typography.titleMedium)
                        if (symbol.unit.isNotBlank()) Text("Unidade: ${symbol.unit}", style = MaterialTheme.typography.bodySmall,
                            color = colors.primary)
                    }
                }
            }
        }
    }
}

@Composable
private fun SymbolChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val background by animateColorAsState(if (selected) colors.primary else colors.surfaceVariant, tween(260), label = "Fundo do símbolo")
    val content by animateColorAsState(if (selected) colors.onPrimary else colors.onSurface, tween(260), label = "Texto do símbolo")
    val scale by animateFloatAsState(if (selected) 1.08f else 1f, spring(Spring.DampingRatioMediumBouncy), label = "Escala do símbolo")
    Box(Modifier.scale(scale).widthIn(min = 46.dp).clip(RoundedCornerShape(12.dp)).background(background)
        .clickable(role = Role.Tab, onClickLabel = "Explicar $label", onClick = onClick)
        .padding(horizontal = 14.dp, vertical = 9.dp), contentAlignment = Alignment.Center) {
        Text(label, color = content, style = MaterialTheme.typography.titleMedium, maxLines = 1)
    }
}

/** Mostra a fórmula sendo construída etapa por etapa, como no quadro. */
@Composable
internal fun FormulaWalkthrough(guide: FormulaGuide) {
    val colors = MaterialTheme.colorScheme
    var current by rememberSaveable(guide.lessonId) { mutableIntStateOf(0) }
    var showAll by rememberSaveable(guide.lessonId) { mutableStateOf(false) }
    val steps = guide.build
    Panel(Modifier.animateContentSize(spring(stiffness = Spring.StiffnessMediumLow))) {
        SectionTitle("Como a fórmula funciona")
        Body("Siga cada etapa no seu ritmo, como se estivesse no quadro.")
        StepDots(steps.size, current, showAll) { current = it; showAll = false }
        if (showAll) {
            steps.forEachIndexed { index, step -> StepCard(index, step.latex, step.text, true) }
        } else {
            AnimatedContent(current, label = "Etapa",
                transitionSpec = {
                    val forward = targetState > initialState
                    (slideInHorizontally(tween(360, easing = FastOutSlowInEasing)) { if (forward) it else -it } + fadeIn(tween(300))) togetherWith
                        (slideOutHorizontally(tween(300)) { if (forward) -it / 2 else it / 2 } + fadeOut(tween(220)))
                }) { index ->
                val step = steps[index]
                StepCard(index, step.latex, step.text, false)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { current-- }, enabled = current > 0) { Text("Voltar") }
                Box(Modifier.weight(1f))
                if (current < steps.lastIndex) {
                    OutlinedButton(onClick = { current++ }) { Text("Próxima etapa") }
                } else {
                    OutlinedButton(onClick = { showAll = true }) { Text("Ver todas juntas") }
                }
            }
        }
        if (showAll) TextButton(onClick = { showAll = false; current = 0 }) { Text("Rever etapa por etapa") }
    }
}

@Composable
private fun StepDots(count: Int, current: Int, all: Boolean, select: (Int) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        repeat(count) { index ->
            val active = all || index <= current
            val width by animateDpAsState(if (!all && index == current) 26.dp else 10.dp, spring(Spring.DampingRatioMediumBouncy),
                label = "Indicador")
            val tint by animateColorAsState(if (active) colors.primary else colors.outlineVariant, tween(250), label = "Cor do indicador")
            Box(Modifier.height(10.dp).width(width).clip(CircleShape).background(tint)
                .clickable(onClickLabel = "Ir para a etapa ${index + 1}") { select(index) })
        }
        Text(if (all) "Todas as etapas" else "Etapa ${current + 1} de $count", Modifier.padding(start = 6.dp),
            style = MaterialTheme.typography.labelSmall, color = colors.onSurfaceVariant)
    }
}

@Composable
private fun StepCard(index: Int, latex: String, text: String, compact: Boolean) {
    val colors = MaterialTheme.colorScheme
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = colors.surfaceVariant,
        border = BorderStroke(1.dp, colors.outlineVariant)) {
        Column(Modifier.padding(if (compact) 12.dp else 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(Modifier.size(26.dp).clip(CircleShape).background(colors.primary), contentAlignment = Alignment.Center) {
                    Text("${index + 1}", color = colors.onPrimary, style = MaterialTheme.typography.labelLarge)
                }
                MathLine(latex, Modifier.weight(1f), font = if (compact) 18 else 22, color = colors.onSurface)
            }
            Text(text, style = MaterialTheme.typography.bodyMedium, color = colors.onSurfaceVariant)
        }
    }
}

/** Cartões com lugares onde a fórmula aparece de verdade. */
@Composable
internal fun RealWorld(guide: FormulaGuide, practice: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(Modifier.semantics { heading() }, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Eyebrow("NO MUNDO REAL")
            SectionTitle("Onde isso acontece de verdade")
        }
        guide.cases.forEachIndexed { index, case -> Reveal(index) { CaseCard(case) } }
        OutlinedButton(onClick = practice, modifier = Modifier.fillMaxWidth()) {
            Text("Resolver os casos reais no treino")
        }
    }
}

@Composable
private fun CaseCard(case: RealCase) {
    val colors = MaterialTheme.colorScheme
    var open by rememberSaveable(case.place) { mutableStateOf(false) }
    val (glyph, tint) = sceneStyle(case.scene)
    val turn by animateFloatAsState(if (open) 90f else 0f, tween(260), label = "Seta")
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = colors.surface,
        border = BorderStroke(1.dp, if (open) tint.copy(alpha = .6f) else colors.outlineVariant)) {
        Column(Modifier.clickable(onClickLabel = if (open) "Esconder a conta" else "Ver a conta") { open = !open }
            .padding(16.dp).animateContentSize(), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconTile(glyph, tint, tint.copy(alpha = .16f))
                Column(Modifier.weight(1f)) {
                    Eyebrow(case.scene.label.uppercase(), tint)
                    Text(case.place, style = MaterialTheme.typography.titleMedium)
                }
                Mark(Glyph.Arrow, colors.onSurfaceVariant, Modifier.size(18.dp).graphicsLayer { rotationZ = turn })
            }
            Body(case.story)
            AnimatedVisibility(open, enter = expandVertically(tween(300)) + fadeIn(tween(300)),
                exit = shrinkVertically(tween(220)) + fadeOut(tween(160))) {
                Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = tint.copy(alpha = .12f)) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Eyebrow("A CONTA", tint)
                        Text(case.math, style = MaterialTheme.typography.titleMedium)
                        if (case.ask != null) Text("Há uma questão sobre este caso no treino da fórmula.",
                            style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
                    }
                }
            }
            if (!open) Text("Toque para ver a conta", style = MaterialTheme.typography.labelSmall, color = tint)
        }
    }
}

@Composable
private fun sceneStyle(scene: Scene): Pair<Glyph, Color> {
    val colors = MaterialTheme.colorScheme
    return when (scene) {
        Scene.Home -> Glyph.Balance to Color(0xFFF59E0B)
        Scene.Phone -> Glyph.Bolt to Color(0xFF38BDF8)
        Scene.Health -> Glyph.Plus to Color(0xFFF43F5E)
        Scene.Space -> Glyph.Orbit to Color(0xFF818CF8)
        Scene.Transport -> Glyph.Arrow to Color(0xFF22C55E)
        Scene.Money -> Glyph.Chart to Color(0xFF10B981)
        Scene.Sport -> Glyph.Medal to Color(0xFFFB923C)
        Scene.Light -> Glyph.Spark to Color(0xFFEAB308)
        Scene.Nature -> Glyph.Wave to Color(0xFF14B8A6)
        Scene.Lab -> Glyph.Flask to colors.primary
        Scene.Computer -> Glyph.Atom to Color(0xFFA78BFA)
        Scene.Music -> Glyph.Wave to Color(0xFFEC4899)
    }
}

/** Selo animado que aparece em cima das questões de treino da fórmula. */
@Composable
internal fun FormulaBadge(latex: String) {
    val colors = MaterialTheme.colorScheme
    val state = remember(latex) { MutableTransitionState(false).apply { targetState = true } }
    AnimatedVisibility(state, enter = scaleIn(spring(Spring.DampingRatioMediumBouncy), initialScale = .85f) + fadeIn()) {
        Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = colors.primaryContainer,
            contentColor = colors.onPrimaryContainer) {
            Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Eyebrow("TREINO DA FÓRMULA", colors.onPrimaryContainer.copy(alpha = .75f))
                MathLine(latex, font = 20, color = colors.onPrimaryContainer)
            }
        }
    }
}
