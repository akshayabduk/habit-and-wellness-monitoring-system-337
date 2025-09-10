package org.example.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.core.models.UserProfile
import org.example.core.repo.RepoResult
import org.example.core.repo.UserProfileRepository
import org.example.feature.profile.ProfileService
import java.time.LocalDateTime

/**
 * PUBLIC_INTERFACE
 * Profile screen with simple CRUD using an in-memory repository.
 */
class ProfileFragment : Fragment() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var service: ProfileService
    private var cached: UserProfile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = object : UserProfileRepository {
            override suspend fun getProfile(): RepoResult<UserProfile?> = RepoResult.Success(cached)
            override suspend fun saveProfile(profile: UserProfile): RepoResult<Unit> {
                cached = profile.copy(updatedAt = LocalDateTime.now())
                return RepoResult.Success(Unit)
            }
        }
        service = ProfileService(repo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val age = view.findViewById<EditText>(R.id.input_age)
        val height = view.findViewById<EditText>(R.id.input_height_cm)
        val weight = view.findViewById<EditText>(R.id.input_weight_kg)
        val goals = view.findViewById<EditText>(R.id.input_goals)
        val save = view.findViewById<Button>(R.id.btn_save_profile)

        save.setOnClickListener {
            scope.launch {
                val a = age.text.toString().toIntOrNull() ?: 0
                val h = height.text.toString().toFloatOrNull() ?: 0f
                val w = weight.text.toString().toFloatOrNull() ?: 0f
                val g = goals.text.toString().split(",").map { it.trim() }.filter { it.isNotEmpty() }
                when (val res = service.saveProfile(a, h, w, g, cached?.id)) {
                    is RepoResult.Success -> Toast.makeText(requireContext(), "Profile saved", Toast.LENGTH_SHORT).show()
                    is RepoResult.Error -> Toast.makeText(requireContext(), "Error: ${res.throwable.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
