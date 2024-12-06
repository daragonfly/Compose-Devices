package com.example.myapplication3

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import com.google.gson.Gson

class TodoWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        return try {

            val todoItemsJson = inputData.getString("todoItems") ?: "[]"
            val gson = Gson()
            val todoItems: List<TodoItem> = gson.fromJson(todoItemsJson, Array<TodoItem>::class.java).toList()
            val file = File(applicationContext.filesDir, "todoItems.dat")
            ObjectOutputStream(file.outputStream()).use { outputStream ->
                outputStream.writeObject(todoItems)
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
                    val todoItems = inputStream.readObject() as List<TodoItem>
                    todoItems
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}

