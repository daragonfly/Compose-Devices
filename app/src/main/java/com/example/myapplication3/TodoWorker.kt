package com.example.myapplication3

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class TodoWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        return try {
            val todoItemsData = inputData.getStringArray("todoItems") ?: emptyArray()

            val file = File(applicationContext.filesDir, "todoItems.dat")
            ObjectOutputStream(file.outputStream()).use { outputStream ->
                outputStream.writeObject(todoItemsData)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    companion object {
        fun loadTodoItems(context: Context): List<TodoItem> {
            val file = File(context.filesDir, "todoItems.dat")
            if (!file.exists()) return emptyList()

            return try {
                ObjectInputStream(FileInputStream(file)).use { inputStream ->
                    val todoItemsData = inputStream.readObject() as Array<String?>
                    todoItemsData.mapNotNull { title -> TodoItem(title ?: "", "No description") }
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
