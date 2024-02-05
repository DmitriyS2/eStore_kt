package ru.netology.estore.dto

import kotlin.math.roundToInt

fun getSumWithTwoDecimal(number: Double, del: Double): Double =
    (number * del.toInt()).roundToInt() / del
