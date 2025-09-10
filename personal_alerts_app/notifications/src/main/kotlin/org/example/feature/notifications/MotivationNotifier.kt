package org.example.feature.notifications

import org.example.core.models.AppNotification
import org.example.core.models.NotificationChannel
import org.example.core.repo.NotificationsManager
import java.time.LocalDateTime
import java.util.UUID

// PUBLIC_INTERFACE
/**
 * Provides helper methods to send motivational notifications.
 */
class MotivationNotifier(
    private val notifications: NotificationsManager
) {

    // PUBLIC_INTERFACE
    /** Send an immediate motivational notification. */
    fun sendNow(title: String = "Keep Going!", body: String): String {
        val id = UUID.randomUUID().toString()
        val notif = AppNotification(
            id = id,
            title = title,
            body = body,
            channel = NotificationChannel.MOTIVATION,
            scheduledAt = null
        )
        notifications.schedule(notif)
        return id
    }

    // PUBLIC_INTERFACE
    /** Schedule a motivational notification for a later time. */
    fun schedule(title: String = "Stay Motivated", body: String, at: LocalDateTime): String {
        val id = UUID.randomUUID().toString()
        val notif = AppNotification(
            id = id,
            title = title,
            body = body,
            channel = NotificationChannel.MOTIVATION,
            scheduledAt = at
        )
        notifications.schedule(notif)
        return id
    }

    // PUBLIC_INTERFACE
    /** Cancel a previously scheduled motivational notification by id. */
    fun cancel(id: String) = notifications.cancel(id)
}
