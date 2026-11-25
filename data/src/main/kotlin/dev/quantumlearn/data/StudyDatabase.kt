package dev.quantumlearn.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import androidx.room.withTransaction
import dev.quantumlearn.core.Answer
import dev.quantumlearn.core.Attempt
import dev.quantumlearn.core.Completion
import dev.quantumlearn.core.Difficulty
import dev.quantumlearn.core.ErrorKind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Entity(tableName = "attempts", indices = [Index("atMillis"), Index("questionId")])
data class AttemptRow(
    @PrimaryKey val submissionId: String,
    val questionId: String,
    val lessonId: String,
    val correct: Boolean,
    val difficulty: String,
    val error: String?,
    val confusion: String?,
    val atMillis: Long,
    val durationMillis: Long,
    val xp: Int,
    val selectedIndex: Int?,
    val writtenAnswer: String?
)

@Entity(tableName = "completions")
data class CompletionRow(@PrimaryKey val lessonId: String, val atMillis: Long)

enum class ThemeMode(val label: String) {
    System("Sistema"), Light("Claro"), Dark("Escuro")
}

@Entity(tableName = "preferences")
data class StudyPreferences(
    @PrimaryKey val id: Int = 0,
    val theme: String = ThemeMode.Dark.name,
    val dailyGoal: Int = 30
)

@Dao
interface StudyDao {
    @Query("SELECT * FROM attempts ORDER BY atMillis, rowid")
    fun attempts(): Flow<List<AttemptRow>>

    @Query("SELECT * FROM completions")
    fun completions(): Flow<List<CompletionRow>>

    @Query("SELECT * FROM attempts ORDER BY atMillis, rowid")
    suspend fun allAttempts(): List<AttemptRow>

    @Query("SELECT * FROM completions")
    suspend fun allCompletions(): List<CompletionRow>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun mergeAttempts(rows: List<AttemptRow>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun mergeCompletions(rows: List<CompletionRow>): List<Long>

    @Query("SELECT * FROM preferences WHERE id = 0")
    fun preferences(): Flow<StudyPreferences?>

    @Query("SELECT * FROM preferences WHERE id = 0")
    suspend fun currentPreferences(): StudyPreferences?

    @Query("SELECT xp FROM attempts WHERE submissionId = :id LIMIT 1")
    suspend fun recordedXp(id: String): Int?

    @Query("SELECT EXISTS(SELECT 1 FROM attempts WHERE questionId = :questionId AND xp > 0 AND atMillis >= :since)")
    suspend fun rewardedSince(questionId: String, since: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(row: AttemptRow)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun complete(row: CompletionRow)

    @Upsert
    suspend fun preferences(row: StudyPreferences)
}

@Database(
    entities = [AttemptRow::class, CompletionRow::class, StudyPreferences::class],
    version = 1,
    exportSchema = true
)
abstract class StudyDatabase : RoomDatabase() {
    abstract fun study(): StudyDao

    companion object {
        @Volatile private var instance: StudyDatabase? = null

        fun get(context: Context): StudyDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context.applicationContext, StudyDatabase::class.java,
                "quantum-learn.db").build().also { instance = it }
        }
    }
}

class StudyRepository(private val database: StudyDatabase) {
    private val dao = database.study()

    val attempts = dao.attempts().map { rows ->
        rows.map {
            Attempt(it.submissionId, it.questionId, it.lessonId, it.correct, Difficulty.valueOf(it.difficulty),
                it.error?.let(ErrorKind::valueOf), it.confusion, it.atMillis, it.durationMillis, it.xp,
                it.selectedIndex?.let { index -> Answer.Selection(index) }
                    ?: it.writtenAnswer?.let { text -> Answer.Text(text) })
        }
    }
    val completions = dao.completions().map { rows -> rows.map { Completion(it.lessonId, it.atMillis) } }
    val preferences = dao.preferences().map { it ?: StudyPreferences() }

    suspend fun record(attempt: Attempt, startOfDay: Long): Int = database.withTransaction {
        dao.recordedXp(attempt.submissionId)?.let { return@withTransaction it }
        val xp = if (attempt.xp > 0 && dao.rewardedSince(attempt.questionId, startOfDay)) 0 else attempt.xp
        dao.insert(AttemptRow(attempt.submissionId, attempt.questionId, attempt.lessonId, attempt.correct,
            attempt.difficulty.name, attempt.error?.name, attempt.confusion, attempt.atMillis,
            attempt.durationMillis, xp, (attempt.answer as? Answer.Selection)?.index,
            (attempt.answer as? Answer.Text)?.value))
        xp
    }

    suspend fun complete(lessonId: String, atMillis: Long) = dao.complete(CompletionRow(lessonId, atMillis))

    internal suspend fun snapshot(): StudyArchive = database.withTransaction {
        StudyArchive(dao.allAttempts(), dao.allCompletions())
    }

    internal suspend fun merge(archive: StudyArchive): Int = database.withTransaction {
        val answers = dao.mergeAttempts(archive.attempts).count { it >= 0 }
        val finishes = dao.mergeCompletions(archive.completions).count { it >= 0 }
        answers + finishes
    }

    suspend fun updatePreferences(update: (StudyPreferences) -> StudyPreferences) = database.withTransaction {
        dao.preferences(update(dao.currentPreferences() ?: StudyPreferences()))
    }
}
