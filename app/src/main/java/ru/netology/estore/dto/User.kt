package ru.netology.estore.dto

data class User(
    val id: Long=0L,
    val firstName:String,
    val username:String,
    val password:String = "",
    val token:String
)
