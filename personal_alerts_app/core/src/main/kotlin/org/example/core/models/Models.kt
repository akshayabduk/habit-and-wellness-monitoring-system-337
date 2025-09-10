package org.example.core.models

import java.time.LocalDate
import java.time.LocalDateTime

// PUBLIC_INTERFACE
/**
 * Represents a user's profile and basic health/goal data.
 */
data class UserProfile(
    val id: String,
    val age: Int,
    val heightCm: Float,
    val weightKg: Float,
    val goals: List<String>,
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

// PUBLIC_INTERFACE
/**
 * Encapsulates reminder configuration for a specific habit.
 */
data class Reminder(
    val id: String,
    val type: ReminderType,
    val time: String, // "HH:mm" in device local time
    val enabled: Boolean,
    val daysOfWeek: Set<DayOfWeek>, // e.g., MON..SUN
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

// PUBLIC_INTERFACE
/**
 * Types of reminders supported by the app.
 */
enum class ReminderType {
    WAKE_UP, WATER, MEAL_BREAKFAST, MEAL_LUNCH, MEAL_DINNER, EXERCISE
}

// PUBLIC_INTERFACE
/**
 * Day of week abstraction to avoid Android enum dependencies in core.
 */
enum class DayOfWeek { MON, TUE, WED, THU, FRI, SAT, SUN }

// PUBLIC_INTERFACE
/**
 * A BMI record point for trend analysis.
 */
data class BmiRecord(
    val date: LocalDate,
    val weightKg: Float,
    val heightCm: Float,
    val bmi: Float
)

// PUBLIC_INTERFACE
/**
 * An AI suggestion for motivation or habit optimization.
 */
data class AiSuggestion(
    val id: String,
    val createdAt: LocalDateTime,
    val message: String,
    val category: SuggestionCategory
)

// PUBLIC_INTERFACE
/**
 * Types of AI suggestions to categorize content.
 */
enum class SuggestionCategory { MOTIVATION, HYDRATION, NUTRITION, SLEEP, EXERCISE }

// PUBLIC_INTERFACE
/**
 * Notification payload for motivational or reminder notifications.
 */
data class AppNotification(
    val id: String,
    val title: String,
    val body: String,
    val channel: NotificationChannel,
    val scheduledAt: LocalDateTime?
)

// PUBLIC_INTERFACE
/**
 * Notification channels used by the system.
 */
enum class NotificationChannel { REMINDERS, MOTIVATION }
