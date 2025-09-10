package org.example.feature.bmi

import org.example.core.models.BmiRecord
import org.example.core.repo.BmiRepository
import org.example.core.repo.RepoResult
import java.time.LocalDate
import kotlin.math.pow

// PUBLIC_INTERFACE
/**
 * Handles BMI calculation and trend storage/retrieval.
 */
class BmiService(
    private val repo: BmiRepository
) {

    // PUBLIC_INTERFACE
    /** Calculate BMI from weight (kg) and height (cm). */
    fun calculateBmi(weightKg: Float, heightCm: Float): Float {
        require(weightKg in 10f..400f) { "Invalid weight" }
        require(heightCm in 50f..280f) { "Invalid height" }
        val m = heightCm / 100f
        return (weightKg / m.pow(2)).let { ((it * 10).toInt()) / 10f } // 1 decimal place
    }

    // PUBLIC_INTERFACE
    /** Save a BMI record for the given date (default: today). */
    suspend fun saveRecord(weightKg: Float, heightCm: Float, date: LocalDate = LocalDate.now()): RepoResult<Unit> {
        val bmi = calculateBmi(weightKg, heightCm)
        return repo.addRecord(BmiRecord(date = date, weightKg = weightKg, heightCm = heightCm, bmi = bmi))
    }

    // PUBLIC_INTERFACE
    /** Return BMI records in a date range. */
    suspend fun trend(from: LocalDate, to: LocalDate): RepoResult<List<BmiRecord>> = repo.records(from, to)

    // PUBLIC_INTERFACE
    /** Latest record. */
    suspend fun latest(): RepoResult<BmiRecord?> = repo.latest()
}
