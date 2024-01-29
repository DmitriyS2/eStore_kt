package ru.netology.estore.repository

import android.util.Log
import ru.netology.estore.dao.DataHistoryDao
import ru.netology.estore.dao.UserDao
import ru.netology.estore.dto.Data
import ru.netology.estore.dto.DataHistory
import ru.netology.estore.dto.Product
import ru.netology.estore.dto.User
import ru.netology.estore.dto.getSumWithTwoDecimal
import ru.netology.estore.entity.DataHistoryEntity
import ru.netology.estore.entity.UserEntity
import ru.netology.estore.entity.toDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dataHistoryDao: DataHistoryDao

):ProductRepository {

    var allProducts = arrayListOf<Product>()
    override var allProductsOriginal = arrayListOf<Product>()

    override fun fillAllProducts(): ArrayList<Product> {

        allProducts.addAll(fillFruits())
        allProducts.addAll(fillVegetable())
        allProducts.addAll(fillBakery())
        addHitAndAction()
        return allProducts
    }

    private fun fillFruits(): List<Product> {
        val fruits = arrayListOf<Product>()
        for (i in Data.fruitsPicture.indices) {
            fruits.add(
                Product(
                    id = Data.idProduct++,
                    name = Data.fruitsName[i],
                    group = Data.fruitGroup,
                    picture = Data.fruitsPicture[i],
                    price = Data.fruitsPrice[i],
                    oneUnit = Data.fruitOneUnit[i],
                    unitWeight = Data.fruitUnitWeight[i]
                )
            )
        }
        return fruits
    }

    private fun fillVegetable(): List<Product> {
        val vegetables = arrayListOf<Product>()
        for (i in Data.fruitsPicture.indices) {
            vegetables.add(
                Product(
                    id = Data.idProduct++,
                    name = Data.vegetablesName[i],
                    group = Data.vegetableGroup,
                    picture = Data.vegetablesPicture[i],
                    price = Data.vegetablesPrice[i],
                    oneUnit = Data.vegetablesOneUnit[i],
                    unitWeight = Data.vegetableUnitWeight[i]
                )
            )
        }
        return vegetables
    }

    private fun fillBakery(): List<Product> {
        val bakeries = arrayListOf<Product>()
        for (i in Data.fruitsPicture.indices) {
            bakeries.add(
                Product(
                    id = Data.idProduct++,
                    name = Data.bakeryName[i],
                    group = Data.bakeryGroup,
                    picture = Data.bakeryPicture[i],
                    price = Data.bakeryPrice[i],
                    oneUnit = Data.bakeryOneUnit[i],
                    unitWeight = Data.bakeryUnitWeight[i]
                )
            )
        }
        return bakeries
    }

    private fun addHitAndAction() {
        for (i in 0..5) {
            val n = (0 until allProducts.size).random()
            allProducts[n].isHit = true
            val m = (0 until allProducts.size).random()
            allProducts[m].isDiscount = true
            allProducts[m].minusPercent = Data.discounts.random()

        }
    }

    override fun like(product: Product): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it != product) it else it.copy(
                isFavorite = !it.isFavorite
            )
        } as ArrayList
        return allProducts
    }

    override fun addToBasket(product: Product): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it != product) it else it.copy(
                inBasket = !it.inBasket,
                weight = (if (!it.inBasket) it.oneUnit else 0.0),
                sum = if (it.inBasket) 0.0 else (it.oneUnit * (if (it.isDiscount) (it.price * (100 - it.minusPercent) / 100) else it.price))
            )
        } as ArrayList
        Log.d("MyLog", "${allProducts.filter { it.id == product.id }}")
        return allProducts
    }

    override fun addToBasketAgain(product: Product): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it != product) it else it.copy(
                weight = it.oneUnit,
                sum = (it.oneUnit * (if (it.isDiscount) (it.price * (100 - it.minusPercent) / 100) else it.price))
            )
        } as ArrayList
        Log.d("MyLog", "${allProducts.filter { it.id == product.id }}")
        return allProducts
    }

    override fun deleteFromBasket(product: Product): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it != product) it else it.copy(
                inBasket = false,
                weight = 0.0,
                sum = 0.0
            )
        } as ArrayList
        Log.d("MyLog", "${allProducts.filter { it.id == product.id }}")
        return allProducts
    }

    override fun weightPLus(product: Product): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it != product) it else it.copy(
                weight = getSumWithTwoDecimal(it.weight + it.oneUnit, 10.0),
                sum = getSumWithTwoDecimal(
                    (it.weight + it.oneUnit) * (if (it.isDiscount) (it.price * (100 - it.minusPercent) / 100) else it.price),
                    10.0
                )
            )
        } as ArrayList
        Log.d("MyLog", "${allProducts.filter { it.id == product.id }}")
        return allProducts
    }

    override fun weightMinus(product: Product): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it != product) it else it.copy(
                weight = getSumWithTwoDecimal(it.weight - it.oneUnit, 10.0),
                sum = getSumWithTwoDecimal(
                    (it.weight - it.oneUnit) * (if (it.isDiscount) (it.price * (100 - it.minusPercent) / 100) else it.price),
                    10.0
                )
            )
        } as ArrayList
        Log.d("MyLog", "${allProducts.filter { it.id == product.id }}")
        return allProducts
    }

    override fun deleteFromBasketWeightZero(): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it.inBasket && it.weight == 0.0) it.copy(
                inBasket = false
            ) else it
        } as ArrayList
        return allProducts
    }

    override fun countOrder(list: List<Product>): Double {
        var sum = 0.0
        for (item in list) {
            sum += item.sum
        }
        return getSumWithTwoDecimal(sum, 100.0)
    }

    override fun cleanBasket(): ArrayList<Product> {
        allProducts = allProducts.onEach{
            it.weight = 0.0
            it.inBasket = false
            it.sum = 0.0
        }
        return allProducts
    }
    override fun reNewDataFull(): ArrayList<Product> {
        allProducts = allProductsOriginal
        return allProducts
    }

    override suspend fun checkSignIn(login: String) =  userDao.getUser(login)?.toDto()

    override suspend fun signUp(login: String, password: String, name: String):User? {
        val user = User(name = name, login = login, password = password, token = getToken())
        userDao.insert(UserEntity.fromDto(user))
        return checkSignIn(login)
    }

    private fun getToken():String{
        var token =""
        for(i in 0..20) {
            token+=Data.aZ[(0 until Data.aZ.length).random()]
        }
        return token
    }

    override suspend fun getHistory(login: String): List<DataHistory> = dataHistoryDao.getDataHistory(login).toDto()

    override suspend fun addHistory(dataHistory: DataHistory) {
        dataHistoryDao.insert(DataHistoryEntity.fromDto(dataHistory))
    }

}