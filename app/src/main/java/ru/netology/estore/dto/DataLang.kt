package ru.netology.estore.dto

import ru.netology.estore.R

abstract class DataLang {
    companion object{
        const val aZ: String = "qwertyuiopASDFGHJKLzxcvbnmQWERTYUIOPasdfghjklZXCVBNM"
    }

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

    abstract val fruitsName:List<String>
    abstract val vegetablesName:List<String>
    abstract val bakeryName:List<String>

    abstract val allGroup:String
    abstract val fruitGroup:String
    abstract val vegetableGroup:String
    abstract val bakeryGroup:String
    abstract val hitGroup:String
    abstract val discountGroup:String
    abstract val favoriteGroup:String
    abstract val basketGroup:String
    abstract val signInGroup:String
    abstract val signUpGroup:String
    abstract val orderGroup:String
    abstract val eStoreGroup:String
    abstract val historyGroup:String

    abstract val country:List<String>
    abstract val storage:List<String>
    abstract val pack:String

    val fruitsPrice = listOf(109.5, 89.9, 179.0, 211.5, 25.0, 189.9)
    val vegetablesPrice = listOf(22.5, 34.3, 29.0, 109.9, 78.5, 111.0)
    val bakeryPrice = listOf(41.0, 45.5, 38.9, 89.5, 77.0, 125.5)

    val fruitOneUnit = listOf(0.5, 0.3, 0.3, 0.2, 3.0, 0.3)
    val vegetablesOneUnit = listOf(0.3, 0.5, 0.3, 0.2, 0.2, 0.1)
    val bakeryOneUnit = listOf(1.0, 1.0, 1.0, 0.3, 0.3, 0.3)

    abstract val fruitUnitWeight:List<String>
    abstract val vegetableUnitWeight:List<String>
    abstract val bakeryUnitWeight:List<String>
}

object DataRus: DataLang() {
    override val fruitsName = listOf("Бананы", "Яблоки", "Груши", "Виноград", "Арбуз", "Апельсины")
    override val vegetablesName = listOf("Картофель", "Капуста", "Морковь", "Перец", "Лук", "Чеснок")
    override val bakeryName = listOf("Ржаной хлеб", "Белый хлеб", "Батон", "Баранки", "Сушки", "Печенье")

    override val allGroup = "Весь ассортимент"
    override val fruitGroup = "Фрукты"
    override val vegetableGroup = "Овощи"
    override val bakeryGroup = "Бакалея"
    override val hitGroup = "Хиты продаж"
    override val discountGroup = "АКЦИЯ!!!"
     override val favoriteGroup = "Избранное"
     override val basketGroup = "Корзина"
     override val signInGroup = "Войти"
     override val signUpGroup = "Зарегистрироваться"
     override val orderGroup = "Заказ"
     override val eStoreGroup = "eStore"
     override val historyGroup = "История заказов"

    override val country = listOf("Россия", "Эквадор")
    override val storage = listOf("Хранить в сухом прохладном месте", "Хранить в холодильнике")
     override val pack:String = "Пакет"

    override val fruitUnitWeight = listOf("кг", "кг", "кг", "кг", "кг", "кг")
    override val vegetableUnitWeight = listOf("кг", "кг", "кг", "кг", "кг", "кг")
    override val bakeryUnitWeight = listOf("шт", "шт", "шт", "кг", "кг", "кг")
}

object DataEng: DataLang() {
    override val fruitsName = listOf("Bananas", "Apple", "Pear", "Grape", "Watermelon", "Orange")
    override val vegetablesName = listOf("Potato", "Cabbage", "Carrot", "Pepper", "Onion", "Garlic")
    override val bakeryName = listOf("Rye bread", "White bread", "Loaf", "Bagels", "Small bagels", "Cookies")

    override val allGroup = "Whole range"
    override val fruitGroup = "Fruits"
    override val vegetableGroup = "Vegetables"
    override val bakeryGroup = "Bakery"
    override val hitGroup = "Bestsellers"
    override val discountGroup = "Discount!!!"
    override val favoriteGroup = "Favorite"
    override val basketGroup = "Basket"
    override val signInGroup = "SignIn"
    override val signUpGroup = "SignUp"
    override val orderGroup = "Order"
    override val eStoreGroup = "eStore"
    override val historyGroup = "History of orders"

    override val country = listOf("Russia", "Ecuador")
    override val storage = listOf("Store in a cool, dry place", "Keep refrigerated")
    override val pack:String = "Bag"

    override val fruitUnitWeight = listOf("kg", "kg", "kg", "kg", "kg", "kg")
    override val vegetableUnitWeight = listOf("kg", "kg", "kg", "kg", "kg", "kg")
    override val bakeryUnitWeight = listOf("pc", "pc", "pc", "kg", "kg", "kg")
}