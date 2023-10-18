package ru.netology.estore.dto

import ru.netology.estore.R
import kotlin.math.roundToInt

object Data {

    var idProduct = 0
    val discounts = listOf(5, 10, 15, 20, 25, 30)

    var allProducts = arrayListOf<Product>()

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

    val fruitsName = listOf("Бананы", "Яблоки", "Груши", "Виноград", "Арбуз", "Апельсин")
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


    val fruitsPrice = listOf(109.5, 89.9, 179.0, 211.5, 25.0, 189.9)
    val vegetablesPrice = listOf(22.5, 34.3, 29.0, 109.9, 78.5, 111.0)
    val bakeryPrice = listOf(41.0, 45.5, 38.9, 89.5, 77.0, 125.5)

    val fruitOneUnit = listOf(0.5, 0.3, 0.3, 0.2, 3.0, 0.3)
    val vegetablesOneUnit = listOf(0.3, 0.5, 0.3, 0.2, 0.2, 0.1)
    val bakeryOneUnit = listOf(1.0, 1.0, 1.0, 0.3, 0.3, 0.3)

    val fruitUnitWeight = listOf("кг", "кг", "кг", "кг", "кг", "кг")
    val vegetableUnitWeight = listOf("кг", "кг", "кг", "кг", "кг", "кг")
    val bakeryUnitWeight = listOf("шт", "шт", "шт", "кг", "кг", "кг")

    fun fillAllProducts():ArrayList<Product> {

        allProducts.addAll(fillFruits())
        allProducts.addAll(fillVegetable())
        allProducts.addAll(fillBakery())
        addHitAndAction()
        return allProducts
    }

    fun fillFruits():List<Product> {
        val fruits = arrayListOf<Product>()
        for (i in 0 until fruitsPicture.size) {
            fruits.add(
                Product(
                    id = idProduct++,
                    name = fruitsName[i],
                    group = fruitGroup,
                    picture = fruitsPicture[i],
                    price = fruitsPrice[i],
                    oneUnit = fruitOneUnit[i],
                    unitWeight = fruitUnitWeight[i]
                )
            )
        }
        return fruits
    }

    fun fillVegetable():List<Product> {
        val vegetables = arrayListOf<Product>()
        for (i in 0 until fruitsPicture.size) {
            vegetables.add(
                Product(
                    id = idProduct++,
                    name = vegetablesName[i],
                    group = vegetableGroup,
                    picture = vegetablesPicture[i],
                    price = vegetablesPrice[i],
                    oneUnit = vegetablesOneUnit[i],
                    unitWeight = vegetableUnitWeight[i]
                )
            )
        }
        return vegetables
    }

    fun fillBakery():List<Product> {
        val bakeries = arrayListOf<Product>()
        for (i in 0 until fruitsPicture.size) {
            bakeries.add(
                Product(
                    id = idProduct++,
                    name = bakeryName[i],
                    group = bakeryGroup,
                    picture = bakeryPicture[i],
                    price = bakeryPrice[i],
                    oneUnit = bakeryOneUnit[i],
                    unitWeight = bakeryUnitWeight[i]
                )
            )
        }
        return bakeries
    }

    fun addHitAndAction() {
        for(i in 0..5) {
            val n = (0 until allProducts.size).random()
            allProducts[n].isHit = true
            val m = (0 until allProducts.size).random()
            allProducts[m].isDiscount = true
            allProducts[m].minusPercent = discounts.random()

        }
    }

    fun like(product: Product):ArrayList<Product> {
               allProducts = allProducts.map {
            if (it != product) it else it.copy(
                isFavorite = !it.isFavorite
            )
        } as ArrayList
        return allProducts
    }
}

fun getSumWithTwoDecimal(number:Double):Double = (number*100).roundToInt()/100.0
