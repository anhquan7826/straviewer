package com.anhquan.straviewer.dependency_injection.app_preferences

interface AppPreferences {
    fun getString(key: String): String?

    fun putString(key: String, value: String?): Boolean

    fun getInt(key: String): Int

    fun putInt(key: String, value: Int): Boolean

    fun getLong(key: String): Long

    fun putLong(key: String, value: Long): Boolean

    fun clearKeys(keys: List<String>): Boolean
}