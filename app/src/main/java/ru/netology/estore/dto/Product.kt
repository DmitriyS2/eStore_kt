package ru.netology.estore.dto

import android.icu.math.BigDecimal.ROUND_DOWN
import java.math.BigDecimal

data class Product(
    val id:Int,
    var name:String,
    var group:String,
    val picture:Int,
    val price:Double,
    val oneUnit:Double,
    var unitWeight:String,
    var isHit:Boolean = false,
    var isDiscount:Boolean = false,
    var minusPercent:Int = 0,
    var isFavorite:Boolean = false,
    var weight:Double = 0.0,
    var inBasket:Boolean = false,
    var sum: Double = 0.0,
    var country:String,
    var storage:String,
    var pack:String
//    val priceN:BigDecimal,
//    var sumN:BigDecimal,

) {
    init {
//        priceN.setScale(2,ROUND_DOWN)
//        sumN.setScale(2, ROUND_DOWN)
//        sumN = BigDecimal(0.0)
    }
}
