package ru.netology.estore.dto

import ru.netology.estore.R
import java.math.BigDecimal
import java.math.RoundingMode

abstract class DataLang {
    companion object {
        const val aZ: String = "qwertyuiopASDFGHJKLzxcvbnmQWERTYUIOPasdfghjklZXCVBNM"
        val bigDecZero: BigDecimal = BigDecimal(0.0).setScale(1, RoundingMode.HALF_UP)
        val bigDecZeroZero:BigDecimal = BigDecimal(0.0).setScale(2, RoundingMode.HALF_UP)
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

    abstract val fruitsName: List<String>
    abstract val vegetablesName: List<String>
    abstract val bakeryName: List<String>

    abstract val allGroup: String
    abstract val fruitGroup: String
    abstract val vegetableGroup: String
    abstract val bakeryGroup: String
    abstract val hitGroup: String
    abstract val discountGroup: String
    abstract val favoriteGroup: String
    abstract val basketGroup: String
    abstract val signInGroup: String
    abstract val signUpGroup: String
    abstract val orderGroup: String
    abstract val eStoreGroup: String
    abstract val historyGroup: String

    abstract val country: List<String>
    abstract val storage: List<String>
    abstract val pack: String

    val fruitsPrice = listOf(109.5, 89.9, 179.0, 211.5, 25.0, 189.9)
    val vegetablesPrice = listOf(22.5, 34.3, 29.0, 109.9, 78.5, 111.0)
    val bakeryPrice = listOf(41.0, 45.5, 38.9, 89.5, 77.0, 125.5)

    val fruitsPriceN:List<BigDecimal> = listOf(
        BigDecimal(109.5).setScale(2), BigDecimal(89.9), BigDecimal(179.0),
        BigDecimal(211.5), BigDecimal(25.0), BigDecimal(189.9))
    val vegetablesPriceN:List<BigDecimal> = listOf(
        BigDecimal(22.5), BigDecimal(34.3), BigDecimal(29.0),
        BigDecimal(109.9), BigDecimal(78.5), BigDecimal(111.0))
    val bakeryPriceN:List<BigDecimal> = listOf(
        BigDecimal(41.0), BigDecimal(45.5), BigDecimal(38.9),
        BigDecimal(89.5), BigDecimal(77.0), BigDecimal(125.5))

    val fruitOneUnit = listOf(0.5, 0.3, 0.3, 0.2, 3.0, 0.3)
    val vegetablesOneUnit = listOf(0.3, 0.5, 0.3, 0.2, 0.2, 0.1)
    val bakeryOneUnit = listOf(1.0, 1.0, 1.0, 0.3, 0.3, 0.3)

    val fruitOneUnitN:List<BigDecimal> = listOf(
        BigDecimal(0.5).setScale(2), BigDecimal(0.3), BigDecimal(0.3),
        BigDecimal(0.2), BigDecimal(3.0), BigDecimal(0.3))
    val vegetablesOneUnitN:List<BigDecimal> = listOf(
        BigDecimal(0.3), BigDecimal(0.5), BigDecimal(0.3),
        BigDecimal(0.2), BigDecimal(0.2), BigDecimal(0.1))
    val bakeryOneUnitN:List<BigDecimal> = listOf(
        BigDecimal(1.0), BigDecimal(1.0), BigDecimal(1.0),
        BigDecimal(0.3), BigDecimal(0.3), BigDecimal(0.3))

    abstract val fruitUnitWeight: List<String>
    abstract val vegetableUnitWeight: List<String>
    abstract val bakeryUnitWeight: List<String>

    abstract val typeOfDelivery: String
    abstract val addressPickUp: String
    abstract val addressDelivery: String
    abstract val typeOfPayment: String
}

object DataRus : DataLang() {
    override val fruitsName = listOf("Бананы", "Яблоки", "Груши", "Виноград", "Арбуз", "Апельсины")
    override val vegetablesName =
        listOf("Картофель", "Капуста", "Морковь", "Перец", "Лук", "Чеснок")
    override val bakeryName =
        listOf("Ржаной хлеб", "Белый хлеб", "Батон", "Баранки", "Сушки", "Печенье")

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
    override val pack: String = "Пакет"

    override val fruitUnitWeight =
        listOf("руб/кг", "руб/кг", "руб/кг", "руб/кг", "руб/кг", "руб/кг")
    override val vegetableUnitWeight =
        listOf("руб/кг", "руб/кг", "руб/кг", "руб/кг", "руб/кг", "руб/кг")
    override val bakeryUnitWeight =
        listOf("руб/шт", "руб/шт", "руб/шт", "руб/кг", "руб/кг", "руб/кг")

    override val typeOfDelivery: String = "Сами заберете или Вам привезти?"
    override val addressPickUp: String = "Выберите магазин, откуда заберете"
    override val addressDelivery: String = "Куда Вам привезти?"
    override val typeOfPayment: String = "Выберите способ оплаты"
}

object DataEng : DataLang() {
    override val fruitsName = listOf("Bananas", "Apple", "Pear", "Grape", "Watermelon", "Orange")
    override val vegetablesName = listOf("Potato", "Cabbage", "Carrot", "Pepper", "Onion", "Garlic")
    override val bakeryName =
        listOf("Rye bread", "White bread", "Loaf", "Bagels", "Small bagels", "Cookies")

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
    override val pack: String = "Bag"

    override val fruitUnitWeight =
        listOf("rub/kg", "rub/kg", "rub/kg", "rub/kg", "rub/kg", "rub/kg")
    override val vegetableUnitWeight =
        listOf("rub/kg", "rub/kg", "rub/kg", "rub/kg", "rub/kg", "rub/kg")
    override val bakeryUnitWeight =
        listOf("rub/pc", "rub/pc", "rub/pc", "rub/kg", "rub/kg", "rub/kg")

    override val typeOfDelivery = "Will you pick it up yourself or bring it to you?"
    override val addressPickUp = "Select the store where you will pick it up"
    override val addressDelivery = "Where should we bring it to you?"
    override val typeOfPayment = "Select a payment method"
}