package dev.quantumlearn.app

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.quantumlearn.core.Curriculum
import dev.quantumlearn.core.Diagram
import dev.quantumlearn.core.Lesson
import dev.quantumlearn.core.Mastery
import dev.quantumlearn.core.Progress
import dev.quantumlearn.core.Track

@Composable
internal fun LearnScreen(progress: Progress, dailyGoal: Int, moduleIndex: Int, setModule: (Int) -> Unit, open: (Lesson) -> Unit) {
    val modules = Curriculum.modules
    val module = modules[moduleIndex.coerceIn(modules.indices)]
    val track = module.track
    val trackModules = modules.withIndex().filter { it.value.track == track }
    val trackLessons = trackModules.flatMap { it.value.lessons }
    val next = trackLessons.firstOrNull { it.id !in progress.completions } ?: trackLessons.first()
    LazyColumn(contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 5.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item("stats") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Metric("${progress.xp} XP", Glyph.Bolt, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                Metric("${progress.streak} dias", Glyph.Flame, MaterialTheme.colorScheme.error, Modifier.weight(1f))
                Metric("${progress.xpToday}/$dailyGoal XP", Glyph.Target, MaterialTheme.colorScheme.tertiary, Modifier.weight(1.17f),
                    "${progress.xpToday} de $dailyGoal XP na meta diária")
            }
        }
        item("tracks") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Track.entries.forEach { option ->
                    FilterChip(selected = option == track,
                        onClick = { modules.indexOfFirst { it.track == option }.takeIf { it >= 0 }?.let(setModule) },
                        label = { Text(option.label) },
                        leadingIcon = {
                            Mark(if (option == Track.English) Glyph.Speech else Glyph.Atom,
                                if (option == track) MaterialTheme.colorScheme.onSecondaryContainer
                                else MaterialTheme.colorScheme.onSurfaceVariant, Modifier.size(18.dp))
                        })
                }
            }
        }
        item("hero") {
            val heroModule = trackModules.indexOfFirst { next in it.value.lessons } + 1
            Hero(next, heroModule, trackLessons.any { it.id in progress.completions }) { open(next) }
        }
        item("title") {
            Column(Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Eyebrow("TRILHA DE ESTUDOS")
                Text(module.title, style = MaterialTheme.typography.headlineMedium)
                val count = module.lessons.count { it.id in progress.completions }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    ProgressLine(count.toFloat() / module.lessons.size, Modifier.weight(1f))
                    Body("$count de ${module.lessons.size} lições")
                }
            }
        }
        item("modules") {
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                trackModules.forEachIndexed { position, (index, item) ->
                    FilterChip(selected = index == moduleIndex, onClick = { setModule(index) },
                        label = { Text("Módulo ${position + 1}") },
                        modifier = Modifier.padding(vertical = 1.dp).moduleDescription(item.title))
                }
            }
        }
        module.units.forEachIndexed { unitIndex, unit ->
            item("unit-$moduleIndex-$unitIndex") {
                Text(unit.title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp))
            }
            itemsIndexed(unit.lessons, key = { _, lesson -> lesson.id }) { index, lesson ->
                Box(Modifier.animateItem()) {
                    LessonRow(lesson, progress.mastery[lesson.id], lesson.id in progress.completions,
                        lesson.id == next.id, index != unit.lessons.lastIndex) { open(lesson) }
                }
            }
        }
    }
}

@Composable
private fun Metric(text: String, icon: Glyph, tint: Color, modifier: Modifier, description: String = text) {
    val colors = MaterialTheme.colorScheme
    Surface(modifier.moduleDescription(description), shape = RoundedCornerShape(12.dp), color = colors.surface,
        border = BorderStroke(1.dp, colors.outlineVariant)) {
        Row(Modifier.padding(horizontal = 8.dp, vertical = 9.dp), horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Mark(icon, tint, Modifier.size(16.dp))
            Text(text, style = MaterialTheme.typography.labelSmall, maxLines = 2)
        }
    }
}

@Composable
private fun Hero(lesson: Lesson, module: Int, hasProgress: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(22.dp)
    BoxWithConstraints(Modifier.fillMaxWidth().clip(shape)
        .background(Brush.linearGradient(listOf(Color(0xFF272043), Color(0xFF482678), Color(0xFF24173C)))),
        contentAlignment = Alignment.CenterStart) {
        val art = maxWidth >= 295.dp
        val float = rememberInfiniteTransition(label = "Mascote")
        val bob by float.animateFloat(-4f, 4f, infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
            label = "Flutuar")
        if (art) Mascot(Modifier.align(Alignment.CenterEnd).size(if (maxWidth > 350.dp) 150.dp else 121.dp)
            .graphicsLayer { translationY = bob.dp.toPx() })
        Column(Modifier.padding(start = 18.dp, end = if (art) 119.dp else 18.dp, top = 17.dp, bottom = 17.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Eyebrow("PRÓXIMA LIÇÃO · MÓDULO $module", PaleViolet)
            Text(lesson.title, color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Text(lesson.objective, color = HeroLight, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(1.dp))
            Surface(onClick, shape = RoundedCornerShape(10.dp), color = HeroLight, contentColor = Color(0xFF3F1B76)) {
                Row(Modifier.padding(horizontal = 11.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text(if (hasProgress) "Continuar" else "Começar lição", style = MaterialTheme.typography.labelLarge)
                    Mark(Glyph.Arrow, Color(0xFF3F1B76), Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun LessonRow(lesson: Lesson, mastery: Mastery?, completed: Boolean, next: Boolean, connected: Boolean, open: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val glyph = when (lesson.diagram) {
        Diagram.Groups -> Glyph.Plus
        Diagram.Parts -> Glyph.Fraction
        Diagram.Growth, Diagram.Square -> if (lesson.id == "roots") Glyph.Root else Glyph.Power
        Diagram.Balance -> Glyph.Balance
        Diagram.Scale, Diagram.Line -> Glyph.Scale
        Diagram.Vector -> Glyph.Arrow
        Diagram.Probability -> Glyph.Chart
        Diagram.Wave, Diagram.Interference -> Glyph.Wave
        Diagram.Photon, Diagram.Energy -> Glyph.Atom
        Diagram.Parabola, Diagram.Area, Diagram.Decay, Diagram.Modes, Diagram.Levels,
        Diagram.Threshold, Diagram.Bell, Diagram.Barrier, Diagram.Matrix -> Glyph.Chart
        Diagram.Circuit -> Glyph.Bolt
        Diagram.Sentence -> Glyph.Speech
    }
    val tint = when {
        completed -> colors.tertiary
        next -> colors.primary
        else -> colors.onSurfaceVariant
    }
    val fill = when {
        completed -> colors.tertiaryContainer
        next -> colors.primaryContainer
        else -> colors.surfaceVariant
    }
    val status = when {
        completed -> "Concluída · ${mastery?.percent ?: 0}% de domínio"
        next -> "Próxima lição"
        (mastery?.attempts ?: 0) > 0 -> "${mastery?.percent ?: 0}% de domínio"
        else -> "Lição e prática"
    }
    val shape = RoundedCornerShape(17.dp)
    Column {
        Surface(Modifier.fillMaxWidth().clip(shape).clickable(onClick = open), shape = shape,
            color = colors.surface, border = BorderStroke(1.dp, if (next) colors.primary.copy(alpha = .58f) else colors.outlineVariant)) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(11.dp)) {
                IconTile(if (completed) Glyph.Check else glyph, tint, fill)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(lesson.title, style = MaterialTheme.typography.titleMedium)
                    Text(status, color = tint, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                }
                Mark(Glyph.Arrow, tint, Modifier.size(18.dp))
            }
        }
        if (connected) Box(Modifier.padding(start = 32.dp).width(2.dp).height(7.dp).background(colors.outlineVariant))
    }
}

internal fun Modifier.moduleDescription(description: String): Modifier =
    this.then(Modifier.description(description))
