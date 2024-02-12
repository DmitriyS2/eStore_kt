package ru.netology.estore.dto

import android.icu.math.BigDecimal.ROUND_DOWN
import java.math.BigDecimal

data class Product(
    val id:Int,
    val name:String,
    val group:String,
    val picture:Int,
    val price:Double,
    val oneUnit:Double,
    val unitWeight:String,
    val isHit:Boolean = false,
    val isDiscount:Boolean = false,
    val minusPercent:Int = 0,
    val isFavorite:Boolean = false,
    val weight:Double = 0.0,
    val inBasket:Boolean = false,
    val sum: Double = 0.0,
    val country:String,
    val storage:String,
    val pack:String,
//    val priceN:BigDecimal
    var sumN:BigDecimal = BigDecimal.valueOf(0.0),

    ) {
    init {
//        priceN.setScale(2,ROUND_DOWN)
        sumN.setScale(2, ROUND_DOWN)
//        sumN = BigDecimal(0.0)
    }
}
