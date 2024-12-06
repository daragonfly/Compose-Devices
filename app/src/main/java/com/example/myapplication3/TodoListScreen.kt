package com.example.myapplication3

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun TodoListScreen(
    navController: NavHostController,
    todoItems: MutableList<TodoItem>,
    onAddTaskClick: () -> Unit,
    backgroundImageUri: MutableState<String?> // Recevoir l'URI de l'image ici
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                backgroundImageUri.value = uri.toString() // Mettre à jour l'URI
                saveBackgroundUri(navController.context, uri.toString()) // Sauvegarder l'URI
            }
        }
    )

    // Charger l'URI de l'image sauvegardée (si pas déjà défini)
    LaunchedEffect(Unit) {
        if (backgroundImageUri.value == null) {
            backgroundImageUri.value = loadBackgroundUri(navController.context)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Affiche l'image de fond si elle existe
        backgroundImageUri.value?.let {
            AsyncImage(
                model = it,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                "Todo List",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
            )

            // Liste des tâches
            LazyColumn(modifier = Modifier.weight(1f).padding(top = 16.dp)) {
                items(todoItems.size) { index ->
                    TodoItemCard(
                        todoItem = todoItems[index],
                        onDeleteClick = { taskToDelete ->
                            todoItems.remove(taskToDelete) // Supprimer une tâche de la liste
                            saveTodoItemsWithWorkManager(navController.context, todoItems)
                        }
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = onAddTaskClick) {
                    Text("Add Task")
                }

                Button(
                    onClick = {
                        launcher.launch("image/*") // Sélectionner une nouvelle image de fond
                    }
                ) {
                    Text("Set Background")
                }

                Button(
                    onClick = {
                        // Sauvegarder les tâches via WorkManager
                        saveTodoItemsWithWorkManager(navController.context, todoItems)
                    }
                ) {
                    Text("Save Tasks")
                }
            }
        }
    }
}



@Composable
fun TodoItemCard(
    todoItem: TodoItem,
    onDeleteClick: (TodoItem) -> Unit  // Fonction pour supprimer la tâche
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
            onClick = { onDeleteClick(todoItem) },  // Appeler la fonction de suppression
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Task")
        }
    }
}
