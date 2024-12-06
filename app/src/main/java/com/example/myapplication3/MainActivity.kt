package com.example.myapplication3

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication3.ui.theme.TodoListAppTheme
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                println("Permission POST_NOTIFICATIONS accordée.")
            } else {
                println("Permission POST_NOTIFICATIONS refusée.")
            }
        }
        NotificationUtils.createNotificationChannel(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            TodoListAppTheme {
                val navController = rememberNavController()


                val savedTodoItems = TodoWorker.loadTodoItems(this)
                val todoItems = remember { mutableStateListOf<TodoItem>().apply { addAll(savedTodoItems) } }

                Surface(
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        navController = navController,
                        todoItems = todoItems
                    )
                }
            }
        }
    }
}


@Composable
fun AppNavigation(
    navController: NavHostController,
    todoItems: MutableList<TodoItem>
) {
    NavHost(navController = navController, startDestination = "todoList") {
        composable("todoList") {
            TodoListScreen(
                navController = navController,
                todoItems = todoItems,
                onAddTaskClick = {
                    navController.navigate("addTask")
                }
            )
        }
        composable("addTask") {
            AddTaskScreen(
                navController = navController,
                onTaskAdded = { newTask ->
                    todoItems.add(newTask)
                    saveTodoItemsWithWorkManager(navController.context, todoItems)
                    navController.navigateUp()
                }
            )
        }
    }
}

fun saveTodoItemsWithWorkManager(context: Context, todoItems: List<TodoItem>) {
    val gson = Gson()
    val todoItemsJson = gson.toJson(todoItems)

    val workData = workDataOf("todoItems" to todoItemsJson)

    val workRequest = OneTimeWorkRequestBuilder<TodoWorker>()
        .setInputData(workData)
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}
