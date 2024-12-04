package com.example.myapplication3

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class TodoWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        return try {
            // Récupérer les tâches à partir de l'inputData
            val todoItemsData = inputData.getStringArray("todoItems") ?: emptyArray()

            // Sauvegarde des tâches dans un fichier
            val file = File(applicationContext.filesDir, "todoItems.dat")
            ObjectOutputStream(FileOutputStream(file)).use { outputStream ->
                outputStream.writeObject(todoItemsData)
            }

            // Indiquer que le travail a été effectué avec succès
            Result.success()
        } catch (e: Exception) {
            // En cas d'échec, retourner un échec
            Result.failure()
        }
    }
}
