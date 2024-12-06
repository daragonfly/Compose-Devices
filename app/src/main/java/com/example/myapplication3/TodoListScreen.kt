package com.example.myapplication3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration

@Composable
fun TodoListScreen(
    navController: NavHostController,
    todoItems: MutableList<TodoItem>,
    onAddTaskClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }
    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(
                message = "Tâche sauvegardée avec succès",
                duration = SnackbarDuration.Short
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                "Todo List",
                style = MaterialTheme.typography.headlineMedium
            )
            LazyColumn(modifier = Modifier.weight(1f).padding(top = 16.dp)) {
                items(todoItems.size) { index ->
                    TodoItemCard(
                        todoItem = todoItems[index],
                        onDeleteClick = { taskToDelete ->
                            todoItems.remove(taskToDelete)
                            saveTodoItemsWithWorkManager(navController.context, todoItems)
                        }
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = onAddTaskClick) {
                    Text("Add Task")
                }
            }
        }


        SnackbarHost(hostState = snackbarHostState)
    }

    LaunchedEffect(todoItems) {
        saveTodoItemsWithWorkManager(navController.context, todoItems)
        showSnackbar = true
    }
}


@Composable
fun TodoItemCard(
    todoItem: TodoItem,
    onDeleteClick: (TodoItem) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
        .padding(16.dp)
    ) {
        Text(todoItem.title, style = MaterialTheme.typography.titleMedium)
        Text(todoItem.description)

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onDeleteClick(todoItem) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Task")
        }
    }
}