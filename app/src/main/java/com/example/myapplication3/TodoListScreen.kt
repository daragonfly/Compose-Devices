package com.example.myapplication3

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodoListScreen(todoItems: List<TodoItem>, onAddTaskClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Todo List", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)

        LazyColumn(modifier = Modifier.weight(1f).padding(top = 16.dp)) {
            items(todoItems.size) { index ->
                TodoItemCard(todoItem = todoItems[index])
            }
        }

        // Bouton pour ajouter une nouvelle tâche
        Button(onClick = onAddTaskClick, modifier = Modifier.fillMaxWidth()) {
            Text("Add Task")
        }
    }
}

@Composable
fun TodoItemCard(todoItem: TodoItem) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(todoItem.title, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
        Text(todoItem.description)
    }
}
