package dev.quantumlearn.app

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.quantumlearn.core.ChallengeMode
import dev.quantumlearn.core.ChallengeSettings
import dev.quantumlearn.core.Curriculum
import dev.quantumlearn.core.Difficulty
import dev.quantumlearn.core.Progress
import dev.quantumlearn.core.StudyDay
import dev.quantumlearn.core.TutorAction
import dev.quantumlearn.data.ThemeMode

@Composable
internal fun ScreenHeading(title: String, subtitle: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(title, style = MaterialTheme.typography.headlineLarge)
        Body(subtitle)
    }
}

@Composable
internal fun <T> Picker(label: String, value: T, values: List<T>, title: (T) -> String, enabled: Boolean = true, change: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = colors.onSurfaceVariant)
        Surface(onClick = { expanded = true }, enabled = enabled, color = colors.surfaceVariant, shape = RoundedCornerShape(12.dp)) {
            Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                Text(title(value), Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
                Text("⌄", color = colors.primary)
            }
        }
        DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
            for (candidate in values) {
                DropdownMenuItem(text = { Text(title(candidate)) }, onClick = {
                    expanded = false
                    change(candidate)
                })
            }
        }
    }
}

@Composable
internal fun ChallengeScreen(model: StudyViewModel) {
    val settings by model.challengeSettings.collectAsStateWithLifecycle()
    val notice by model.challengeNotice.collectAsStateWithLifecycle()
    val colors = MaterialTheme.colorScheme
    LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { ScreenHeading("Desafios", "Pratique um assunto ou revise o que já estudou.") }
        item {
            Panel(color = colors.primaryContainer) {
                Row(horizontalArrangement = Arrangement.spacedBy(11.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconTile(Glyph.Flame, colors.primary, colors.surface)
                    Column(Modifier.weight(1f)) {
                        SectionTitle("Desafio diário")
                        Text("Três perguntas selecionadas para hoje.", style = MaterialTheme.typography.bodySmall)
                    }
                }
                PrimaryButton("Jogar desafio diário", {
                    model.startChallenge(ChallengeSettings(count = 3, mode = ChallengeMode.Daily))
                }, Modifier.fillMaxWidth())
            }
        }
        item {
            Panel {
                SectionTitle("Monte sua sessão")
                Picker("Modo", settings.mode, ChallengeMode.entries.filter { it != ChallengeMode.Daily },
                    { it.label }) { model.updateSettings(settings.copy(mode = it)) }
                Body(when (settings.mode) {
                    ChallengeMode.Random -> "Mistura os exercícios disponíveis e prioriza os menos recentes."
                    ChallengeMode.Review -> "Prioriza conceitos com revisão agendada ou já estudados."
                    ChallengeMode.Exam -> "As correções aparecem no resultado da sessão."
                    ChallengeMode.Infinite -> "Continue respondendo até escolher encerrar."
                    ChallengeMode.Calculation -> "Treine com respostas numéricas e variações calculadas pelo aplicativo."
                    ChallengeMode.Daily -> "Seleção do dia."
                })
                Picker("Assunto", settings.lessonId, listOf(null) + Curriculum.lessons.map { it.id },
                    { id -> id?.let { Curriculum.byId.getValue(it).title } ?: "Todos os assuntos" }) {
                    model.updateSettings(settings.copy(lessonId = it))
                }
                Picker("Dificuldade", settings.difficulty, listOf(null) + Difficulty.entries,
                    { it?.label ?: "Adaptativa" }) { model.updateSettings(settings.copy(difficulty = it)) }
                if (settings.mode != ChallengeMode.Infinite) {
                    Picker("Quantidade", settings.count, listOf(3, 5, 10, 20, 30), { "$it questões" }) {
                        model.updateSettings(settings.copy(count = it))
                    }
                }
                notice?.let { Text(it, color = colors.error, style = MaterialTheme.typography.bodyMedium) }
                PrimaryButton("Começar desafio", { model.startChallenge() }, Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
internal fun TutorScreen(model: StudyViewModel) {
    val topic by model.tutorLesson.collectAsStateWithLifecycle()
    val reply by model.tutorReply.collectAsStateWithLifecycle()
    val list = rememberLazyListState()
    val colors = MaterialTheme.colorScheme
    LaunchedEffect(reply) { if (reply != null) list.animateScrollToItem(2) }
    LazyColumn(state = list, contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ScreenHeading("Professor", "Revise conceitos com o Íon, seu tutor offline.")
        }
        item {
            Panel(color = colors.primaryContainer) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Mascot(Modifier.size(84.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        SectionTitle("Como posso ajudar?")
                        Text("Escolha um assunto e o tipo de explicação.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Picker("Assunto", topic, Curriculum.lessons.map { it.id },
                    { Curriculum.byId.getValue(it).title }, change = { model.tutorFor(it) })
            }
        }
        reply?.let { response ->
            item {
                Panel {
                    Eyebrow(response.evidence.label.uppercase())
                    Text(response.text, style = MaterialTheme.typography.bodyLarge)
                    response.formula?.let { Equation(it) }
                    References(response.references)
                }
            }
        }
        items(TutorAction.entries, key = { it.name }) { action ->
            ActionRow(action.label, if (action == TutorAction.Practice) Glyph.Target else Glyph.Spark) {
                model.tutorAction(action)
            }
        }
    }
}

@Composable
private fun ActionRow(label: String, icon: Glyph, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Surface(onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = colors.surface,
        border = BorderStroke(1.dp, colors.outlineVariant)) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconTile(icon, colors.primary, colors.primaryContainer)
            Text(label, Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
            Mark(Glyph.Arrow, colors.primary, Modifier.size(17.dp))
        }
    }
}

@Composable
internal fun ProfileScreen(progress: Progress, goal: Int, mode: ThemeMode, model: StudyViewModel) {
    val colors = MaterialTheme.colorScheme
    var selectedModule by rememberSaveable { mutableIntStateOf(0) }
    var certificate by rememberSaveable { mutableStateOf<String?>(null) }
    val notice by model.saveNotice.collectAsStateWithLifecycle()
    val modules = Curriculum.modules
    val module = modules[selectedModule]
    val transferNotice by model.transferNotice.collectAsStateWithLifecycle()
    val transferring by model.transferring.collectAsStateWithLifecycle()
    val export = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        uri?.let { model.transfer(it, importing = false) }
    }
    val restore = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { model.transfer(it, importing = true) }
    }
    val mastered = progress.mastery.count { (_, mastery) -> mastery.percent >= 75 }
    val retained = progress.mastery.values.sumOf { it.retainedChecks }
    val completedModules = modules.filter { entry -> entry.lessons.all { it.id in progress.completions } }
    val badges = buildList {
        if (progress.xp > 0) add("Primeiro acerto")
        if (progress.streak >= 3) add("Três dias em sequência")
        if (progress.xpToday >= goal) add("Meta de hoje")
        if (retained > 0) add("Retenção demonstrada")
        if (mastered > 0) add("$mastered medalhas de domínio")
    }

    LazyColumn(contentPadding = PaddingValues(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { ScreenHeading("Seu perfil", "Acompanhe o que você já demonstrou e o que vale revisar.") }
        item {
            Panel(color = colors.primaryContainer) {
                Row(horizontalArrangement = Arrangement.spacedBy(11.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconTile(Glyph.Person, colors.primary, colors.surface)
                    Column {
                        SectionTitle("Nível ${progress.level}")
                        Body("${progress.xp} XP no total")
                    }
                }
                ProgressLine((progress.xp % 100) / 100f, Modifier.fillMaxWidth(), track = colors.surfaceVariant)
                Body("${100 - progress.xp % 100} XP até o próximo nível")
            }
        }
        item {
            Panel {
                SectionTitle("Desempenho")
                Body("${progress.accuracy}% de acertos · ${progress.streak} dias em sequência")
                Body("${progress.minutes} min de estudo registrados · $retained revisões espaçadas corretas")
                WeeklyActivity(progress.recentDays)
                Text("Meta diária · ${progress.xpToday}/$goal XP", style = MaterialTheme.typography.labelLarge)
                ProgressLine(progress.xpToday.toFloat() / goal, Modifier.fillMaxWidth())
            }
        }
        item {
            Panel {
                SectionTitle("Domínio por assunto")
                Body("O cálculo considera acertos, dificuldade, variedade e retenção em revisões espaçadas.")
                Picker("Módulo", selectedModule, modules.indices.toList(), { modules[it].title }) { selectedModule = it }
                for (lesson in module.lessons) {
                    val evidence = progress.mastery.getValue(lesson.id)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(lesson.title, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        Text(if (evidence.attempts == 0) "A estudar" else "${evidence.percent}%",
                            style = MaterialTheme.typography.labelLarge, color = colors.primary)
                    }
                    ProgressLine(evidence.percent / 100f, Modifier.fillMaxWidth())
                }
            }
        }
        item {
            Panel {
                SectionTitle("Erros recorrentes")
                val recurring = progress.errors.filter { it.second >= 2 }
                if (recurring.isEmpty()) Body("Erros semelhantes aparecem aqui quando ocorrem mais de uma vez.")
                else recurring.take(5).forEach { (concept, count) -> Body("$concept · $count ocorrências recentes") }
            }
        }
        item { SectionTitle("Recomendado para você") }
        items(progress.recommendations, key = { it.id }) { recommended ->
            ActionRow(recommended.title, Glyph.Book) { model.openLesson(recommended) }
        }
        item {
            Panel {
                SectionTitle("Conquistas")
                if (badges.isEmpty()) Body("Seu primeiro acerto libera a primeira conquista.")
                else badges.forEach { Body(it) }
                if (completedModules.isNotEmpty()) {
                    SectionTitle("Certificados internos")
                    completedModules.forEach { module ->
                        TextButton(onClick = { certificate = module.title }) { Text(module.title) }
                    }
                }
            }
        }
        item {
            Panel {
                SectionTitle("Preferências")
                Picker("Aparência", mode, ThemeMode.entries, { it.label }, change = model::theme)
                Picker("Meta diária", goal, listOf(10, 20, 30, 40, 50, 60), { "$it XP" }, change = model::goal)
                notice?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = colors.error) }
            }
        }
        item {
            Panel {
                SectionTitle("Transferir progresso")
                Body("Salve as respostas e conclusões em um arquivo. Importe-o em outro aparelho para mesclar com o progresso local.")
                OutlinedButton(onClick = { export.launch("quantum-learn-progresso.json") },
                    enabled = !transferring, modifier = Modifier.fillMaxWidth()) { Text("Exportar progresso") }
                OutlinedButton(onClick = { restore.launch(arrayOf("application/json", "text/plain")) },
                    enabled = !transferring, modifier = Modifier.fillMaxWidth()) { Text("Importar progresso") }
                transferNotice?.let { Body(it) }
            }
        }
    }
    certificate?.let { title ->
        AlertDialog(onDismissRequest = { certificate = null },
            icon = { Mark(Glyph.Medal, colors.primary) },
            title = { Text("Certificado interno") },
            text = { Text("Você concluiu todas as lições publicadas de $title no Quantum Learn.") },
            confirmButton = { TextButton(onClick = { certificate = null }) { Text("Fechar") } })
    }
}

@Composable
private fun WeeklyActivity(days: List<StudyDay>) {
    val colors = MaterialTheme.colorScheme
    val maximum = days.maxOfOrNull(StudyDay::answers)?.coerceAtLeast(1) ?: 1
    Text("Últimos sete dias", style = MaterialTheme.typography.labelLarge)
    Canvas(Modifier.fillMaxWidth().height(50.dp).description(days.joinToString(". ") {
        "Dia ${it.date.dayOfMonth}, ${it.correct} de ${it.answers} corretas"
    })) {
        val space = size.width / 7
        val width = space * .62f
        days.forEachIndexed { index, record ->
            val x = space * index + (space - width) / 2
            val all = size.height * record.answers / maximum
            val correct = size.height * record.correct / maximum
            drawRoundRect(colors.surfaceVariant, Offset(x, 0f), Size(width, size.height), CornerRadius(4.dp.toPx()))
            if (all > 0) drawRoundRect(colors.primaryContainer, Offset(x, size.height - all), Size(width, all), CornerRadius(4.dp.toPx()))
            if (correct > 0) drawRoundRect(colors.primary, Offset(x, size.height - correct), Size(width, correct), CornerRadius(4.dp.toPx()))
        }
    }
    Row(Modifier.fillMaxWidth()) {
        days.forEach { day ->
            androidx.compose.foundation.layout.Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(day.date.dayOfMonth.toString(), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
