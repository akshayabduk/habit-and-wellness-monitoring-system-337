package org.example.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.example.app.R
import org.example.core.models.AiSuggestion
import org.example.core.models.SuggestionCategory
import org.example.core.repo.AiSuggestionsRepository
import org.example.core.repo.RepoResult
import org.example.feature.ai.AiSuggestionService
import java.time.LocalDateTime
import java.util.UUID

/**
 * PUBLIC_INTERFACE
 * AI Insights chat-like stub UI. Sends a "prompt" and shows a stubbed AI reply.
 */
class AiInsightsFragment : Fragment() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var service: AiSuggestionService
    private lateinit var list: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = object : AiSuggestionsRepository {
            private val messages = mutableListOf<AiSuggestion>()
            override suspend fun latest(limit: Int): RepoResult<List<AiSuggestion>> =
                RepoResult.Success(messages.takeLast(limit))
            override suspend fun requestSuggestion(topic: SuggestionCategory): RepoResult<AiSuggestion> {
                val msg = when (topic) {
                    SuggestionCategory.MOTIVATION -> "Small steps add up. Try a 5-minute walk."
                    SuggestionCategory.HYDRATION -> "Drink a glass of water now to keep hydrated."
                    SuggestionCategory.NUTRITION -> "Add a serving of veggies to your next meal."
                    SuggestionCategory.SLEEP -> "Aim for a consistent bedtime this week."
                    SuggestionCategory.EXERCISE -> "Do 10 bodyweight squats to energize."
                }
                val s = AiSuggestion(UUID.randomUUID().toString(), LocalDateTime.now(), msg, topic)
                messages.add(s)
                return RepoResult.Success(s)
            }
        }
        service = AiSuggestionService(repo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_ai, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list = view.findViewById(R.id.list_messages)
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        list.adapter = adapter

        val input = view.findViewById<EditText>(R.id.input_prompt)
        val send = view.findViewById<Button>(R.id.btn_send)

        send.setOnClickListener {
            val prompt = input.text?.toString()?.trim().orEmpty()
            if (prompt.isEmpty()) return@setOnClickListener

            adapter.add("You: $prompt")
            adapter.notifyDataSetChanged()
            input.setText("")

            scope.launch {
                val topic = classify(prompt)
                when (val res = service.request(topic)) {
                    is RepoResult.Success -> {
                        adapter.add("AI: ${res.value.message}")
                        adapter.notifyDataSetChanged()
                    }
                    is RepoResult.Error -> {
                        adapter.add("AI: Error ${res.throwable.message}")
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun classify(prompt: String): SuggestionCategory {
        val p = prompt.lowercase()
        return when {
            listOf("water","hydrate","drink").any { p.contains(it) } -> SuggestionCategory.HYDRATION
            listOf("sleep","rest","bed").any { p.contains(it) } -> SuggestionCategory.SLEEP
            listOf("meal","food","diet","nutrition").any { p.contains(it) } -> SuggestionCategory.NUTRITION
            listOf("exercise","workout","run").any { p.contains(it) } -> SuggestionCategory.EXERCISE
            else -> SuggestionCategory.MOTIVATION
        }
    }
}
