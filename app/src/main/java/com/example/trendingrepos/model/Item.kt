package com.example.trendingrepos.model

data class Item(
    val collaborators_url: String,
    val description: String,
    val forks_count: Int,
    val full_name: String,
    val id: Int,
    val language: String,
    val name: String,
    val owner: Owner,
    val score: Double,
    val stargazers_count: Int,
)