package dev.quantumlearn.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.quantumlearn.data.ThemeMode

internal val Violet = Color(0xFF8B5CF6)
internal val PaleViolet = Color(0xFFD8C8FF)
internal val HeroLight = Color(0xFFF3ECFF)

private val dark = darkColorScheme(
    primary = Color(0xFFBCA5FF), onPrimary = Color(0xFF231044),
    primaryContainer = Color(0xFF2B1B48), onPrimaryContainer = PaleViolet,
    background = Color(0xFF09090B), onBackground = Color.White,
    surface = Color(0xFF18181B), onSurface = Color(0xFFF7F5FA),
    surfaceContainer = Color(0xFF19171E), surfaceVariant = Color(0xFF23212A),
    onSurfaceVariant = Color(0xFFB7B3C1), outline = Color(0xFF777080),
    outlineVariant = Color(0xFF302D37), error = Color(0xFFFF8A87),
    errorContainer = Color(0xFF3C1D24), onErrorContainer = Color(0xFFFFC0BB),
    tertiary = Color(0xFF4ADE80), tertiaryContainer = Color(0xFF142D23),
    onTertiaryContainer = Color(0xFF9AEEB5)
)
private val light = lightColorScheme(
    primary = Color(0xFF6134C2), onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE3FF), onPrimaryContainer = Color(0xFF34176A),
    background = Color(0xFFF8F6FC), onBackground = Color(0xFF191323),
    surface = Color.White, onSurface = Color(0xFF191323),
    surfaceContainer = Color(0xFFF4EFFA), surfaceVariant = Color(0xFFF0EBF6),
    onSurfaceVariant = Color(0xFF554C62), outline = Color(0xFF827591),
    outlineVariant = Color(0xFFE4DAEE), error = Color(0xFFB32329),
    errorContainer = Color(0xFFFFE6E5), onErrorContainer = Color(0xFF831921),
    tertiary = Color(0xFF147443), tertiaryContainer = Color(0xFFDBF8E6),
    onTertiaryContainer = Color(0xFF135934)
)

private val typography = Typography(
    headlineLarge = TextStyle(fontSize = 32.sp, lineHeight = 37.sp, letterSpacing = (-1.35).sp, fontWeight = FontWeight.Bold),
    headlineMedium = TextStyle(fontSize = 26.sp, lineHeight = 31.sp, letterSpacing = (-0.7).sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 21.sp, lineHeight = 27.sp, letterSpacing = (-0.35).sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 17.sp, lineHeight = 23.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 18.sp),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
    labelSmall = TextStyle(fontSize = 11.sp, lineHeight = 15.sp, letterSpacing = .35.sp, fontWeight = FontWeight.SemiBold)
)

@Composable
internal fun QuantumTheme(mode: ThemeMode, content: @Composable (Boolean) -> Unit) {
    val darkMode = when (mode) {
        ThemeMode.System -> isSystemInDarkTheme()
        ThemeMode.Dark -> true
        ThemeMode.Light -> false
    }
    MaterialTheme(colorScheme = if (darkMode) dark else light, typography = typography) { content(darkMode) }
}

@Composable
internal fun Panel(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(modifier.fillMaxWidth(), color = color, shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
internal fun Eyebrow(text: String, color: Color = MaterialTheme.colorScheme.primary) {
    Text(text, style = MaterialTheme.typography.labelSmall, letterSpacing = 1.4.sp, color = color)
}

@Composable
internal fun Body(text: String) {
    Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
internal fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleLarge)
}

@Composable
internal fun PrimaryButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF7542DE), contentColor = Color.White,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Button(onClick, modifier.height(52.dp), enabled = enabled, colors = colors, shape = RoundedCornerShape(14.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
internal fun ProgressLine(
    value: Float, modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary, track: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val progress = value.coerceIn(0f, 1f)
    val animated by animateFloatAsState(progress, animationSpec = tween(280), label = "Progresso")
    Box(modifier.height(6.dp).clip(RoundedCornerShape(8.dp)).background(track)
        .semantics { progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f) }) {
        if (animated > 0f) Box(Modifier.fillMaxWidth(animated).height(6.dp).background(tint, RoundedCornerShape(8.dp)))
    }
}

internal enum class Glyph {
    Atom, Book, Target, Flask, Spark, Person, Bolt, Flame, Arrow, Back, Check, Plus,
    Fraction, Power, Root, Balance, Scale, Wave, Orbit, Chart, Moon, Clock, Medal, Speech
}

@Composable
internal fun Mark(glyph: Glyph, color: Color, modifier: Modifier = Modifier.size(23.dp)) {
    Canvas(modifier) {
        val step = size.minDimension / 24f
        fun p(x: Float, y: Float) = Offset(x * step, y * step)
        fun line(vararg points: Pair<Int, Int>) {
            points.toList().zipWithNext().forEach { (start, end) ->
                drawLine(color, p(start.first.toFloat(), start.second.toFloat()), p(end.first.toFloat(), end.second.toFloat()),
                    1.65f * step, StrokeCap.Round)
            }
        }
        fun ring(radius: Float, x: Float = 12f, y: Float = 12f) =
            drawCircle(color, radius * step, p(x, y), style = Stroke(1.55f * step))
        fun dot(radius: Float, x: Float, y: Float) = drawCircle(color, radius * step, p(x, y))
        fun box(x: Float, y: Float, width: Float, height: Float, radius: Float) =
            drawRoundRect(color, p(x, y), Size(width * step, height * step), CornerRadius(radius * step),
                style = Stroke(1.55f * step))
        when (glyph) {
            Glyph.Atom -> {
                for (angle in listOf(0f, 60f, 120f)) rotate(angle, p(12f, 12f)) {
                    drawOval(color, p(2f, 8f), Size(20f * step, 8f * step), style = Stroke(1.3f * step))
                }
                dot(1.75f, 12f, 12f)
            }
            Glyph.Book -> {
                line(12 to 6, 9 to 4, 3 to 4, 3 to 18, 9 to 18, 12 to 20, 15 to 18, 21 to 18, 21 to 4, 15 to 4, 12 to 6, 12 to 20)
            }
            Glyph.Target -> { ring(9f); ring(5.2f); dot(1.6f, 12f, 12f) }
            Glyph.Flask -> {
                line(8 to 3, 16 to 3); line(9 to 3, 9 to 10, 4 to 19, 6 to 21, 18 to 21, 20 to 19, 15 to 10, 15 to 3)
                line(7 to 15, 17 to 15); dot(1f, 12f, 17.5f)
            }
            Glyph.Spark -> {
                line(12 to 2, 14 to 9, 21 to 12, 14 to 14, 12 to 21, 9 to 14, 2 to 12, 9 to 9, 12 to 2)
                line(20 to 3, 20 to 7); line(18 to 5, 22 to 5)
            }
            Glyph.Person -> {
                ring(3.7f, 12f, 7.5f)
                drawArc(color, 180f, 180f, false, p(4f, 14f), Size(16 * step, 12 * step), style = Stroke(1.65f * step))
            }
            Glyph.Bolt -> line(13 to 2, 5 to 13, 11 to 13, 10 to 22, 19 to 10, 13 to 10, 13 to 2)
            Glyph.Flame -> {
                val path = Path().apply {
                    moveTo(12f * step, 2f * step)
                    cubicTo(15f * step, 7f * step, 21f * step, 9f * step, 20f * step, 16f * step)
                    cubicTo(19f * step, 24f * step, 4f * step, 24f * step, 4f * step, 15f * step)
                    cubicTo(4f * step, 11f * step, 7f * step, 9f * step, 8f * step, 7f * step)
                    cubicTo(8f * step, 11f * step, 12f * step, 9f * step, 12f * step, 2f * step)
                }
                drawPath(path, color, style = Stroke(1.65f * step))
            }
            Glyph.Arrow -> { line(4 to 12, 20 to 12); line(13 to 5, 20 to 12, 13 to 19) }
            Glyph.Back -> line(15 to 5, 8 to 12, 15 to 19)
            Glyph.Check -> line(5 to 12, 10 to 17, 20 to 7)
            Glyph.Plus -> { line(12 to 6, 12 to 18); line(6 to 12, 18 to 12) }
            Glyph.Fraction -> { line(6 to 19, 18 to 5); ring(2f, 7f, 7f); ring(2f, 17f, 17f) }
            Glyph.Power -> { line(4 to 17, 13 to 8); line(4 to 8, 13 to 17); line(15 to 5, 19 to 5, 19 to 8, 15 to 11, 20 to 11) }
            Glyph.Root -> line(3 to 13, 6 to 13, 9 to 19, 12 to 6, 21 to 6)
            Glyph.Balance -> { line(4 to 9, 20 to 9); line(12 to 5, 12 to 20); line(7 to 20, 17 to 20); line(4 to 9, 2 to 15, 7 to 15, 5 to 9); line(19 to 9, 17 to 15, 22 to 15, 20 to 9) }
            Glyph.Scale -> { line(4 to 8, 4 to 19, 21 to 19); line(6 to 17, 11 to 14, 15 to 10, 20 to 4) }
            Glyph.Wave -> {
                val curve = Path().apply {
                    moveTo(2 * step, 12 * step)
                    cubicTo(7 * step, -4 * step, 10 * step, 29 * step, 14 * step, 12 * step)
                    cubicTo(17 * step, 0f, 20 * step, 8 * step, 22 * step, 12 * step)
                }
                drawPath(curve, color, style = Stroke(1.65f * step, cap = StrokeCap.Round))
            }
            Glyph.Orbit -> { ring(8.5f); dot(2f, 17.5f, 6f); dot(2f, 12f, 12f) }
            Glyph.Chart -> { line(4 to 4, 4 to 20, 21 to 20); line(8 to 16, 8 to 13); line(12 to 16, 12 to 9); line(16 to 16, 16 to 11); line(20 to 16, 20 to 6) }
            Glyph.Moon -> {
                val moon = Path().apply {
                    moveTo(13f * step, 3f * step)
                    cubicTo(1f * step, 2f * step, 0f, 22f * step, 14f * step, 21f * step)
                    cubicTo(18f * step, 21f * step, 21f * step, 18f * step, 22f * step, 15f * step)
                    cubicTo(12f * step, 18f * step, 8f * step, 10f * step, 13f * step, 3f * step)
                }
                drawPath(moon, color, style = Stroke(1.6f * step))
            }
            Glyph.Clock -> { ring(9f); line(12 to 6, 12 to 12, 16 to 14) }
            Glyph.Medal -> { ring(5f, 12f, 15f); line(5 to 3, 10 to 11); line(19 to 3, 14 to 11); dot(1.5f, 12f, 15f) }
            Glyph.Speech -> {
                box(3f, 4f, 18f, 12f, 4f)
                line(8 to 16, 7 to 21, 13 to 16)
                dot(1.2f, 8f, 10f); dot(1.2f, 12f, 10f); dot(1.2f, 16f, 10f)
            }
        }
    }
}

@Composable
internal fun IconTile(glyph: Glyph, color: Color, background: Color, modifier: Modifier = Modifier) {
    Box(modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(background), contentAlignment = Alignment.Center) {
        Mark(glyph, color, Modifier.size(21.dp))
    }
}

@Composable
internal fun Mascot(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val scale = size.minDimension / 150f
        val origin = Offset((size.width - 150 * scale) / 2, (size.height - 150 * scale) / 2)
        fun point(x: Float, y: Float) = origin + Offset(x * scale, y * scale)
        fun softly(x: Float, y: Float, width: Float, height: Float, radius: Float, color: Color) =
            drawRoundRect(color, point(x, y), Size(width * scale, height * scale), CornerRadius(radius * scale))
        fun outline(x: Float, y: Float, width: Float, height: Float, radius: Float, color: Color) =
            drawRoundRect(color, point(x, y), Size(width * scale, height * scale), CornerRadius(radius * scale),
                style = Stroke(1.3f * scale))
        val glow = Color(0xFFAB77FF)
        drawCircle(Brush.radialGradient(listOf(glow.copy(alpha = .58f), glow.copy(alpha = 0f)), point(79f, 77f), 71 * scale),
            71 * scale, point(79f, 77f))
        for (angle in listOf(-31f, 31f)) rotate(angle, point(78f, 75f)) {
            drawOval(PaleViolet.copy(alpha = .59f), point(17f, 57f), Size(122 * scale, 38 * scale),
                style = Stroke(1.1f * scale))
        }
        drawOval(Color(0x330D041D), point(48f, 117f), Size(65 * scale, 12 * scale))
        softly(63f, 92f, 31f, 25f, 12f, Color(0xFFA275F5))
        outline(63f, 92f, 31f, 25f, 12f, PaleViolet)
        drawLine(PaleViolet, point(78f, 49f), point(78f, 36f), 2.5f * scale, StrokeCap.Round)
        drawCircle(Color(0xFFCFADFF), 4.6f * scale, point(78f, 33f))
        softly(37f, 63f, 17f, 24f, 8f, Color(0xFF8351D4))
        softly(104f, 63f, 17f, 24f, 8f, Color(0xFF8351D4))
        drawRoundRect(Brush.linearGradient(listOf(Color(0xFFFBF2FF), Color(0xFF9861F5)),
            start = point(45f, 51f), end = point(111f, 103f)),
            point(46f, 48f), Size(66 * scale, 55 * scale), CornerRadius(23 * scale))
        outline(46f, 48f, 66f, 55f, 23f, Color(0xFFF0DFFF))
        softly(53f, 57f, 52f, 35f, 14f, Color(0xFF231237))
        softly(64f, 66f, 8f, 12f, 4f, Color(0xFFCEB3FF))
        softly(86f, 66f, 8f, 12f, 4f, Color(0xFFCEB3FF))
        drawArc(PaleViolet, 7f, 166f, false, point(75f, 77f), Size(8f * scale, 6f * scale),
            style = Stroke(1.3f * scale, cap = StrokeCap.Round))
        for ((x, y, radius) in listOf(Triple(22f, 44f, 3f), Triple(126f, 105f, 3.5f), Triple(117f, 33f, 4.5f), Triple(35f, 108f, 2.2f))) {
            drawLine(HeroLight, point(x - radius, y), point(x + radius, y), 1.4f * scale, StrokeCap.Round)
            drawLine(HeroLight, point(x, y - radius), point(x, y + radius), 1.4f * scale, StrokeCap.Round)
        }
        drawCircle(Color(0xFFDABEFF), 2.7f * scale, point(23f, 82f))
        drawCircle(Color(0xFFDABEFF), 2.7f * scale, point(133f, 65f))
    }
}
