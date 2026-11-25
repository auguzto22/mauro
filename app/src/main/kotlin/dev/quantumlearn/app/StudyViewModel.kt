package dev.quantumlearn.app

import android.app.Application
import android.net.Uri
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.quantumlearn.core.Answer
import dev.quantumlearn.core.Attempt
import dev.quantumlearn.core.ChallengeMode
import dev.quantumlearn.core.ChallengeSettings
import dev.quantumlearn.core.Corrections
import dev.quantumlearn.core.Curriculum
import dev.quantumlearn.core.Difficulty
import dev.quantumlearn.core.Feedback
import dev.quantumlearn.core.Learning
import dev.quantumlearn.core.Lesson
import dev.quantumlearn.core.OfflineTutor
import dev.quantumlearn.core.Outcome
import dev.quantumlearn.core.Practice
import dev.quantumlearn.core.Question
import dev.quantumlearn.core.QuestionSelection
import dev.quantumlearn.core.TutorAction
import dev.quantumlearn.core.TutorProvider
import dev.quantumlearn.core.TutorReply
import dev.quantumlearn.core.TutorRequest
import dev.quantumlearn.data.StudyBackup
import dev.quantumlearn.data.StudyDatabase
import dev.quantumlearn.data.StudyPreferences
import dev.quantumlearn.data.StudyRepository
import dev.quantumlearn.data.ThemeMode
import java.time.Instant
import java.time.ZoneId
import java.util.UUID
import kotlin.random.Random
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Tab(val label: String) {
    Learn("Aprender"), Challenges("Desafios"), Lab("Laboratório"), Tutor("Professor"), Profile("Perfil")
}

sealed interface Page {
    data object Home : Page
    data class LessonDetail(val lesson: Lesson) : Page
    data class Quiz(val session: Session) : Page
    data class Results(val session: Session, val completed: Boolean) : Page
}

data class Checked(val question: Question, val result: Feedback, val xp: Int)

data class Session(
    val questions: List<Question>,
    val origin: Tab,
    val lesson: Lesson? = null,
    val settings: ChallengeSettings? = null,
    val index: Int = 0,
    val checks: List<Checked> = emptyList(),
    val feedback: Feedback? = null,
    val followUps: Set<String> = emptySet(),
    val repaired: Set<String> = emptySet(),
    val shownAt: Long = SystemClock.elapsedRealtime(),
    val token: String = UUID.randomUUID().toString(),
    val busy: Boolean = false,
    val notice: String? = null
) {
    val question get() = questions[index]
    val isExam get() = settings?.mode == ChallengeMode.Exam
    val isInfinite get() = settings?.mode == ChallengeMode.Infinite
}

class StudyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StudyRepository(StudyDatabase.get(application))
    private val backup = StudyBackup(repository, application.contentResolver)
    private val tutor: TutorProvider = OfflineTutor
    private val authoredQuestions by lazy { Curriculum.questions.associateBy(Question::id) }

    val page = MutableStateFlow<Page>(Page.Home)
    val tab = MutableStateFlow(Tab.Learn)
    val tutorLesson = MutableStateFlow("operations")
    val tutorReply = MutableStateFlow<TutorReply?>(null)
    val challengeSettings = MutableStateFlow(ChallengeSettings())
    val challengeNotice = MutableStateFlow<String?>(null)
    val saveNotice = MutableStateFlow<String?>(null)
    val transferNotice = MutableStateFlow<String?>(null)
    val transferring = MutableStateFlow(false)
    val preferences = repository.preferences.stateIn(viewModelScope, SharingStarted.Eagerly, StudyPreferences())
    private val attempts = repository.attempts.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val ticks = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(30_000)
        }
    }
    val progress = combine(attempts, repository.completions, ticks) { evidence, completions, _ ->
        Learning.progress(Curriculum.lessons, evidence, completions, System.currentTimeMillis(), ZoneId.systemDefault())
    }.stateIn(viewModelScope, SharingStarted.Eagerly,
        Learning.progress(Curriculum.lessons, emptyList(), emptyList(), System.currentTimeMillis(), ZoneId.systemDefault()))

    private var openedLessonAt = 0L
    private var tutorRevision = 0

    fun switchTab(next: Tab) {
        page.value = Page.Home
        tab.value = next
    }

    fun openLesson(lesson: Lesson) {
        openedLessonAt = SystemClock.elapsedRealtime()
        page.value = Page.LessonDetail(lesson)
    }

    fun back() {
        when (val current = page.value) {
            is Page.Quiz -> if (!current.session.busy) {
                current.session.lesson?.takeIf { current.session.origin == Tab.Learn }
                    ?.let(::openLesson) ?: switchTab(current.session.origin)
            }
            is Page.Results -> switchTab(current.session.origin)
            is Page.LessonDetail -> page.value = Page.Home
            Page.Home -> Unit
        }
    }

    fun startLesson(lesson: Lesson) {
        page.value = Page.Quiz(Session(lesson.questions, tab.value, lesson,
            shownAt = openedLessonAt.takeIf { it > 0 } ?: SystemClock.elapsedRealtime()))
    }

    fun updateSettings(settings: ChallengeSettings) {
        challengeNotice.value = null
        challengeSettings.value = settings
    }

    fun startChallenge(override: ChallengeSettings = challengeSettings.value) {
        val now = System.currentTimeMillis()
        val available = Curriculum.questions.toMutableList()
        val addVariants = override.mode in setOf(ChallengeMode.Calculation, ChallengeMode.Infinite) ||
            override.difficulty == Difficulty.Advanced
        val random = if (override.mode == ChallengeMode.Daily) {
            val date = Instant.ofEpochMilli(now).atZone(ZoneId.systemDefault()).toLocalDate()
            Random(date.toEpochDay())
        } else Random.Default
        if (addVariants) {
            available += variants(override.lessonId, random, 4)
        }
        val selected = QuestionSelection.select(available.distinctBy(Question::prompt), override,
            attempts.value, progress.value.mastery, now = now, random = random)
        if (selected.isEmpty()) {
            challengeNotice.value = "Nenhuma questão combina com estes filtros. Escolha outra dificuldade ou assunto."
            return
        }
        challengeNotice.value = null
        tab.value = Tab.Challenges
        val notice = if (selected.size < override.count && override.mode != ChallengeMode.Infinite)
            "Há ${selected.size} questões disponíveis com estes filtros." else null
        page.value = Page.Quiz(Session(selected, Tab.Challenges, settings = override, notice = notice))
    }

    private fun variants(lessonId: String?, random: Random, perLesson: Int): List<Question> =
        Curriculum.lessons.asSequence().filter { lessonId == null || it.id == lessonId }.flatMap { lesson ->
            List(perLesson) { Practice.generate(lesson.id, random.nextInt(Int.MAX_VALUE)) }.filterNotNull()
        }.toList()

    fun submit(answer: Answer) {
        val session = (page.value as? Page.Quiz)?.session ?: return
        if (session.busy || session.feedback?.outcome in setOf(Outcome.Correct, Outcome.Incorrect)) return
        val feedback = Corrections.grade(session.question, answer)
        if (feedback.outcome == Outcome.Clarify) {
            page.value = Page.Quiz(session.copy(feedback = feedback))
            return
        }
        page.value = Page.Quiz(session.copy(busy = true))
        viewModelScope.launch {
            try {
                val correct = feedback.outcome == Outcome.Correct
                val now = Instant.now()
                val zone = ZoneId.systemDefault()
                val expectedXp = Learning.xp(correct, session.question.difficulty)
                val xp = repository.record(Attempt(session.token, session.question.id, session.question.lessonId,
                    correct, session.question.difficulty, feedback.errorKind, feedback.confusion, now.toEpochMilli(),
                    (SystemClock.elapsedRealtime() - session.shownAt).coerceIn(0, 600_000), expectedXp, answer),
                    now.atZone(zone).toLocalDate().atStartOfDay(zone).toInstant().toEpochMilli())
                val checked = session.copy(checks = session.checks + Checked(session.question, feedback, xp),
                    feedback = feedback, busy = false,
                    repaired = if (correct && session.question.id in session.followUps)
                        session.repaired + session.question.id else session.repaired)
                if (session.isExam) advance(checked) else page.value = Page.Quiz(checked)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                page.value = Page.Quiz(session.copy(notice = "Não foi possível salvar a resposta. Tente conferir novamente."))
            }
        }
    }

    fun next() {
        val session = (page.value as? Page.Quiz)?.session ?: return
        if (session.busy) return
        val feedback = session.feedback ?: return
        if (feedback.outcome == Outcome.Clarify) return
        advance(if (feedback.outcome == Outcome.Incorrect && !session.isExam) remediate(session) else session)
    }

    fun skip() {
        val session = (page.value as? Page.Quiz)?.session ?: return
        if (!session.busy && session.feedback?.outcome !in setOf(Outcome.Correct, Outcome.Incorrect)) {
            advance(session)
        }
    }

    private fun remediate(session: Session): Session {
        if (session.question.id in session.followUps) return session
        val queued = session.questions.map(Question::prompt).toSet()
        val alreadyShown = session.questions.take(session.index + 1).map(Question::id).toSet() + session.followUps
        val candidate = variants(session.question.lessonId, Random.Default, 10)
            .firstOrNull { it.prompt !in queued }
            ?: QuestionSelection.followUp(session.question, Curriculum.questions, alreadyShown)
            ?: return session
        val queue = session.questions.take(session.index + 1) + candidate +
            session.questions.drop(session.index + 1).filterNot { it.id == candidate.id }
        return session.copy(questions = queue, followUps = session.followUps + candidate.id)
    }

    private fun advance(session: Session) {
        var queue = session.questions
        val settings = session.settings
        if (session.index == queue.lastIndex && session.isInfinite && settings != null) {
            val recent = queue.takeLast(12)
            val pool = Curriculum.questions + variants(settings.lessonId, Random.Default, 5)
            val candidates = pool.distinctBy(Question::prompt)
            val recentPrompts = recent.map(Question::prompt).toSet()
            val nextSettings = settings.copy(count = 1)
            val now = System.currentTimeMillis()
            val next = QuestionSelection.select(candidates.filterNot { it.prompt in recentPrompts }, nextSettings,
                attempts.value, progress.value.mastery, now = now).firstOrNull()
                ?: QuestionSelection.select(candidates.filterNot { it.prompt == session.question.prompt },
                    nextSettings, attempts.value, progress.value.mastery, now = now).firstOrNull()
            if (next != null) queue = queue + next
            else {
                finish(session.copy(notice = "Estes filtros só encontraram uma pergunta diferente."))
                return
            }
        }
        if (session.index + 1 < queue.size) {
            page.value = Page.Quiz(session.copy(questions = queue, index = session.index + 1, feedback = null,
                shownAt = SystemClock.elapsedRealtime(), token = UUID.randomUUID().toString(), notice = null))
        } else finish(session)
    }

    fun finishInfinite() {
        val session = (page.value as? Page.Quiz)?.session ?: return
        if (session.isInfinite && !session.busy) finish(session)
    }

    private fun finish(session: Session) {
        val lesson = session.lesson
        val distinctCorrect = session.checks.asSequence().filter { it.result.outcome == Outcome.Correct }
            .distinctBy { it.question.prompt }.take(2).count()
        if (lesson == null || distinctCorrect < 2) {
            page.value = Page.Results(session, false)
            return
        }
        page.value = Page.Quiz(session.copy(busy = true))
        viewModelScope.launch {
            try {
                repository.complete(lesson.id, System.currentTimeMillis())
                page.value = Page.Results(session, true)
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                page.value = Page.Quiz(session.copy(busy = false,
                    notice = "Não foi possível salvar a conclusão. Tente continuar novamente."))
            }
        }
    }

    fun tutorFor(lessonId: String, action: TutorAction? = null) {
        val lesson = Curriculum.byId[lessonId] ?: return
        switchTab(Tab.Tutor)
        tutorLesson.value = lesson.id
        tutorReply.value = null
        tutorRevision++
        action?.let(::tutorAction)
    }

    fun tutorAction(action: TutorAction) {
        val lesson = Curriculum.byId.getValue(tutorLesson.value)
        val revision = ++tutorRevision
        if (action == TutorAction.Practice) {
            val recent = attempts.value.takeLast(8).map(Attempt::questionId).toSet()
            val fresh = variants(lesson.id, Random.Default, 5).firstOrNull { it.id !in recent }
                ?: lesson.questions.firstOrNull { it.id !in recent } ?: lesson.questions.random()
            page.value = Page.Quiz(Session(listOf(fresh), Tab.Tutor))
            return
        }
        viewModelScope.launch {
            val last = attempts.value.lastOrNull { it.lessonId == lesson.id && !it.correct }
            val question = last?.let { authoredQuestions[it.questionId] ?: Practice.restore(it.questionId) }
            val answer = last?.answer
            val feedback = if (question != null && answer != null) Corrections.grade(question, answer) else null
            val reply = tutor.explain(TutorRequest(lesson, action, feedback))
            if (revision == tutorRevision) tutorReply.value = reply
        }
    }

    fun theme(mode: ThemeMode) = savePreferences { it.copy(theme = mode.name) }
    fun goal(xp: Int) = savePreferences { it.copy(dailyGoal = xp.coerceIn(10, 60)) }

    fun transfer(uri: Uri, importing: Boolean) {
        if (transferring.value) return
        transferring.value = true
        transferNotice.value = "Processando o arquivo…"
        viewModelScope.launch {
            try {
                transferNotice.value = if (importing) {
                    val added = backup.importFrom(uri)
                    if (added == 0) "O progresso deste arquivo já está neste aparelho."
                    else "Progresso mesclado. Registros adicionados: $added."
                } else {
                    backup.exportTo(uri)
                    "Arquivo de progresso salvo."
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                transferNotice.value = if (importing) "O arquivo é inválido ou não pôde ser lido."
                else "Não foi possível salvar o arquivo de progresso."
            } finally {
                transferring.value = false
            }
        }
    }

    private fun savePreferences(update: (StudyPreferences) -> StudyPreferences) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                repository.updatePreferences(update)
                saveNotice.value = null
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                saveNotice.value = "Não foi possível salvar esta preferência."
            }
        }
    }
}
