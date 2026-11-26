package dev.quantumlearn.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.quantumlearn.data.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val model: StudyViewModel = viewModel()
            val preferences by model.preferences.collectAsStateWithLifecycle()
            val mode = remember(preferences.theme) {
                ThemeMode.entries.firstOrNull { it.name == preferences.theme } ?: ThemeMode.Dark
            }
            QuantumTheme(mode) { dark ->
                SideEffect {
                    val style = if (dark) SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                    else SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
                    enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
                }
                App(model, preferences.dailyGoal, mode)
            }
        }
    }
}

@Composable
private fun App(model: StudyViewModel, dailyGoal: Int, mode: ThemeMode) {
    val page by model.page.collectAsStateWithLifecycle()
    val tab by model.tab.collectAsStateWithLifecycle()
    val progress by model.progress.collectAsStateWithLifecycle()
    var module by rememberSaveable { mutableIntStateOf(0) }

    BackHandler(page != Page.Home) { model.back() }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
        topBar = { if (page == Page.Home) AppBar(progress.level) },
        bottomBar = { if (page == Page.Home) BottomBar(tab, model::switchTab) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.TopCenter) {
            Box(Modifier.widthIn(max = 650.dp).fillMaxSize()) {
                when (val current = page) {
                    Page.Home -> key(tab) {
                        when (tab) {
                            Tab.Learn -> LearnScreen(progress, dailyGoal, module, { module = it }, model::openLesson)
                            Tab.Challenges -> ChallengeScreen(model)
                            Tab.Lab -> LaboratoryScreen()
                            Tab.Tutor -> TutorScreen(model)
                            Tab.Profile -> ProfileScreen(progress, dailyGoal, mode, model)
                        }
                    }
                    is Page.LessonDetail -> key(current.lesson.id) {
                        LessonScreen(current.lesson, model::back, { model.startLesson(current.lesson) },
                            { model.startFormulaDrill(current.lesson) },
                            { action -> model.tutorFor(current.lesson.id, action) })
                    }
                    is Page.Quiz -> QuizScreen(current.session, model)
                    is Page.Results -> ResultsScreen(current.session, current.completed, model)
                }
            }
        }
    }
}

@Composable
private fun AppBar(level: Int) {
    Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).statusBarsPadding(),
        contentAlignment = Alignment.Center) {
        Row(Modifier.widthIn(max = 650.dp).fillMaxWidth().padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconTile(Glyph.Atom, PaleViolet, MaterialTheme.colorScheme.primaryContainer, Modifier.size(37.dp))
            Text("Quantum Learn", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold)
            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                Text("Nível $level", Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun BottomBar(current: Tab, select: (Tab) -> Unit) {
    val colors = MaterialTheme.colorScheme
    Surface(color = colors.surface) {
        Column(Modifier.fillMaxWidth().navigationBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalDivider(color = colors.outlineVariant)
            Row(Modifier.widthIn(max = 650.dp).fillMaxWidth().selectableGroup()) {
                Tab.entries.forEach { tab ->
                    val selected = tab == current
                    val color = if (selected) colors.primary else colors.onSurfaceVariant
                    val icon = when (tab) {
                        Tab.Learn -> Glyph.Book
                        Tab.Challenges -> Glyph.Target
                        Tab.Lab -> Glyph.Flask
                        Tab.Tutor -> Glyph.Spark
                        Tab.Profile -> Glyph.Person
                    }
                    Column(Modifier.weight(1f).selectable(selected, role = Role.Tab, onClick = { select(tab) })
                        .padding(vertical = 7.dp), horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Surface(shape = RoundedCornerShape(11.dp),
                            color = if (selected) colors.primaryContainer else colors.surface) {
                            Mark(icon, color, Modifier.padding(horizontal = 13.dp, vertical = 4.dp).size(21.dp))
                        }
                        Text(tab.label, style = MaterialTheme.typography.labelSmall, color = color, maxLines = 1)
                    }
                }
            }
        }
    }
}
