package org.example.feature.ai

import org.example.core.models.AiSuggestion
import org.example.core.models.SuggestionCategory
import org.example.core.repo.AiSuggestionsRepository
import org.example.core.repo.RepoResult

// PUBLIC_INTERFACE
/**
 * High-level AI suggestion orchestrator. It wraps the repository which may call OpenAI or a local ML model.
 */
class AiSuggestionService(
    private val repo: AiSuggestionsRepository
) {

    // PUBLIC_INTERFACE
    /** Get recent suggestions. */
    suspend fun latest(limit: Int = 10): RepoResult<List<AiSuggestion>> = repo.latest(limit)

    // PUBLIC_INTERFACE
    /** Request a new suggestion for a given category. */
    suspend fun request(topic: SuggestionCategory): RepoResult<AiSuggestion> = repo.requestSuggestion(topic)
}
