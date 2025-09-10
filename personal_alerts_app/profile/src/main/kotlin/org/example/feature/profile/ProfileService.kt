package org.example.feature.profile

import org.example.core.models.UserProfile
import org.example.core.repo.RepoResult
import org.example.core.repo.UserProfileRepository
import java.util.UUID

// PUBLIC_INTERFACE
/**
 * Provides profile read/write operations with light validation.
 */
class ProfileService(
    private val repository: UserProfileRepository
) {

    // PUBLIC_INTERFACE
    /** Get current profile, may return null when not set. */
    suspend fun getProfile(): RepoResult<UserProfile?> = repository.getProfile()

    // PUBLIC_INTERFACE
    /** Create or update a profile with validation rules. */
    suspend fun saveProfile(
        age: Int,
        heightCm: Float,
        weightKg: Float,
        goals: List<String>,
        id: String? = null
    ): RepoResult<Unit> {
        val validated = try {
            validate(age, heightCm, weightKg, goals)
        } catch (t: Throwable) {
            return RepoResult.Error(t)
        }
        val profile = UserProfile(
            id = id ?: UUID.randomUUID().toString(),
            age = validated.age,
            heightCm = validated.heightCm,
            weightKg = validated.weightKg,
            goals = validated.goals
        )
        return repository.saveProfile(profile)
    }

    private fun validate(age: Int, heightCm: Float, weightKg: Float, goals: List<String>): UserProfile {
        require(age in 1..120) { "Age must be between 1 and 120" }
        require(heightCm in 50f..280f) { "Height must be between 50cm and 280cm" }
        require(weightKg in 10f..400f) { "Weight must be between 10kg and 400kg" }
        val cleanGoals = goals.map { it.trim() }.filter { it.isNotEmpty() }.take(10)
        return UserProfile(id = "", age = age, heightCm = heightCm, weightKg = weightKg, goals = cleanGoals)
    }
}
