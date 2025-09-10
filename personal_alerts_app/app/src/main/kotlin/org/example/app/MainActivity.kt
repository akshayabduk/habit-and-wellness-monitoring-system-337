package org.example.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.example.app.ui.AiInsightsFragment
import org.example.app.ui.BmiFragment
import org.example.app.ui.DashboardFragment
import org.example.app.ui.ProfileFragment
import org.example.app.ui.RemindersFragment

/**
 * App entrypoint Activity (traditional Views).
 *
 * PUBLIC_INTERFACE
 * Hosts bottom navigation and swaps fragments for each primary feature.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottom = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottom.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> switchTo(DashboardFragment())
                R.id.nav_reminders -> switchTo(RemindersFragment())
                R.id.nav_bmi -> switchTo(BmiFragment())
                R.id.nav_ai -> switchTo(AiInsightsFragment())
                R.id.nav_profile -> switchTo(ProfileFragment())
                else -> {
                    Toast.makeText(this, "Unknown tab", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }

        if (savedInstanceState == null) {
            bottom.selectedItemId = R.id.nav_dashboard
        }
    }

    private fun switchTo(fragment: Fragment): Boolean {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }
}
