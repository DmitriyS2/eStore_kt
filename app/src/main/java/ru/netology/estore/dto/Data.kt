package ru.netology.estore.dto

import ru.netology.estore.R
import ru.netology.estore.activity.idProduct
import kotlin.math.roundToInt

object Data {

    val allProducts = arrayListOf<Product>()

    val fruitsPicture = arrayListOf(
        R.drawable.banan, R.drawable.apple,
        R.drawable.pear, R.drawable.grapes, R.drawable.watermelon, R.drawable.orange
    )

    val vegetablesPicture = arrayListOf(
        R.drawable.potato, R.drawable.cabbage,
        R.drawable.carrot, R.drawable.pepper, R.drawable.onion, R.drawable.garlic
    )

    val bakeryPicture = arrayListOf(
        R.drawable.brown_bread, R.drawable.white_bread,
        R.drawable.loaf, R.drawable.baranki, R.drawable.sushki, R.drawable.cookies
    )

    val fruitsName = arrayListOf("Бананы", "Яблоки", "Груши", "Виноград", "Арбуз", "Апельсин")
    val vegetablesName = arrayListOf("Картофель", "Капуста", "Морковь", "Перец", "Лук", "Чеснок")
    val bakeryName =
        arrayListOf("Ржаной хлеб", "Белый хлеб", "Батон", "Баранки", "Сушки", "Печенье")

    val fruitGroup = "Фрукты"
    val vegetableGroup = "Овощи"
    val bakeryGroup = "Бакалея"

    val fruitsPrice = arrayListOf(109.5, 89.9, 179.0, 211.5, 25.0, 189.9)
    val vegetablesPrice = arrayListOf(22.5, 34.3, 29.0, 109.9, 78.5, 111.0)
    val bakeryPrice = arrayListOf(41.0, 45.5, 38.9, 89.5, 77.0, 125.5)

    val fruitOneUnit = arrayListOf(0.5, 0.3, 0.3, 0.2, 3.0, 0.3)
    val vegetablesOneUnit = arrayListOf(0.3, 0.5, 0.3, 0.2, 0.2, 0.1)
    val bakeryOneUnit = arrayListOf(1.0, 1.0, 1.0, 0.3, 0.3, 0.3)

    val fruitUnitWeight = arrayListOf("кг", "кг", "кг", "кг", "кг", "кг")
    val vegetableUnitWeight = arrayListOf("кг", "кг", "кг", "кг", "кг", "кг")
    val bakeryUnitWeight = arrayListOf("шт", "шт", "шт", "кг", "кг", "кг")

    fun fillAllProducts():ArrayList<Product> {

        allProducts.addAll(fillFruits())
        allProducts.addAll(fillVegetable())
        allProducts.addAll(fillBakery())

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
}

fun getSumWithTwoDecimal(number:Double):Double = (number*100).roundToInt()/100.0
