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
