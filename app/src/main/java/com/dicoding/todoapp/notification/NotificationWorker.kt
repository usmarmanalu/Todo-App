package com.dicoding.todoapp.notification

import android.annotation.*
import android.app.*
import android.content.*
import android.os.*
import androidx.core.app.*
import androidx.core.app.TaskStackBuilder
import androidx.work.*
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.*
import com.dicoding.todoapp.ui.detail.*
import com.dicoding.todoapp.utils.*
import kotlinx.coroutines.*

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val repository = TaskRepository.getInstance(applicationContext)

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            } else {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    override fun doWork(): Result {
        //TODO 14 : If notification preference on, get nearest active task from repository and show notification with pending intent
        val nearest = repository.getNearestActiveTask()
        showNotification(applicationContext, nearest)
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, task: Task) {
        val channelId = "channel_id"
        val message = context.getString(
            R.string.notify_content,
            DateConverter.convertMillisToString(task.dueDateMillis)
        )
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(task.title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentIntent(getPendingIntent(task))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                NOTIFICATION_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationBuilder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = notificationBuilder.build()
        notificationManagerCompat.notify(100, notification)

    }
}
