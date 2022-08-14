package dev.costas.vertext

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import dev.costas.vertext.fragments.MainFragment

class MainActivity : AppCompatActivity(R.layout.activity_main), FragmentChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        Log.d("Create", "mode: " + sharedPreferences.getString("theme", "NONE"))
        if (savedInstanceState == null) {
            val nightMode =
                when (sharedPreferences.getString("theme", "MODE_NIGHT_FOLLOW_SYSTEM")) {
                    "MODE_NIGHT_FOLLOW_SYSTEM" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    "MODE_NIGHT_NO" -> AppCompatDelegate.MODE_NIGHT_NO
                    "MODE_NIGHT_YES" -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> {
                        Log.e(
                            "activityOnCreate",
                            "unexpected mode option " + sharedPreferences.getString("theme", "")
                        )
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                }
            AppCompatDelegate.setDefaultNightMode(nightMode)
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, MainFragment::class.java, savedInstanceState)
                .commit()
        }
    }

    override fun setFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment!!, fragment.toString())
            .addToBackStack(fragment.toString())
            .commit()
    }
}