package com.example.myapplication3

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AddTaskScreen(
    navController: NavController,
    onTaskAdded: (TodoItem) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Add New Task", style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigateUp() },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Cancel")
            }


            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        val newTask = TodoItem(title, description)
                        onTaskAdded(newTask)
                        NotificationUtils.showNotification(
                            context = navController.context,
                            title = "Task Added",
                            description = "First Task: ${newTask.title}"
                        )
                        navController.navigateUp()
                    }
                },
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text("Add Task")
            }
        }
    }
}