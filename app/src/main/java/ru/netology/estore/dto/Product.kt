package ru.netology.estore.dto

data class Product(
    val id:Int,
    val name:String,
    val group:String,
    val picture:Int,
    val price:Double,
    val oneUnit:Double,
    val unitWeight:String,
    var isHit:Boolean = false,
    var isAction:Boolean = false,
    var minusPercent:Int = 0,
    var isFavorite:Boolean = false
)
