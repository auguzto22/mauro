package dev.quantumlearn.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import dev.quantumlearn.core.Diagram
import dev.quantumlearn.core.Lesson
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

@Composable
internal fun LessonDiagram(lesson: Lesson) {
    val palette = MaterialTheme.colorScheme
    Canvas(Modifier.fillMaxWidth().height(132.dp).background(palette.surfaceVariant, RoundedCornerShape(12.dp))) {
        val ink = palette.primary
        val faint = palette.outline
        fun at(x: Float, y: Float) = Offset(size.width * x, size.height * y)
        fun straight(a: Offset, b: Offset, color: Color = ink, width: Float = 2f) =
            drawLine(color, a, b, width.dp.toPx(), StrokeCap.Round)
        fun circle(x: Float, y: Float, radius: Float = 4f, tint: Color = ink) =
            drawCircle(tint, radius.dp.toPx(), at(x, y))
        when (lesson.diagram) {
            Diagram.Groups -> {
                for (index in 0..2) {
                    val x = .086f + index * .209f
                    drawRoundRect(palette.primaryContainer, at(x, .24f), Size(size.width * .16f, size.height * .52f), CornerRadius(8.dp.toPx()))
                    for (i in 0..3) circle(x + .047f + (i % 2) * .066f, .39f + (i / 2) * .22f)
                }
                straight(at(.71f, .5f), at(.77f, .5f)); straight(at(.74f, .43f), at(.74f, .57f))
                circle(.83f, .50f); circle(.90f, .50f)
            }
            Diagram.Parts -> {
                val highlighted = if (lesson.id == "born") 1 else 2
                for (part in 0..3) {
                    drawRoundRect(if (part < highlighted) ink else palette.outlineVariant, at(.09f + .21f * part, .38f),
                        Size(size.width * .19f, size.height * .24f), CornerRadius(7.dp.toPx()))
                }
            }
            Diagram.Growth, Diagram.Square -> {
                for (row in 0..2) for (column in 0..2) {
                    val distance = 23.dp.toPx()
                    drawCircle(ink, 5.dp.toPx(), center + Offset((column - 1) * distance, (row - 1) * distance))
                }
            }
            Diagram.Balance -> {
                straight(at(.24f, .34f), at(.76f, .34f))
                straight(at(.50f, .24f), at(.50f, .78f))
                straight(at(.40f, .78f), at(.60f, .78f))
                for (x in listOf(.24f, .76f)) {
                    straight(at(x, .34f), at(x - .09f, .62f))
                    straight(at(x, .34f), at(x + .09f, .62f))
                    straight(at(x - .09f, .62f), at(x + .09f, .62f))
                }
            }
            Diagram.Scale -> {
                straight(at(.15f, .54f), at(.85f, .54f), faint)
                for (i in 0..4) circle(.15f + i * .175f, .54f, if (i == 1) 7f else 4f)
                straight(at(.74f, .32f), at(.83f, .32f))
                straight(at(.83f, .32f), at(.80f, .26f)); straight(at(.83f, .32f), at(.80f, .38f))
            }
            Diagram.Probability -> {
                for (i in 0..5) drawRoundRect(if (i % 2 == 1) ink else faint,
                    at(.13f + i * .13f, .39f), Size(size.width * .085f, size.height * .35f), CornerRadius(5.dp.toPx()))
                straight(at(.10f, .77f), at(.92f, .77f), faint)
            }
            Diagram.Vector, Diagram.Line -> {
                straight(at(.15f, .79f), at(.88f, .79f), faint)
                straight(at(.22f, .86f), at(.22f, .13f), faint)
                if (lesson.id == "spin") {
                    straight(at(.22f, .79f), at(.72f, .79f))
                    straight(at(.22f, .79f), at(.22f, .28f))
                } else {
                    val start = if (lesson.diagram == Diagram.Line) .64f else .79f
                    straight(at(.22f, start), at(.72f, .24f))
                    circle(.22f, start); circle(.72f, .24f)
                    if (lesson.diagram == Diagram.Vector) {
                        straight(at(.73f, .25f), at(.73f, .79f), faint)
                    }
                }
            }
            Diagram.Wave, Diagram.Photon -> {
                val vertical = if (lesson.diagram == Diagram.Photon) .35f else .50f
                straight(at(.07f, vertical), at(.93f, vertical), faint)
                curve(ink, vertical) { x -> .23 * sin(x * 4 * PI) }
                if (lesson.diagram == Diagram.Photon) {
                    straight(at(.07f, .72f), at(.93f, .72f), faint)
                    curve(palette.error, .72f) { x -> .14 * sin(x * 8 * PI) }
                }
            }
            Diagram.Parabola, Diagram.Area, Diagram.Decay, Diagram.Modes, Diagram.Levels,
            Diagram.Threshold, Diagram.Bell, Diagram.Barrier -> {
                straight(at(.07f, .84f), at(.93f, .84f), faint, 1f)
                straight(at(.07f, .84f), at(.07f, .12f), faint, 1f)
                when (lesson.diagram) {
                    Diagram.Parabola -> {
                        curve(ink, .84f) { x -> .66 * x * x }
                        if (lesson.id == "derivatives") {
                            straight(at(.31f, .82f), at(.85f, .32f), palette.tertiary, 1.5f)
                            circle(.57f, .62f, 3.5f, palette.tertiary)
                        }
                    }
                    Diagram.Area -> {
                        drawRect(ink.copy(alpha = .24f), at(.16f, .28f), Size(size.width * .57f, size.height * .56f))
                        straight(at(.16f, .28f), at(.73f, .28f))
                        straight(at(.16f, .84f), at(.16f, .28f), faint, 1f)
                        straight(at(.73f, .84f), at(.73f, .28f), faint, 1f)
                    }
                    Diagram.Decay -> curve(ink, .84f) { x -> .66 * exp(-4 * x) }
                    Diagram.Modes -> for (x in listOf(.32f, .71f)) {
                        straight(at(x, .84f), at(x, .26f))
                        circle(x, .26f, 4f)
                    }
                    Diagram.Levels -> for (n in 0..4) {
                        val y = when (lesson.id) {
                            "hydrogen" -> .21f + .62f / ((n + 1f) * (n + 1f))
                            "schrodinger-stationary" -> .84f - .025f * (n + 1f) * (n + 1f)
                            else -> .78f - .14f * n
                        }
                        straight(at(.25f, y), at(.79f, y), if (n == 0) palette.tertiary else ink)
                    }
                    Diagram.Threshold -> {
                        circle(.38f, .84f, 4f, palette.tertiary)
                        straight(at(.38f, .84f), at(.87f, .23f))
                    }
                    Diagram.Bell -> {
                        if (lesson.id == "blackbody") {
                            fun spectralShape(x: Double, temperature: Double): Double {
                                val wavelength = .22 + 2.7 * x
                                val occupation = exp(4.9651142317 / (wavelength * temperature)) - 1
                                return 1 / (wavelength * wavelength * wavelength * wavelength * wavelength * occupation)
                            }
                            curve(palette.tertiary, .84f) { x -> 43 * spectralShape(x, 1.0) }
                            curve(ink, .84f) { x -> 43 * spectralShape(x, 1.14) }
                        } else {
                            curve(ink, .84f) { x -> .69 * exp(-70 * (x - .50) * (x - .50)) }
                            curve(palette.tertiary, .84f) { x -> .35 * exp(-18 * (x - .50) * (x - .50)) }
                        }
                    }
                    Diagram.Barrier -> {
                        straight(at(.08f, .78f), at(.35f, .78f))
                        straight(at(.35f, .78f), at(.35f, .24f))
                        straight(at(.35f, .24f), at(.66f, .24f))
                        straight(at(.66f, .24f), at(.66f, .78f))
                        straight(at(.66f, .78f), at(.92f, .78f))
                        straight(at(.08f, .50f), at(.92f, .50f), palette.tertiary, 1.5f)
                    }
                    else -> Unit
                }
            }
            Diagram.Circuit -> {
                straight(at(.24f, .29f), at(.43f, .29f))
                straight(at(.57f, .29f), at(.76f, .29f))
                straight(at(.76f, .29f), at(.76f, .72f))
                straight(at(.76f, .72f), at(.52f, .72f))
                straight(at(.48f, .72f), at(.24f, .72f))
                straight(at(.24f, .72f), at(.24f, .29f))
                drawRect(ink, at(.43f, .23f), Size(size.width * .14f, size.height * .12f), style = Stroke(2.dp.toPx()))
                straight(at(.48f, .62f), at(.48f, .82f), palette.tertiary)
                straight(at(.52f, .67f), at(.52f, .77f), palette.tertiary)
            }
            Diagram.Matrix -> {
                val square = 27.dp.toPx()
                for (row in 0..1) for (column in 0..1) {
                    val emphasized = lesson.id != "decoherence" || row == column
                    val point = center + Offset((column - 1) * (square + 5.dp.toPx()), (row - 1) * (square + 5.dp.toPx()))
                    drawRoundRect(if (emphasized) ink else faint, point, Size(square, square), CornerRadius(4.dp.toPx()))
                }
            }
            Diagram.Energy, Diagram.Interference -> {
                for (i in 0..30) {
                    val brightness = cos(i * .47).let { it * it }
                    drawRect(ink.copy(alpha = (.10 + brightness * .85).toFloat()), at(.07f + i * .028f, .27f),
                        Size(size.width * .027f, size.height * .46f))
                }
            }
        }
    }
}

internal fun DrawScope.curve(color: Color, axis: Float, value: (Double) -> Double) {
    val path = Path()
    for (i in 0..140) {
        val f = i / 140.0
        val point = Offset(size.width * (.07f + .86f * f.toFloat()), size.height * (axis - value(f).toFloat()))
        if (i == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
    }
    drawPath(path, color, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round))
}
