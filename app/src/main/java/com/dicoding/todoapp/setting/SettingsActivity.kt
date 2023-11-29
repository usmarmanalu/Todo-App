package com.dicoding.todoapp.setting

import android.os.*
import androidx.appcompat.app.*
import androidx.preference.*
import androidx.work.*
import com.dicoding.todoapp.R
import com.dicoding.todoapp.notification.*
import java.util.concurrent.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification =
                findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { _, newValue ->
                val channelName = getString(R.string.notify_channel_name)

                val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                    1, TimeUnit.DAYS
                ).addTag(channelName).build()

                val workManager = WorkManager.getInstance(requireContext())

                if (newValue as Boolean) {
                    workManager.enqueueUniquePeriodicWork(
                        channelName, ExistingPeriodicWorkPolicy.UPDATE, notificationWorkRequest
                    )
                } else {
                    workManager.cancelAllWorkByTag(channelName)
                    workManager.pruneWork()
                }
                true
            }
        }
    }
}