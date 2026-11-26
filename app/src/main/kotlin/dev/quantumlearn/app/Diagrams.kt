package dev.quantumlearn.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    val motion = rememberInfiniteTransition(label = "Movimento do diagrama")
    val phase by motion.animateFloat(0f, 1f, infiniteRepeatable(tween(2600, easing = LinearEasing)), label = "Fase")
    val reveal = remember(lesson.id) { Animatable(0f) }
    LaunchedEffect(lesson.id) { reveal.animateTo(1f, tween(1200, easing = FastOutSlowInEasing)) }
    Canvas(Modifier.fillMaxWidth().height(132.dp).background(palette.surfaceVariant, RoundedCornerShape(12.dp))) {
        val ink = palette.primary
        val faint = palette.outline
        val grow = reveal.value
        val turn = phase
        fun at(x: Float, y: Float) = Offset(size.width * x, size.height * y)
        fun straight(a: Offset, b: Offset, color: Color = ink, width: Float = 2f) =
            drawLine(color, a, b, width.dp.toPx(), StrokeCap.Round)
        fun circle(x: Float, y: Float, radius: Float = 4f, tint: Color = ink) =
            drawCircle(tint, radius.dp.toPx(), at(x, y))
        /** Cada elemento k de n aparece em sequência enquanto o diagrama surge. */
        fun pop(k: Int, n: Int) = (grow * (n + 2) - k).coerceIn(0f, 1f)
        /** Vai e volta suavemente entre 0 e 1. */
        val swing = (.5f - .5f * cos(turn * 2 * PI)).toFloat()
        when (lesson.diagram) {
            Diagram.Groups -> {
                for (index in 0..2) {
                    val x = .086f + index * .209f
                    drawRoundRect(palette.primaryContainer, at(x, .24f), Size(size.width * .16f, size.height * .52f), CornerRadius(8.dp.toPx()))
                    for (i in 0..3) circle(x + .047f + (i % 2) * .066f, .39f + (i / 2) * .22f, 4f * pop(index * 4 + i, 14))
                }
                straight(at(.71f, .5f), at(.77f, .5f)); straight(at(.74f, .43f), at(.74f, .57f))
                circle(.83f, .50f, 4f * pop(12, 14)); circle(.90f, .50f, 4f * pop(13, 14))
            }
            Diagram.Parts -> {
                val highlighted = if (lesson.id == "born") 1 else 2
                for (part in 0..3) {
                    drawRoundRect(if (part < highlighted) ink.copy(alpha = .25f + .75f * pop(part, highlighted)) else palette.outlineVariant,
                        at(.09f + .21f * part, .38f),
                        Size(size.width * .19f, size.height * .24f), CornerRadius(7.dp.toPx()))
                }
            }
            Diagram.Growth, Diagram.Square -> {
                for (row in 0..2) for (column in 0..2) {
                    val distance = 23.dp.toPx()
                    drawCircle(ink, 5.dp.toPx() * pop(row * 3 + column, 9), center + Offset((column - 1) * distance, (row - 1) * distance))
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
                for (i in 0..4) circle(.15f + i * .175f, .54f, (if (i == 1) 7f else 4f) * pop(i, 5))
                straight(at(.74f, .32f), at(.83f, .32f))
                straight(at(.83f, .32f), at(.80f, .26f)); straight(at(.83f, .32f), at(.80f, .38f))
            }
            Diagram.Probability -> {
                for (i in 0..5) {
                    val height = .35f * pop(i, 6)
                    drawRoundRect(if (i % 2 == 1) ink else faint, at(.13f + i * .13f, .74f - height),
                        Size(size.width * .085f, size.height * height), CornerRadius(5.dp.toPx()))
                }
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
                    val tipX = .22f + .50f * grow
                    val tipY = start + (.24f - start) * grow
                    straight(at(.22f, start), at(tipX, tipY))
                    circle(.22f, start); circle(tipX, tipY, 4f + 1.5f * swing)
                    if (lesson.diagram == Diagram.Vector) {
                        straight(at(.73f, .25f), at(.73f, .79f), faint)
                    }
                }
            }
            Diagram.Wave, Diagram.Photon -> {
                val vertical = if (lesson.diagram == Diagram.Photon) .35f else .50f
                straight(at(.07f, vertical), at(.93f, vertical), faint)
                curve(ink, vertical, grow) { x -> .23 * sin(x * 4 * PI - turn * 2 * PI) }
                if (lesson.diagram == Diagram.Photon) {
                    straight(at(.07f, .72f), at(.93f, .72f), faint)
                    curve(palette.error, .72f, grow) { x -> .14 * sin(x * 8 * PI - turn * 4 * PI) }
                }
            }
            Diagram.Parabola, Diagram.Area, Diagram.Decay, Diagram.Modes, Diagram.Levels,
            Diagram.Threshold, Diagram.Bell, Diagram.Barrier -> {
                straight(at(.07f, .84f), at(.93f, .84f), faint, 1f)
                straight(at(.07f, .84f), at(.07f, .12f), faint, 1f)
                when (lesson.diagram) {
                    Diagram.Parabola -> {
                        curve(ink, .84f, grow) { x -> .66 * x * x }
                        if (lesson.id == "derivatives" && grow > .99f) {
                            // A reta tangente desliza pela curva: a inclinação muda em cada ponto.
                            val f = .25f + .6f * swing
                            val y = .84f - .66f * f * f
                            val slope = 1.32f * f
                            val dx = .18f
                            straight(at(.07f + .86f * (f - dx), y + slope * dx), at(.07f + .86f * (f + dx), y - slope * dx),
                                palette.tertiary, 1.5f)
                            circle(.07f + .86f * f, y, 3.5f, palette.tertiary)
                        }
                    }
                    Diagram.Area -> {
                        drawRect(ink.copy(alpha = .24f), at(.16f, .28f), Size(size.width * .57f * grow, size.height * .56f))
                        straight(at(.16f, .28f), at(.16f + .57f * grow, .28f))
                        straight(at(.16f, .84f), at(.16f, .28f), faint, 1f)
                        straight(at(.73f, .84f), at(.73f, .28f), faint, 1f)
                    }
                    Diagram.Decay -> curve(ink, .84f, grow) { x -> .66 * exp(-4 * x) }
                    Diagram.Modes -> listOf(.32f, .71f).forEachIndexed { index, x ->
                        val top = .84f - .58f * pop(index, 2)
                        straight(at(x, .84f), at(x, top))
                        circle(x, top, 4f)
                    }
                    Diagram.Levels -> for (n in 0..4) {
                        val y = when (lesson.id) {
                            "hydrogen" -> .21f + .62f / ((n + 1f) * (n + 1f))
                            "schrodinger-stationary" -> .84f - .025f * (n + 1f) * (n + 1f)
                            else -> .78f - .14f * n
                        }
                        straight(at(.25f, y), at(.79f, y), if (n == 0) palette.tertiary else ink)
                        // O elétron desce de nível em nível, como ao emitir fótons.
                        if (n == 4 - (turn * 5).toInt().coerceIn(0, 4)) circle(.52f, y - .05f, 4.5f, palette.tertiary)
                    }
                    Diagram.Threshold -> {
                        circle(.38f, .84f, 4f, palette.tertiary)
                        straight(at(.38f, .84f), at(.38f + .49f * grow, .84f - .61f * grow))
                    }
                    Diagram.Bell -> {
                        if (lesson.id == "blackbody") {
                            fun spectralShape(x: Double, temperature: Double): Double {
                                val wavelength = .22 + 2.7 * x
                                val occupation = exp(4.9651142317 / (wavelength * temperature)) - 1
                                return 1 / (wavelength * wavelength * wavelength * wavelength * wavelength * occupation)
                            }
                            curve(palette.tertiary, .84f, grow) { x -> 43 * spectralShape(x, 1.0) }
                            curve(ink, .84f, grow) { x -> 43 * spectralShape(x, 1.14) }
                        } else {
                            curve(ink, .84f, grow) { x -> .69 * exp(-70 * (x - .50) * (x - .50)) }
                            curve(palette.tertiary, .84f, grow) { x -> .35 * exp(-18 * (x - .50) * (x - .50)) }
                        }
                    }
                    Diagram.Barrier -> {
                        straight(at(.08f, .78f), at(.35f, .78f))
                        straight(at(.35f, .78f), at(.35f, .24f))
                        straight(at(.35f, .24f), at(.66f, .24f))
                        straight(at(.66f, .24f), at(.66f, .78f))
                        straight(at(.66f, .78f), at(.92f, .78f))
                        straight(at(.08f, .50f), at(.92f, .50f), palette.tertiary.copy(alpha = .35f), 1.5f)
                        // Uma partícula atravessa a barreira e sai mais fraca do outro lado.
                        val x = .08f + .84f * turn
                        val strength = when {
                            x < .35f -> 1f
                            x < .66f -> 1f - .7f * (x - .35f) / .31f
                            else -> .3f
                        }
                        circle(x, .50f, 5f, palette.tertiary.copy(alpha = strength))
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
                // Pontos andando pelo fio representam a corrente.
                for (k in 0 until 8) {
                    val t = (turn + k / 8f) % 1f
                    val point = when {
                        t < .25f -> at(.24f + .52f * (t / .25f), .29f)
                        t < .5f -> at(.76f, .29f + .43f * ((t - .25f) / .25f))
                        t < .75f -> at(.76f - .52f * ((t - .5f) / .25f), .72f)
                        else -> at(.24f, .72f - .43f * ((t - .75f) / .25f))
                    }
                    drawCircle(palette.tertiary, 2.6.dp.toPx(), point)
                }
            }
            Diagram.Matrix -> {
                val square = 27.dp.toPx()
                for (row in 0..1) for (column in 0..1) {
                    val emphasized = lesson.id != "decoherence" || row == column
                    val point = center + Offset((column - 1) * (square + 5.dp.toPx()), (row - 1) * (square + 5.dp.toPx()))
                    val pulse = if (emphasized) .65f + .35f * swing else 1f
                    drawRoundRect((if (emphasized) ink else faint).copy(alpha = pulse), point, Size(square, square), CornerRadius(4.dp.toPx()))
                }
            }
            Diagram.Energy, Diagram.Interference -> {
                for (i in 0..30) {
                    val brightness = cos(i * .47).let { it * it }
                    drawRect(ink.copy(alpha = ((.10 + brightness * .85) * pop(i, 31).coerceAtLeast(.05f)).toFloat()), at(.07f + i * .028f, .27f),
                        Size(size.width * .027f, size.height * .46f))
                }
            }
        }
    }
}

internal fun DrawScope.curve(color: Color, axis: Float, until: Float = 1f, value: (Double) -> Double) {
    val path = Path()
    val last = (140 * until.coerceIn(0f, 1f)).toInt()
    if (last < 1) return
    for (i in 0..last) {
        val f = i / 140.0
        val point = Offset(size.width * (.07f + .86f * f.toFloat()), size.height * (axis - value(f).toFloat()))
        if (i == 0) path.moveTo(point.x, point.y) else path.lineTo(point.x, point.y)
    }
    drawPath(path, color, style = Stroke(2.dp.toPx(), cap = StrokeCap.Round))
}
