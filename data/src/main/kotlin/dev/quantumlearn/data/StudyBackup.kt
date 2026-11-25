package dev.quantumlearn.data

import android.content.ContentResolver
import android.net.Uri
import dev.quantumlearn.core.Curriculum
import dev.quantumlearn.core.Difficulty
import dev.quantumlearn.core.ErrorKind
import dev.quantumlearn.core.Learning
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

internal data class StudyArchive(val attempts: List<AttemptRow>, val completions: List<CompletionRow>)

class StudyBackup(private val repository: StudyRepository, private val resolver: ContentResolver) {
    suspend fun exportTo(uri: Uri) = withContext(Dispatchers.IO) {
        val archive = repository.snapshot()
        val document = JSONObject()
            .put("format", "quantum-learn")
            .put("version", 1)
            .put("attempts", JSONArray().apply { archive.attempts.forEach { put(encode(it)) } })
            .put("completions", JSONArray().apply {
                archive.completions.forEach { completion ->
                    put(JSONObject().put("lesson", completion.lessonId).put("at", completion.atMillis))
                }
            })
        val bytes = document.toString().toByteArray(Charsets.UTF_8)
        require(bytes.size <= maxBytes)
        requireNotNull(resolver.openOutputStream(uri, "wt")).use { it.write(bytes) }
    }

    suspend fun importFrom(uri: Uri): Int = withContext(Dispatchers.IO) {
        val text = requireNotNull(resolver.openInputStream(uri)).use { source ->
            val buffer = ByteArray(8_192)
            val contents = ByteArrayOutputStream()
            while (true) {
                val count = source.read(buffer)
                if (count < 0) break
                require(contents.size() + count <= maxBytes)
                contents.write(buffer, 0, count)
            }
            contents.toByteArray().toString(Charsets.UTF_8)
        }
        val root = JSONObject(text)
        require(root.getString("format") == "quantum-learn" && root.getInt("version") == 1)
        val known = Curriculum.byId.keys
        val attempts = root.getJSONArray("attempts").objects { readAttempt(it, known) }
        val completions = root.getJSONArray("completions").objects {
            val lesson = it.getString("lesson")
            val at = it.getLong("at")
            require(lesson in known && at > 0)
            CompletionRow(lesson, at)
        }
        repository.merge(StudyArchive(attempts, completions))
    }

    private fun encode(row: AttemptRow): JSONObject = JSONObject()
        .put("id", row.submissionId).put("question", row.questionId).put("lesson", row.lessonId)
        .put("correct", row.correct).put("difficulty", row.difficulty)
        .put("error", row.error ?: JSONObject.NULL).put("confusion", row.confusion ?: JSONObject.NULL)
        .put("at", row.atMillis).put("duration", row.durationMillis).put("xp", row.xp)
        .put("choice", row.selectedIndex ?: JSONObject.NULL).put("answer", row.writtenAnswer ?: JSONObject.NULL)

    private fun readAttempt(value: JSONObject, known: Set<String>): AttemptRow {
        val id = value.getString("id")
        val question = value.getString("question")
        val lesson = value.getString("lesson")
        val correct = value.getBoolean("correct")
        val difficulty = Difficulty.valueOf(value.getString("difficulty"))
        val error = value.optionalText("error")?.let(ErrorKind::valueOf)
        val confusion = value.optionalText("confusion")
        val at = value.getLong("at")
        val duration = value.getLong("duration")
        val xp = value.getInt("xp")
        val choice = if (value.isNull("choice")) null else value.getInt("choice")
        val answer = value.optionalText("answer")
        require(id.length in 1..64 && question.length in 1..100 && lesson in known && question.startsWith("$lesson-"))
        require(at > 0 && duration in 0L..600_000L && xp in 0..Learning.xp(correct, difficulty))
        require((confusion?.length ?: 0) <= 300 && (answer?.length ?: 0) <= 160 && (choice == null || choice in 0..20))
        return AttemptRow(id, question, lesson, correct, difficulty.name, error?.name, confusion, at, duration, xp, choice, answer)
    }

    private fun JSONObject.optionalText(key: String): String? = if (isNull(key)) null else getString(key)

    private fun <T> JSONArray.objects(transform: (JSONObject) -> T) = List(length()) { transform(getJSONObject(it)) }

    private companion object {
        const val maxBytes = 12 * 1024 * 1024
    }
}
