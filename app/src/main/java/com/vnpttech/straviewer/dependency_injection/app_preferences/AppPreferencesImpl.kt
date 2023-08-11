package com.vnpttech.straviewer.dependency_injection.app_preferences

import android.content.Context
import android.content.SharedPreferences
import com.vnpttech.straviewer.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(@ApplicationContext context: Context) : AppPreferences {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    private val preferencesEditor = preferences.edit()

    override fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    override fun putString(key: String, value: String?): Boolean {
        preferencesEditor.putString(key, value)
        return preferencesEditor.commit()
    }

    override fun getInt(key: String): Int {
        return preferences.getInt(key, -1)
    }

    override fun putInt(key: String, value: Int): Boolean {
        preferencesEditor.putInt(key, value)
        return preferencesEditor.commit()
    }

    override fun getLong(key: String): Long {
        return preferences.getLong(key, -1)
    }

    override fun putLong(key: String, value: Long): Boolean {
        preferencesEditor.putLong(key, value)
        return preferencesEditor.commit()
    }

    override fun clearKeys(keys: List<String>): Boolean {
        for (key in keys) {
            preferencesEditor.remove(key)
        }
        return preferencesEditor.commit()
    }
}