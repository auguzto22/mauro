package dev.quantumlearn.app

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.quantumlearn.core.Answer
import dev.quantumlearn.core.AnswerSpec
import dev.quantumlearn.core.ChoiceType
import dev.quantumlearn.core.Feedback
import dev.quantumlearn.core.FormulaDrills
import dev.quantumlearn.core.Outcome
import dev.quantumlearn.core.Question
import dev.quantumlearn.core.TutorAction
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun QuizScreen(session: Session, model: StudyViewModel) {
    key(session.token) {
        QuestionScreen(session, model::back, model::submit, model::next, model::skip, model::finishInfinite)
    }
}

@Composable
private fun QuestionScreen(
    session: Session, back: () -> Unit, submit: (Answer) -> Unit, next: () -> Unit,
    skip: () -> Unit, finish: () -> Unit
) {
    val question = session.question
    val feedback = session.feedback
    val graded = feedback?.outcome in setOf(Outcome.Correct, Outcome.Incorrect)
    val wasFollowUp = question.id in session.followUps
    var selected by rememberSaveable { mutableIntStateOf(-1) }
    var entered by rememberSaveable { mutableStateOf("") }
    val colors = MaterialTheme.colorScheme
    val list = rememberLazyListState()
    val keyboard = LocalSoftwareKeyboardController.current
    var ordered by rememberSaveable { mutableStateOf<List<Int>>(emptyList()) }
    var matches by rememberSaveable { mutableStateOf<List<Int>>(emptyList()) }
    val currentAnswer = when (val spec = question.expected) {
        is AnswerSpec.Choice -> selected.takeIf { it >= 0 }?.let { Answer.Selection(it) }
        is AnswerSpec.Sequence -> ordered.takeIf { it.size == spec.steps.size }?.let { Answer.Text(it.joinToString(",")) }
        is AnswerSpec.Matching -> matches.takeIf { it.size == spec.pairs.size && it.none { value -> value < 0 } }
            ?.let { Answer.Text(it.joinToString(",")) }
        else -> entered.trim().takeIf(String::isNotBlank)?.let { Answer.Text(it) }
    }
    val showFeedback = feedback != null
    val showSkip = !session.isExam || feedback?.outcome == Outcome.Clarify

    LaunchedEffect(feedback) {
        if (showFeedback) list.animateScrollToItem(2)
    }
    val drill = FormulaDrills.isDrill(question)
    val promptShown = remember { MutableTransitionState(false).apply { targetState = true } }
    val answersShown = remember { MutableTransitionState(false).apply { targetState = true } }
    val feedbackShown = remember(feedback) { MutableTransitionState(false).apply { targetState = true } }

    Column(Modifier.fillMaxSize().navigationBarsPadding().imePadding()) {
        DetailHeader(session.settings?.mode?.label ?: if (drill) "Treino da fórmula" else "Prática", back) {
            if (session.isInfinite) TextButton(onClick = finish, enabled = !session.busy) { Text("Encerrar") }
            else Body("${session.index + 1} de ${session.questions.size}")
        }
        if (!session.isInfinite) {
            ProgressLine(session.index.toFloat() / session.questions.size, Modifier.fillMaxWidth().padding(18.dp, 12.dp))
        }
        LazyColumn(Modifier.weight(1f), state = list, contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item("prompt") {
                Reveal(0, promptShown) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (drill) FormulaBadge(question.formula.latex)
                        Eyebrow(if (wasFollowUp) "VERIFICAÇÃO · ${question.concept.uppercase()}" else question.concept.uppercase())
                        Text(question.prompt, style = if (drill) MaterialTheme.typography.headlineSmall
                            else MaterialTheme.typography.headlineMedium)
                        session.notice?.let { Text(it, color = colors.onSurfaceVariant, style = MaterialTheme.typography.bodySmall) }
                    }
                }
            }
            item("answer") {
                Reveal(2, answersShown) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        when (val spec = question.expected) {
                            is AnswerSpec.Choice -> {
                                Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    spec.options.forEachIndexed { index, option ->
                                        AnswerChoice(option.label, selected == index, !graded && !session.busy,
                                            if (graded && selected == index) feedback?.outcome else null,
                                            if (spec.type == ChoiceType.Graph) index else null) { selected = index }
                                    }
                                }
                            }
                            is AnswerSpec.Sequence -> {
                                spec.steps.forEachIndexed { index, step ->
                                    val position = ordered.indexOf(index)
                                    val label = if (position >= 0) "${position + 1}. $step" else step
                                    AnswerChoice(label, position >= 0, !graded && !session.busy && position < 0, null) {
                                        ordered = ordered + index
                                    }
                                }
                                if (ordered.isNotEmpty() && !graded) {
                                    TextButton(onClick = { ordered = emptyList() }, enabled = !session.busy) {
                                        Text("Reiniciar ordem")
                                    }
                                }
                            }
                            is AnswerSpec.Matching -> {
                                val options = (spec.pairs.indices.reversed().toList())
                                spec.pairs.forEachIndexed { index, pair ->
                                    Picker(pair.term, matches.getOrNull(index) ?: -1, options,
                                        { chosen -> spec.pairs.getOrNull(chosen)?.definition ?: "Selecione a relação" },
                                        enabled = !graded && !session.busy) { chosen ->
                                        matches = List(spec.pairs.size) { slot ->
                                            if (slot == index) chosen else matches.getOrNull(slot) ?: -1
                                        }
                                    }
                                }
                            }
                            is AnswerSpec.Numeric, is AnswerSpec.Written -> {
                                OutlinedTextField(entered, { entered = it.take(160) }, Modifier.fillMaxWidth(),
                                    enabled = !graded && !session.busy,
                                    label = { Text(if (spec is AnswerSpec.Numeric) "Valor ou expressão" else "Sua explicação") },
                                    supportingText = {
                                        if (spec is AnswerSpec.Numeric) Body(
                                            if (spec.unit == null) "Exemplos de entrada: 3/4, sqrt(9), 2,5e-3."
                                            else "Separe o valor da unidade por um espaço. Exemplo: 3 ${spec.unit}."
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    singleLine = spec is AnswerSpec.Numeric, minLines = if (spec is AnswerSpec.Written) 3 else 1)
                            }
                        }
                    }
                }
            }
            if (feedback != null) {
                item("feedback") { Reveal(0, feedbackShown) { FeedbackCard(question, feedback, wasFollowUp) } }
            }
        }
        Surface(color = colors.background) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                if (!graded && showSkip) {
                    TextButton(onClick = skip, enabled = !session.busy) {
                        Text(if (feedback?.outcome == Outcome.Clarify) "Pular sem avaliação" else "Pular questão")
                    }
                }
                PrimaryButton(
                    label = when {
                        session.busy -> "Salvando…"
                        graded -> "Continuar"
                        session.isExam -> "Registrar resposta"
                        feedback?.outcome == Outcome.Clarify -> "Enviar esclarecimento"
                        else -> "Conferir resposta"
                    },
                    onClick = {
                        if (graded) next() else {
                            keyboard?.hide()
                            currentAnswer?.let(submit)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !session.busy && (graded || currentAnswer != null)
                )
            }
        }
    }
}

@Composable
private fun AnswerChoice(
    label: String, selected: Boolean, enabled: Boolean, result: Outcome?, graph: Int? = null, select: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(14.dp)
    val background by animateColorAsState(when (result) {
        Outcome.Correct -> colors.tertiaryContainer
        Outcome.Incorrect -> colors.errorContainer
        else -> if (selected) colors.primaryContainer else colors.surface
    }, tween(260), label = "Fundo da alternativa")
    val tint by animateColorAsState(when (result) {
        Outcome.Correct -> colors.onTertiaryContainer
        Outcome.Incorrect -> colors.onErrorContainer
        else -> if (selected) colors.onPrimaryContainer else colors.onSurface
    }, tween(260), label = "Cor da alternativa")
    val pop by animateFloatAsState(if (selected && result == null) 1.02f else 1f,
        spring(Spring.DampingRatioMediumBouncy), label = "Seleção")
    val shake = remember { Animatable(0f) }
    LaunchedEffect(result) {
        when (result) {
            Outcome.Incorrect -> for (offset in listOf(14f, -12f, 9f, -6f, 3f, 0f)) shake.animateTo(offset, tween(55))
            Outcome.Correct -> {
                shake.animateTo(-6f, tween(90))
                shake.animateTo(0f, spring(Spring.DampingRatioMediumBouncy))
            }
            else -> Unit
        }
    }
    Surface(Modifier.graphicsLayer {
        scaleX = pop; scaleY = pop
        if (result == Outcome.Correct) { translationY = shake.value } else { translationX = shake.value }
    }, shape = shape, color = background, contentColor = tint,
        border = BorderStroke(1.dp, if (selected) tint.copy(alpha = .5f) else colors.outlineVariant)) {
        Row(Modifier.fillMaxWidth().selectable(selected, enabled, role = Role.RadioButton, onClick = select)
            .padding(14.dp), horizontalArrangement = Arrangement.spacedBy(11.dp),
            verticalAlignment = Alignment.CenterVertically) {
            if (result == Outcome.Correct) Mark(Glyph.Check, tint, Modifier.size(17.dp))
            else {
                Canvas(Modifier.size(17.dp)) {
                    drawCircle(tint.copy(alpha = .7f), radius = size.minDimension / 2 - 1.dp.toPx(),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(1.5.dp.toPx()))
                    if (selected) drawCircle(tint, radius = 3.8.dp.toPx())
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(label, style = MaterialTheme.typography.bodyLarge)
                if (graph != null) GraphOption(graph, tint)
            }
        }
    }
}

@Composable
private fun GraphOption(graph: Int, tint: Color) {
    val axis = MaterialTheme.colorScheme.outline
    Canvas(Modifier.fillMaxWidth().height(45.dp)) {
        val origin = Offset(.07f * size.width, .84f * size.height)
        drawLine(axis, origin, Offset(.93f * size.width, origin.y), 1.dp.toPx())
        drawLine(axis, origin, Offset(origin.x, .10f * size.height), 1.dp.toPx())
        val intercept = if (graph == 2) .84f else .45f
        val end = when (graph) { 0 -> .75f; 1 -> .13f; else -> .38f }
        drawLine(tint, Offset(origin.x, intercept * size.height), Offset(.89f * size.width, end * size.height),
            2.dp.toPx(), androidx.compose.ui.graphics.StrokeCap.Round)
    }
}

@Composable
private fun FeedbackCard(question: Question, feedback: Feedback, remediation: Boolean) {
    val colors = MaterialTheme.colorScheme
    val tint = when (feedback.outcome) {
        Outcome.Correct -> colors.onTertiaryContainer
        Outcome.Incorrect -> colors.onErrorContainer
        Outcome.Clarify -> colors.onPrimaryContainer
    }
    val container = when (feedback.outcome) {
        Outcome.Correct -> colors.tertiaryContainer
        Outcome.Incorrect -> colors.errorContainer
        Outcome.Clarify -> colors.primaryContainer
    }
    Column(Modifier.semantics { liveRegion = LiveRegionMode.Polite }, verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = container, contentColor = tint) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                val badge = remember { Animatable(.4f) }
                LaunchedEffect(Unit) { badge.animateTo(1f, spring(Spring.DampingRatioHighBouncy, Spring.StiffnessLow)) }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (feedback.outcome == Outcome.Correct) {
                        Mark(Glyph.Check, tint, Modifier.size(22.dp).graphicsLayer { scaleX = badge.value; scaleY = badge.value })
                    }
                    Text(when (feedback.outcome) {
                        Outcome.Correct -> if (remediation) "Verificação correta" else "Resposta correta"
                        Outcome.Incorrect -> "Entenda o seu resultado"
                        Outcome.Clarify -> "Precisamos de um detalhe"
                    }, style = MaterialTheme.typography.titleMedium)
                }
                Text(feedback.explanation, style = MaterialTheme.typography.bodyMedium)
                if (feedback.outcome == Outcome.Incorrect) {
                    Text("Sua resposta · ${feedback.answer}", style = MaterialTheme.typography.labelLarge)
                    Text("Resposta esperada · ${feedback.correctAnswer}", style = MaterialTheme.typography.labelLarge)
                    feedback.errorKind?.let { Text("Diagnóstico · ${it.label}", style = MaterialTheme.typography.bodySmall) }
                    feedback.confusion?.let { Text("Conceito a revisar · $it", style = MaterialTheme.typography.bodySmall) }
                }
            }
        }
        if (feedback.steps.isNotEmpty()) {
            Panel {
                SectionTitle("Resolução passo a passo")
                feedback.steps.forEachIndexed { index, step ->
                    Reveal(index + 1) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                            Box(Modifier.size(22.dp).background(colors.primary, RoundedCornerShape(11.dp)),
                                contentAlignment = Alignment.Center) {
                                Text("${index + 1}", color = colors.onPrimary, style = MaterialTheme.typography.labelSmall)
                            }
                            Body(step)
                        }
                    }
                }
                Equation(question.formula)
                SectionTitle("Outro exemplo")
                Body(feedback.example)
            }
        }
    }
}

@Composable
internal fun ResultsScreen(session: Session, completed: Boolean, model: StudyViewModel) {
    val correct = session.checks.count { it.result.outcome == Outcome.Correct }
    val earned = session.checks.sumOf(Checked::xp)
    val errors = session.checks.filter { it.result.outcome == Outcome.Incorrect }
    val lesson = session.lesson
    Column(Modifier.fillMaxSize().navigationBarsPadding()) {
        DetailHeader("Resultado", model::back)
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)) {
            item {
                val colors = MaterialTheme.colorScheme
                Panel(color = colors.primaryContainer) {
                    Box(Modifier.fillMaxWidth().height(105.dp), contentAlignment = Alignment.Center) {
                        if (correct > 0) Celebration(Modifier.fillMaxWidth().height(105.dp))
                        IconTile(if (completed) Glyph.Medal else Glyph.Target, colors.onPrimaryContainer, colors.surface,
                            Modifier.size(61.dp))
                    }
                    Text(if (completed) "Lição concluída!" else "Sessão finalizada", style = MaterialTheme.typography.headlineMedium)
                    Text("$correct de ${session.checks.size} respostas corretas · +$earned XP",
                        style = MaterialTheme.typography.titleMedium)
                    session.notice?.let { Body(it) }
                    if (session.repaired.isNotEmpty()) {
                        Body("${session.repaired.size} verificações corretas após o diagnóstico.")
                    }
                }
            }
            if (lesson != null) {
                item {
                    Panel {
                        SectionTitle("Revisão da lição")
                        Body(lesson.summary)
                        if (!completed) {
                            Body("A conclusão é registrada com dois acertos em enunciados diferentes.")
                            OutlinedButton(onClick = { model.openLesson(lesson) }) { Text("Rever lição") }
                        }
                    }
                }
            }
            if (errors.isNotEmpty()) {
                item { SectionTitle("Correções para revisar") }
                items(errors) { checked ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(checked.question.prompt, style = MaterialTheme.typography.titleMedium)
                        FeedbackCard(checked.question, checked.result, false)
                        TextButton(onClick = { model.tutorFor(checked.question.lessonId, TutorAction.Error) }) {
                            Text("Revisar o último erro no professor")
                        }
                    }
                }
            }
        }
        PrimaryButton(if (session.origin == Tab.Learn) "Voltar à trilha" else "Voltar a ${session.origin.label.lowercase()}",
            model::back, Modifier.fillMaxWidth().padding(18.dp, 11.dp))
    }
}

@Composable
private fun Celebration(modifier: Modifier) {
    val animation = remember { Animatable(0f) }
    LaunchedEffect(Unit) { animation.animateTo(1f, tween(720)) }
    val accents = listOf(Color(0xFF8B5CF6), Color(0xFFF4A8E5), Color(0xFF4ADE80), Color(0xFFF6BF71))
    Canvas(modifier) {
        val progress = animation.value
        for (i in 0..15) {
            val angle = i * PI / 8
            val position = center + Offset((cos(angle) * (.24 * size.width) * progress).toFloat(),
                (sin(angle) * (.40 * size.height) * progress).toFloat())
            drawCircle(accents[i % accents.size].copy(alpha = .4f + .6f * progress), (2 + i % 3).dp.toPx(), position)
        }
    }
}
