package ru.netology.estore.dto

data class DataHistory(
    val id:Long,
    val login:String,
    val sumOrder:Double,
    val pickUp:Boolean,
    val dateTime: String
)
