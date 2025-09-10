package org.example.feature.reminders

import org.example.core.models.AppNotification
import org.example.core.models.NotificationChannel
import org.example.core.models.Reminder
import org.example.core.repo.NotificationsManager
import org.example.core.repo.RemindersRepository
import org.example.core.repo.RepoResult
import java.time.LocalDateTime

// PUBLIC_INTERFACE
/**
 * Manages reminder CRUD and delegates scheduling to NotificationsManager.
 */
class ReminderService(
    private val repo: RemindersRepository,
    private val notifications: NotificationsManager
) {

    // PUBLIC_INTERFACE
    /** Ensure OS channels exist before scheduling. */
    fun ensureChannels() = notifications.ensureChannels()

    // PUBLIC_INTERFACE
    /** Retrieve all reminders. */
    suspend fun list(): RepoResult<List<Reminder>> = repo.list()

    // PUBLIC_INTERFACE
    /** Create or update a reminder and schedule/cancel notification accordingly. */
    suspend fun upsert(reminder: Reminder): RepoResult<Unit> {
        val res = repo.upsert(reminder)
        if (res is RepoResult.Success) {
            if (reminder.enabled) {
                scheduleForReminder(reminder)
            } else {
                notifications.cancel(reminder.id)
            }
        }
        return res
    }

    // PUBLIC_INTERFACE
    /** Delete reminder and cancel its scheduled notification. */
    suspend fun delete(reminderId: String): RepoResult<Unit> {
        val res = repo.delete(reminderId)
        if (res is RepoResult.Success) {
            notifications.cancel(reminderId)
        }
        return res
    }

    private fun scheduleForReminder(reminder: Reminder) {
        // For now, schedule a simple next-run notification (actual OS scheduling to be wired later)
        val n = AppNotification(
            id = reminder.id,
            title = "Reminder",
            body = when (reminder.type) {
                org.example.core.models.ReminderType.WAKE_UP -> "Time to wake up!"
                org.example.core.models.ReminderType.WATER -> "Hydration time — drink some water."
                org.example.core.models.ReminderType.MEAL_BREAKFAST -> "Breakfast time!"
                org.example.core.models.ReminderType.MEAL_LUNCH -> "Lunch time!"
                org.example.core.models.ReminderType.MEAL_DINNER -> "Dinner time!"
                org.example.core.models.ReminderType.EXERCISE -> "Time for exercise!"
            },
            channel = NotificationChannel.REMINDERS,
            scheduledAt = LocalDateTime.now().plusMinutes(1) // placeholder
        )
        notifications.schedule(n)
    }
}
