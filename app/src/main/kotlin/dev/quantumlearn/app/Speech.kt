package dev.quantumlearn.app

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import dev.quantumlearn.core.Phrase
import dev.quantumlearn.core.Word
import java.util.Locale

/** Leitura em voz alta com o motor de voz do aparelho, em inglês americano. Funciona offline se a voz estiver instalada. */
internal class Speaker(context: Context) : TextToSpeech.OnInitListener {
    private val engine = TextToSpeech(context.applicationContext, this)
    var ready by mutableStateOf(false)
        private set
    var speaking by mutableStateOf<String?>(null)
        private set

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val result = engine.setLanguage(Locale.US)
        ready = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        engine.setSpeechRate(.85f)
    }

    fun say(text: String, slow: Boolean = false) {
        if (!ready) return
        engine.setSpeechRate(if (slow) .55f else .85f)
        speaking = text
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "fala-${text.hashCode()}")
    }

    fun close() {
        engine.stop()
        engine.shutdown()
    }
}

@Composable
internal fun rememberSpeaker(): Speaker {
    val context = LocalContext.current
    val speaker = remember { Speaker(context) }
    DisposableEffect(speaker) { onDispose { speaker.close() } }
    return speaker
}

/** Ícone de alto-falante desenhado, no mesmo traço dos outros ícones do app. */
@Composable
internal fun SpeakerIcon(color: Color, modifier: Modifier = Modifier.size(20.dp), active: Boolean = false) {
    val pulse = rememberInfiniteTransition(label = "Som")
    val wave by pulse.animateFloat(.5f, 1f, infiniteRepeatable(tween(520), RepeatMode.Reverse), label = "Ondas")
    Canvas(modifier) {
        val s = size.minDimension / 24f
        fun p(x: Float, y: Float) = Offset(x * s, y * s)
        val body = Path().apply {
            moveTo(3 * s, 9 * s); lineTo(7 * s, 9 * s); lineTo(12 * s, 4 * s); lineTo(12 * s, 20 * s)
            lineTo(7 * s, 15 * s); lineTo(3 * s, 15 * s); close()
        }
        drawPath(body, color, style = Stroke(1.7f * s))
        val strength = if (active) wave else 1f
        drawArc(color.copy(alpha = strength), -50f, 100f, false, p(11f, 7f), androidx.compose.ui.geometry.Size(8 * s, 10 * s),
            style = Stroke(1.7f * s, cap = StrokeCap.Round))
        drawArc(color.copy(alpha = strength * .7f), -55f, 110f, false, p(11f, 3f), androidx.compose.ui.geometry.Size(13 * s, 18 * s),
            style = Stroke(1.7f * s, cap = StrokeCap.Round))
    }
}

/** Botão redondo de ouvir. Se a voz em inglês não estiver instalada, o botão fica desativado. */
@Composable
internal fun ListenButton(text: String, speaker: Speaker, slow: Boolean = false) {
    val colors = MaterialTheme.colorScheme
    FilledTonalIconButton(onClick = { speaker.say(text, slow) }, enabled = speaker.ready,
        modifier = Modifier.semantics { contentDescription = (if (slow) "Ouvir devagar: " else "Ouvir: ") + text }) {
        SpeakerIcon(colors.onSecondaryContainer, Modifier.size(20.dp), active = speaker.speaking == text)
    }
}

/** Frases para ouvir e repetir em voz alta, com tradução e pronúncia aproximada. */
@Composable
internal fun SpeakPractice(phrases: List<Phrase>) {
    val speaker = rememberSpeaker()
    val colors = MaterialTheme.colorScheme
    Panel {
        SectionTitle("Ouça e repita")
        Body("Toque para ouvir. Repita em voz alta logo depois. A linha em itálico mostra como soa, escrito do jeito que se lê em português.")
        phrases.forEachIndexed { index, phrase ->
            Reveal(index) {
                Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = colors.surfaceVariant,
                    border = BorderStroke(1.dp, if (speaker.speaking == phrase.english) colors.primary else colors.outlineVariant)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        ListenButton(phrase.english, speaker)
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(phrase.english, style = MaterialTheme.typography.titleMedium)
                            Text(phrase.sounds, style = MaterialTheme.typography.bodySmall, fontStyle = FontStyle.Italic,
                                color = colors.primary)
                            Text(phrase.portuguese, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
                        }
                        TextButton(onClick = { speaker.say(phrase.english, slow = true) }, enabled = speaker.ready) {
                            Text("Devagar")
                        }
                    }
                }
            }
        }
        if (!speaker.ready) {
            Text("Para ouvir as frases, instale a voz em inglês nas configurações de texto para voz do Android.",
                style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
        }
    }
}

/** Frase de uma questão de lacuna, com a resposta no lugar de "___". */
internal fun spokenAnswer(prompt: String, answer: String): String? {
    val sentence = prompt.substringAfterLast('\n')
    if ("___" !in sentence) return null
    val filled = sentence.replace("___", answer)
    // Em "frase → ___", só a parte depois da seta é a resposta montada.
    return (if ("→" in filled) filled.substringAfter("→") else filled)
        .replace(Regex("""\([^)]*\)"""), "").trim().takeIf { it.isNotBlank() }
}

/** Linha de ouvir a frase certa, mostrada no retorno das questões de inglês. */
@Composable
internal fun ListenRow(text: String, speaker: Speaker) {
    val colors = MaterialTheme.colorScheme
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = colors.surfaceVariant) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ListenButton(text, speaker)
            Column(Modifier.weight(1f)) {
                Eyebrow("OUÇA A FRASE CERTA")
                Text(text, style = MaterialTheme.typography.titleMedium)
            }
            TextButton(onClick = { speaker.say(text, slow = true) }, enabled = speaker.ready) { Text("Devagar") }
        }
    }
}

/** Glossário da lição: cada palavra abre para mostrar exemplo, tradução, pronúncia e áudio. */
@Composable
internal fun WordList(words: List<Word>) {
    val speaker = rememberSpeaker()
    val colors = MaterialTheme.colorScheme
    var open by rememberSaveable { mutableStateOf(-1) }
    Panel(Modifier.animateContentSize()) {
        SectionTitle("As ${words.size} palavras desta lição")
        Body("Toque numa palavra para ver o exemplo, a tradução e ouvir como se fala.")
        words.forEachIndexed { index, word ->
            val expanded = open == index
            val border by animateColorAsState(if (expanded) colors.primary else colors.outlineVariant, label = "Borda da palavra")
            Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = colors.surfaceVariant,
                border = BorderStroke(1.dp, border)) {
                Column(Modifier.clickable(onClickLabel = if (expanded) "Fechar ${word.word}" else "Abrir ${word.word}") {
                    open = if (expanded) -1 else index
                }.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(word.word, style = MaterialTheme.typography.titleMedium, color = colors.primary)
                        Text(word.meaning, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant)
                    }
                    AnimatedVisibility(expanded, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Pronúncia aproximada: ${word.sounds}", style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic, color = colors.primary)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                ListenButton(word.example, speaker)
                                Column(Modifier.weight(1f)) {
                                    Text(word.example, style = MaterialTheme.typography.bodyLarge)
                                    Text(word.translation, style = MaterialTheme.typography.bodySmall, color = colors.onSurfaceVariant)
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                TextButton(onClick = { speaker.say(word.word, slow = true) }, enabled = speaker.ready) {
                                    Text("Ouvir só a palavra")
                                }
                                TextButton(onClick = { speaker.say(word.example, slow = true) }, enabled = speaker.ready) {
                                    Text("Frase devagar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
