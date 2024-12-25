package com.example.tbsewaku.data.preferences

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // Menyimpan data login
    fun saveLoginData(token: String, userId: Int, username: String) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.putInt("user_id", userId)
        editor.putString("username", username)
        editor.apply()
    }

    // Mendapatkan token yang disimpan
    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    // Menghapus data login (logout)
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
