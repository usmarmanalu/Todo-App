package com.dicoding.todoapp.data

import android.content.*
import androidx.room.*
import androidx.sqlite.db.*
import com.dicoding.todoapp.R
import kotlinx.coroutines.*
import org.json.*
import java.io.*

//TODO 3 : Define room database class and prepopulate database using JSON
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        @Volatile
        private var INSTANCE: TaskDatabase? = null
        fun getInstance(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { taskDatabase ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val task = taskDatabase.taskDao()
                                fillWithStartingData(context, task)
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        private fun fillWithStartingData(context: Context, dao: TaskDao) {
            try {
                val task = loadJsonArray(context)
                if (task != null) {
                    for (i in 0 until task.length()) {
                        val item = task.getJSONObject(i)
                        dao.insertAll(
                            Task(
                                item.getInt("id"),
                                item.getString("title"),
                                item.getString("description"),
                                item.getLong("dueDate"),
                                item.getBoolean("completed")
                            )
                        )
                    }
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }

        private fun loadJsonArray(context: Context): JSONArray? {
            val builder = StringBuilder()
            try {
                context.resources.openRawResource(R.raw.task).use { `in` ->
                    BufferedReader(InputStreamReader(`in`)).use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            builder.append(line)
                        }
                    }
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray("tasks")
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
            return null
        }
    }
}
