package ru.netology.estore.dto

import ru.netology.estore.R
import kotlin.math.roundToInt

object Data {

    val aZ: String = "qwertyuiopASDFGHJKLzxcvbnmQWERTYUIOPasdfghjklZXCVBNM"
    var idProduct = 0
    val discounts = listOf(5, 10, 15, 20, 25, 30)

    val fruitsPicture = listOf(
        R.drawable.banan, R.drawable.apple,
        R.drawable.pear, R.drawable.grapes, R.drawable.watermelon, R.drawable.orange
    )

    val vegetablesPicture = listOf(
        R.drawable.potato, R.drawable.cabbage,
        R.drawable.carrot, R.drawable.pepper, R.drawable.onion, R.drawable.garlic
    )

    val bakeryPicture = listOf(
        R.drawable.brown_bread, R.drawable.white_bread,
        R.drawable.loaf, R.drawable.baranki, R.drawable.sushki, R.drawable.cookies
    )

    val fruitsName = listOf("Бананы", "Яблоки", "Груши", "Виноград", "Арбуз", "Апельсины")
    val vegetablesName = listOf("Картофель", "Капуста", "Морковь", "Перец", "Лук", "Чеснок")
    val bakeryName =
        listOf("Ржаной хлеб", "Белый хлеб", "Батон", "Баранки", "Сушки", "Печенье")

    val allGroup = "Весь ассортимент"
    val fruitGroup = "Фрукты"
    val vegetableGroup = "Овощи"
    val bakeryGroup = "Бакалея"
    val hitGroup = "Хиты продаж"
    val discountGroup = "АКЦИЯ!!!"
    val favoriteGroup = "Избранное"
    val basketGroup = "Корзина"
    val signInGroup = "SignIn"
    val signUpGroup = "SignUp"
    val orderGroup = "Заказ"
    val eStoreGroup = "eStore"
    val historyGroup = "История заказов"

    val country = listOf("Россия", "Эквадор")
    val storage = listOf("Хранить в сухом прохладном месте", "Хранить в холодильнике")
    val pack:String = "Пакет"

    val fruitsPrice = listOf(109.5, 89.9, 179.0, 211.5, 25.0, 189.9)
    val vegetablesPrice = listOf(22.5, 34.3, 29.0, 109.9, 78.5, 111.0)
    val bakeryPrice = listOf(41.0, 45.5, 38.9, 89.5, 77.0, 125.5)

    val fruitOneUnit = listOf(0.5, 0.3, 0.3, 0.2, 3.0, 0.3)
    val vegetablesOneUnit = listOf(0.3, 0.5, 0.3, 0.2, 0.2, 0.1)
    val bakeryOneUnit = listOf(1.0, 1.0, 1.0, 0.3, 0.3, 0.3)

    val fruitUnitWeight = listOf("кг", "кг", "кг", "кг", "кг", "кг")
    val vegetableUnitWeight = listOf("кг", "кг", "кг", "кг", "кг", "кг")
    val bakeryUnitWeight = listOf("шт", "шт", "шт", "кг", "кг", "кг")

}
fun getSumWithTwoDecimal(number: Double, del: Double): Double =
    (number * del.toInt()).roundToInt() / del
