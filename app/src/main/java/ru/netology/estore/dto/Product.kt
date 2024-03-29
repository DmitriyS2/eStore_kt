package ru.netology.estore.dto

import java.math.BigDecimal
import java.math.RoundingMode

data class Product(
    val id: Int,
    val name: String,
    val group: String,
    val picture: Int,
    val unitWeight: String,
    val isHit: Boolean = false,
    val isDiscount: Boolean = false,
    val minusPercent: Int = 0,
    val isFavorite: Boolean = false,
    val inBasket: Boolean = false,
    val country: String,
    val storage: String,
    val pack: String,
    val priceN: BigDecimal,
    val oneUnitN: BigDecimal,
    val sumN: BigDecimal = BigDecimal.valueOf(0.0).setScale(2, RoundingMode.HALF_UP),
    val weightN: BigDecimal = BigDecimal.valueOf(0.0).setScale(1, RoundingMode.HALF_UP)
)
