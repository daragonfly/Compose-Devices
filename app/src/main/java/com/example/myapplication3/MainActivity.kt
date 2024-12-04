package com.example.myapplication3

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication3.ui.theme.TodoListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListAppTheme {
                // Configure le navController
                val navController = rememberNavController()
                val todoItems = remember { mutableStateListOf<TodoItem>() }

                Surface(
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(navController, todoItems)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, todoItems: MutableList<TodoItem>) {
    NavHost(navController = navController, startDestination = "todoList") {
        composable("todoList") {
            TodoListScreen(
                navController = navController,  // Passer le navController ici
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
                    navController.navigateUp()  // Revenir à la liste
                }
            )
        }
    }
}


