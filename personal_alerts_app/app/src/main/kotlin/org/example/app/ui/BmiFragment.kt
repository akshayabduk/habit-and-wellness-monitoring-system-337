package org.example.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
 * BMI calculator screen: inputs weight/height and shows BMI result.
 */
class BmiFragment : Fragment() {

    private lateinit var service: BmiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service = BmiService(object : BmiRepository {
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
        return inflater.inflate(R.layout.fragment_bmi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val weight = view.findViewById<EditText>(R.id.input_weight)
        val height = view.findViewById<EditText>(R.id.input_height)
        val calculate = view.findViewById<Button>(R.id.btn_calculate)
        val result = view.findViewById<TextView>(R.id.text_bmi_result)

        calculate.setOnClickListener {
            try {
                val w = weight.text.toString().toFloat()
                val h = height.text.toString().toFloat()
                val bmi = service.calculateBmi(w, h)
                result.text = "BMI: $bmi"
            } catch (t: Throwable) {
                result.text = "BMI: Error (${t.message})"
            }
        }
    }
}
