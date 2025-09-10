# Architecture Overview

This project is organized into feature-focused Android library modules with a thin `app` module that wires UI and platform specifics.

Modules:
- core: Shared models and repository/manager interfaces.
- profile: ProfileService for user profile CRUD and validation.
- reminders: ReminderService for reminders CRUD and scheduling (delegates to NotificationsManager).
- bmi: BmiService for BMI calculation and trend management.
- ai: AiSuggestionService for integrating OpenAI or local ML via AiSuggestionsRepository.
- notifications: MotivationNotifier and future Android Notification wiring.

Key Concepts:
- All public APIs are documented and marked with PUBLIC_INTERFACE in code.
- No environment variables are hard-coded; future API keys (e.g., OpenAI) must come from .env or secure storage (to be added later).
- UI remains in the `app` module, using traditional Android Views (no Compose). Feature modules are platform-agnostic (no Android UI dependencies).

Next Steps:
- Implement concrete repository classes (e.g., in `app` or separate `data-*` module) for persistence (Room/DataStore).
- Add Android NotificationsManager implementation using NotificationManager, AlarmManager/WorkManager.
- Add OpenAI integration in `ai` with a provider implementation and secure key handling.
- Build screens for Profile, Reminders, BMI Trend, AI Suggestions, Settings with bottom navigation.
