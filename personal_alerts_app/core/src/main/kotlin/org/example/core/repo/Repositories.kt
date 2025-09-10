package org.example.core.repo

import org.example.core.models.*
import java.time.LocalDate

// PUBLIC_INTERFACE
/**
 * Generic result wrapper for repository operations.
 */
sealed class RepoResult<out T> {
    data class Success<T>(val value: T): RepoResult<T>()
    data class Error(val throwable: Throwable): RepoResult<Nothing>()
}

// PUBLIC_INTERFACE
/**
 * Repository interface to manage user profile data.
 */
interface UserProfileRepository {
    suspend fun getProfile(): RepoResult<UserProfile?>
    suspend fun saveProfile(profile: UserProfile): RepoResult<Unit>
}

// PUBLIC_INTERFACE
/**
 * Repository interface for reminder CRUD and querying.
 */
interface RemindersRepository {
    suspend fun list(): RepoResult<List<Reminder>>
    suspend fun upsert(reminder: Reminder): RepoResult<Unit>
    suspend fun delete(reminderId: String): RepoResult<Unit>
}

// PUBLIC_INTERFACE
/**
 * Repository interface for BMI trend data.
 */
interface BmiRepository {
    suspend fun latest(): RepoResult<BmiRecord?>
    suspend fun addRecord(record: BmiRecord): RepoResult<Unit>
    suspend fun records(from: LocalDate, to: LocalDate): RepoResult<List<BmiRecord>>
}

// PUBLIC_INTERFACE
/**
 * Interface to obtain AI suggestions based on profile and recent activity.
 */
interface AiSuggestionsRepository {
    suspend fun latest(limit: Int = 10): RepoResult<List<AiSuggestion>>
    suspend fun requestSuggestion(topic: SuggestionCategory): RepoResult<AiSuggestion>
}

// PUBLIC_INTERFACE
/**
 * Abstraction for scheduling and sending app notifications.
 */
interface NotificationsManager {
    fun ensureChannels()
    fun schedule(notification: AppNotification)
    fun cancel(notificationId: String)
}
