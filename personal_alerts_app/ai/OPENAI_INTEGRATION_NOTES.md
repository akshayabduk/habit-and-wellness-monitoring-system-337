# OpenAI Integration Notes

Plan:
- Add an implementation of `AiSuggestionsRepository` that calls OpenAI APIs.
- Retrieve API key from environment or secure storage (do not hardcode).
- Provide a local ML fallback implementation for offline/basic hints.

Interfaces to implement:
- `AiSuggestionsRepository.latest(limit: Int)`
- `AiSuggestionsRepository.requestSuggestion(topic: SuggestionCategory)`

Security:
- Use an .env.example in project root to declare `OPENAI_API_KEY`.
- On Android, store runtime key in EncryptedSharedPreferences or fetch from a backend.
