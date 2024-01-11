package ru.netology.estore.dto

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class DataOrderForHistory(
    val id:Long,
    val login:String,
    val sumOrder:Double,
    val pickUp:Boolean,
    val dateTime: String = OffsetDateTime.now().format((DateTimeFormatter.ofPattern("HH:mm")))
)
