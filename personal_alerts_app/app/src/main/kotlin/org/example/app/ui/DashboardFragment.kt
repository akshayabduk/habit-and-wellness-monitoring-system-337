package org.example.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.example.app.R
import org.example.core.models.BmiRecord
import org.example.core.repo.BmiRepository
import org.example.core.repo.RepoResult
import org.example.feature.bmi.BmiService
import java.time.LocalDate

/**
 * PUBLIC_INTERFACE
 * Simple dashboard to display latest BMI and placeholder progress values.
 */
class DashboardFragment : Fragment() {

    private lateinit var bmiService: BmiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Wire a trivial in-memory repo for now to allow UI to run.
        bmiService = BmiService(object : BmiRepository {
            private var latest: BmiRecord? = null
            override suspend fun latest(): RepoResult<BmiRecord?> = RepoResult.Success(latest)
            override suspend fun addRecord(record: BmiRecord): RepoResult<Unit> {
                latest = record
                return RepoResult.Success(Unit)
            }
            override suspend fun records(from: LocalDate, to: LocalDate): RepoResult<List<BmiRecord>> =
                RepoResult.Success(emptyList())
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bmiText = view.findViewById<TextView>(R.id.dashboard_bmi_value)
        val hydration = view.findViewById<ProgressBar>(R.id.dashboard_hydration_progress)
        val exercise = view.findViewById<ProgressBar>(R.id.dashboard_exercise_progress)

        // Demo values (would come from repositories/managers)
        bmiText.text = "--"
        hydration.progress = 40
        exercise.progress = 20
    }
}
