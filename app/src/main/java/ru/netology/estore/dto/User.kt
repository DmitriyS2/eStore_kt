package ru.netology.estore.dto

data class User(
    val id: Long=0L,
    val name:String,
    val login:String,
    val password:String,
    val token:String
)
