package com.example.myapplication3

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication3.ui.theme.TodoListAppTheme
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListAppTheme {
                val navController = rememberNavController()

                // Charger les tâches sauvegardées
                val savedTodoItems = TodoWorker.loadTodoItems(this)
                val todoItems = remember { mutableStateListOf<TodoItem>().apply { addAll(savedTodoItems) } }

                // Stockage de l'URI de l'image de fond à un niveau supérieur
                val backgroundImageUri = remember { mutableStateOf<String?>(null) }

                Surface(
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        navController = navController,
                        todoItems = todoItems,
                        backgroundImageUri = backgroundImageUri // Transmettre l'URI ici
                    )
                }
            }
        }
    }
}


@Composable
fun AppNavigation(
    navController: NavHostController,
    todoItems: MutableList<TodoItem>,
    backgroundImageUri: MutableState<String?> // Ajouter cet argument
) {
    NavHost(navController = navController, startDestination = "todoList") {
        composable("todoList") {
            TodoListScreen(
                navController = navController,
                todoItems = todoItems,
                onAddTaskClick = {
                    navController.navigate("addTask")
                },
                backgroundImageUri = backgroundImageUri // Transmettre l'URI ici
            )
        }
        composable("addTask") {
            AddTaskScreen(
                navController = navController,
                onTaskAdded = { newTask ->
                    todoItems.add(newTask)
                    saveTodoItemsWithWorkManager(navController.context, todoItems) // Sauvegarder ici
                    navController.navigateUp()
                }
            )
        }
    }
}



// Fonction pour sauvegarder les tâches via WorkManager
fun saveTodoItemsWithWorkManager(context: Context, todoItems: List<TodoItem>) {
    // Convertir la liste de tâches en tableau nullable
    val todoItemsData = todoItems.map { it.title as String? }.toTypedArray()

    // Créer une requête de travail unique
    val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
        .setInputData(workDataOf("todoItems" to todoItemsData))
        .build()

    // Planifier la requête avec WorkManager
    WorkManager.getInstance(context).enqueue(workRequest)
}

