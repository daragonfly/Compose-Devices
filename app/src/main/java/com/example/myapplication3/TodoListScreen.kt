package com.example.myapplication3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
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
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.myapplication3.TodoItem

@Composable
fun TodoListScreen(
    navController: NavHostController,
    todoItems: MutableList<TodoItem>,
    onAddTaskClick: () -> Unit
) {
    var backgroundImageUri by remember { mutableStateOf<String?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                backgroundImageUri = uri.toString()
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Affiche l'image de fond si elle existe
        if (backgroundImageUri != null) {
            AsyncImage(
                model = backgroundImageUri,
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

            LazyColumn(modifier = Modifier.weight(1f).padding(top = 16.dp)) {
                items(todoItems.size) { index ->
                    TodoItemCard(
                        todoItem = todoItems[index],
                        onDeleteClick = { taskToDelete ->
                            todoItems.remove(taskToDelete)
                        }
                    )
                }
            }

            // Boutons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = onAddTaskClick) {
                    Text("Add Task")
                }

                Button(
                    onClick = {
                        // Lance le sélecteur d'image
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
fun TodoItemCard(todoItem: TodoItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp)
            .then(Modifier
                .background(
                    color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                )
                .padding(16.dp) // Intérieur de la carte
            )
    ) {
        Text(
            text = todoItem.title,
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
            color = androidx.compose.material3.MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp)) // Espacement entre titre et description
        Text(
            text = todoItem.description,
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
        )
    }
    // Ajout d'un Divider entre les tâches
    Divider(
        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}
@Composable
fun TodoItemCard(todoItem: TodoItem, onDeleteClick: () -> Unit, onViewImageClick: () -> Unit) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Espace entre les cartes
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp), // Coins arrondis
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp) // Ombre douce
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Titre de la tâche
            Text(
                text = todoItem.title,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Description de la tâche
            Text(todoItem.description)

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton "Delete" aligné à droite
            Button(
                onClick = onDeleteClick,
                modifier = Modifier.align(alignment = androidx.compose.ui.Alignment.End)
            ) {
                Text("Delete")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bouton "View Image" pour accéder à l'image
            Button(
                onClick = onViewImageClick,
                modifier = Modifier.align(alignment = androidx.compose.ui.Alignment.End)
            ) {
                Text("View Image")
            }
        }
    }
}
@Composable
fun TodoItemCard(
    todoItem: TodoItem,
    onDeleteClick: (TodoItem) -> Unit // Accepter le paramètre pour la suppression
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
            onClick = { onDeleteClick(todoItem) }, // Appel de la fonction pour supprimer la tâche
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Task")
        }
    }
}

fun openImagePicker(context: Context, onImageSelected: (String) -> Unit) {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    (context as Activity).startActivityForResult(intent, 1)
}





