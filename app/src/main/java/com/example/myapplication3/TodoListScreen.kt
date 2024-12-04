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
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        backgroundImageUri.value?.let {
            AsyncImage(
                model = it,
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Colonne avec le contenu de la liste de tâches
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
                            todoItems.remove(taskToDelete)  // Supprimer une tâche de la liste
                        }
                    )
                }
            }

            // Boutons pour ajouter une tâche ou changer l'image de fond
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = onAddTaskClick) {
                    Text("Add Task")
                }

                Button(
                    onClick = {
                        // Lancer le sélecteur d'image pour choisir une nouvelle image de fond
                        launcher.launch("image/*")
                    }
                ) {
                    Text("Set Background")
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
