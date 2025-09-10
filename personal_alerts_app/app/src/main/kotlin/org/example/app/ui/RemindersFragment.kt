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
import org.example.core.models.*
import org.example.core.repo.NotificationsManager
import org.example.core.repo.RemindersRepository
import org.example.core.repo.RepoResult
import org.example.feature.reminders.ReminderService
import java.time.LocalDateTime
import java.util.UUID

/**
 * PUBLIC_INTERFACE
 * Allows creating and listing reminders with a basic UI, using mock repos for now.
 */
class RemindersFragment : Fragment() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private lateinit var service: ReminderService
    private val inMemory = mutableListOf<Reminder>()
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = object : RemindersRepository {
            override suspend fun list(): RepoResult<List<Reminder>> = RepoResult.Success(inMemory.toList())
            override suspend fun upsert(reminder: Reminder): RepoResult<Unit> {
                inMemory.removeAll { it.id == reminder.id }
                inMemory.add(reminder)
                return RepoResult.Success(Unit)
            }
            override suspend fun delete(reminderId: String): RepoResult<Unit> {
                inMemory.removeAll { it.id == reminderId }
                return RepoResult.Success(Unit)
            }
        }
        val notifications = object : NotificationsManager {
            override fun ensureChannels() {}
            override fun schedule(notification: AppNotification) {}
            override fun cancel(notificationId: String) {}
        }
        service = ReminderService(repo, notifications)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_reminders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val time = view.findViewById<EditText>(R.id.input_time)
        val type = view.findViewById<EditText>(R.id.input_type)
        val enabled = view.findViewById<CheckBox>(R.id.input_enabled)
        val save = view.findViewById<Button>(R.id.btn_save_reminder)
        listView = view.findViewById(R.id.list_reminders)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        listView.adapter = adapter

        save.setOnClickListener {
            val t = time.text?.toString()?.trim().orEmpty()
            val typeStr = type.text?.toString()?.trim().orEmpty()
            val rType = when (typeStr.lowercase()) {
                "wake", "wakeup" -> ReminderType.WAKE_UP
                "water", "hydration" -> ReminderType.WATER
                "breakfast" -> ReminderType.MEAL_BREAKFAST
                "lunch" -> ReminderType.MEAL_LUNCH
                "dinner" -> ReminderType.MEAL_DINNER
                "exercise", "workout" -> ReminderType.EXERCISE
                else -> ReminderType.WATER
            }
            val rem = Reminder(
                id = UUID.randomUUID().toString(),
                type = rType,
                time = if (t.isBlank()) "09:00" else t,
                enabled = enabled.isChecked,
                daysOfWeek = setOf(DayOfWeek.MON, DayOfWeek.TUE, DayOfWeek.WED, DayOfWeek.THU, DayOfWeek.FRI)
            )
            scope.launch {
                service.upsert(rem)
                refresh()
                Toast.makeText(requireContext(), "Reminder saved", Toast.LENGTH_SHORT).show()
            }
        }
        refresh()
    }

    private fun refresh() {
        scope.launch {
            when (val res = service.list()) {
                is RepoResult.Success -> {
                    adapter.clear()
                    adapter.addAll(res.value.map { "${it.type} @ ${it.time} ${if (it.enabled) "ON" else "OFF"}" })
                    adapter.notifyDataSetChanged()
                }
                is RepoResult.Error -> {
                    Toast.makeText(requireContext(), "Error: ${res.throwable.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
