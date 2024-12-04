package com.example.myapplication3

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

object WorkManagerUtils {
    fun saveTodoItems(context: Context, todoItems: List<TodoItem>) {
        val todoItems = todoItems.map { it.title as String? }.toTypedArray()

        val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
            .setInputData(workDataOf("todoItems" to todoItems))
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)

    }
}
