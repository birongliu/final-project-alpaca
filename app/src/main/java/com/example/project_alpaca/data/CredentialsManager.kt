package com.example.project_alpaca.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class CredentialsManager(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPreferences = EncryptedSharedPreferences.create(
        "secret_user_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveCredentials(username: String, password: String) {
        with(sharedPreferences.edit()) {
            // In a real production app, you should hash and salt the password before storing it.
            putString("username", username)
            putString("password", password)
            apply()
        }
    }

    fun areCredentialsStored(): Boolean {
        return sharedPreferences.contains("username")
    }

    fun checkCredentials(username: String, password: String): Boolean {
        val savedUsername = sharedPreferences.getString("username", null)
        val savedPassword = sharedPreferences.getString("password", null)
        return savedUsername == username && savedPassword == password
    }
} 