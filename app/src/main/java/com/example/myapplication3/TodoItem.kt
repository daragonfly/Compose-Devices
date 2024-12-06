package com.example.myapplication3


import java.io.Serializable

data class TodoItem(
    val title: String,
    val description: String
) : Serializable
