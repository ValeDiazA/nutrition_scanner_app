package com.nutritionscanner.data.auth

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private const val PREF_NAME = "auth_preferences"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_EMAIL = "email"
        private const val KEY_NAME = "name"
        private const val KEY_LAST_LOGIN = "last_login"
        private const val KEY_REMEMBER_ME = "remember_me"
    }
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    fun saveUserSession(userId: String, email: String, name: String, rememberMe: Boolean = true) {
        prefs.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_EMAIL, email)
            putString(KEY_NAME, name)
            putBoolean(KEY_IS_LOGGED_IN, true)
            putBoolean(KEY_REMEMBER_ME, rememberMe)
            putLong(KEY_LAST_LOGIN, System.currentTimeMillis())
            apply()
        }
    }
    
    fun clearUserSession() {
        prefs.edit().apply {
            remove(KEY_USER_ID)
            remove(KEY_EMAIL)
            remove(KEY_NAME)
            putBoolean(KEY_IS_LOGGED_IN, false)
            remove(KEY_REMEMBER_ME)
            remove(KEY_LAST_LOGIN)
            apply()
        }
    }
    
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    
    fun getCurrentUserId(): String? = prefs.getString(KEY_USER_ID, null)
    
    fun getCurrentUserEmail(): String? = prefs.getString(KEY_EMAIL, null)
    
    fun getCurrentUserName(): String? = prefs.getString(KEY_NAME, null)
    
    fun shouldRememberUser(): Boolean = prefs.getBoolean(KEY_REMEMBER_ME, false)
    
    fun getLastLoginTime(): Long = prefs.getLong(KEY_LAST_LOGIN, 0L)
    
    fun updateLoginTime() {
        prefs.edit().putLong(KEY_LAST_LOGIN, System.currentTimeMillis()).apply()
    }
}