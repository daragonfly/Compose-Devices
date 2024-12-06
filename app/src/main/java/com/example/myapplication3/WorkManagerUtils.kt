package com.example.myapplication3

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import android.content.SharedPreferences


private const val PREFS_NAME = "todo_app_prefs"
private const val KEY_BACKGROUND_URI = "background_uri"

// Sauvegarde l'URI de l'image de fond dans SharedPreferences
fun saveBackgroundUri(context: Context, uri: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().putString(KEY_BACKGROUND_URI, uri).apply()
}

// Charge l'URI de l'image de fond depuis SharedPreferences
fun loadBackgroundUri(context: Context): String? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getString(KEY_BACKGROUND_URI, null)
}

