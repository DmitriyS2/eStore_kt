package ru.netology.estore.repository

import android.util.Log
import ru.netology.estore.api.ApiService
import ru.netology.estore.dao.DataHistoryDao
import ru.netology.estore.dao.UserDao
import ru.netology.estore.dto.AuthRequest
import ru.netology.estore.dto.DataHistory
import ru.netology.estore.dto.DataLang
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
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val dataHistoryDao: DataHistoryDao

) : ProductRepository {

    var allProducts = arrayListOf<Product>()
    override var allProductsOriginal = arrayListOf<Product>()

    override fun fillAllProducts(dataLang: DataLang): ArrayList<Product> {
        allProducts.addAll(fillFruits(dataLang))
        allProducts.addAll(fillVegetable(dataLang))
        allProducts.addAll(fillBakery(dataLang))
        addHitAndAction(dataLang)
        return allProducts
    }

    private fun fillFruits(dataLang: DataLang): List<Product> {
        val fruits = arrayListOf<Product>()
        for (i in dataLang.fruitsPicture.indices) {
            fruits.add(
                Product(
                    id = dataLang.idProduct++,
                    name = dataLang.fruitsName[i],
                    group = dataLang.fruitGroup,
                    picture = dataLang.fruitsPicture[i],
                    price = dataLang.fruitsPrice[i],
                    oneUnit = dataLang.fruitOneUnit[i],
                    unitWeight = dataLang.fruitUnitWeight[i],
                    country = (if (i == 0 || i == 5) dataLang.country[1] else dataLang.country[0]),
                    storage = dataLang.storage[1],
                    pack = dataLang.pack,
                    buttonAdd = dataLang.buttonAdd,
                    buttonDelete = dataLang.buttonDel
                )
            )
        }
        return fruits
    }

    private fun fillVegetable(dataLang: DataLang): List<Product> {
        val vegetables = arrayListOf<Product>()
        for (i in dataLang.fruitsPicture.indices) {
            vegetables.add(
                Product(
                    id = dataLang.idProduct++,
                    name = dataLang.vegetablesName[i],
                    group = dataLang.vegetableGroup,
                    picture = dataLang.vegetablesPicture[i],
                    price = dataLang.vegetablesPrice[i],
                    oneUnit = dataLang.vegetablesOneUnit[i],
                    unitWeight = dataLang.vegetableUnitWeight[i],
                    country = dataLang.country[0],
                    storage = dataLang.storage[1],
                    pack = dataLang.pack,
                    buttonAdd = dataLang.buttonAdd,
                    buttonDelete = dataLang.buttonDel
                )
            )
        }
        return vegetables
    }

    private fun fillBakery(dataLang: DataLang): List<Product> {
        val bakeries = arrayListOf<Product>()
        for (i in dataLang.fruitsPicture.indices) {
            bakeries.add(
                Product(
                    id = dataLang.idProduct++,
                    name = dataLang.bakeryName[i],
                    group = dataLang.bakeryGroup,
                    picture = dataLang.bakeryPicture[i],
                    price = dataLang.bakeryPrice[i],
                    oneUnit = dataLang.bakeryOneUnit[i],
                    unitWeight = dataLang.bakeryUnitWeight[i],
                    country = dataLang.country[0],
                    storage = dataLang.storage[0],
                    pack = dataLang.pack,
                    buttonAdd = dataLang.buttonAdd,
                    buttonDelete = dataLang.buttonDel
                )
            )
        }
        return bakeries
    }

    private fun addHitAndAction(dataLang: DataLang) {
        for (i in 0..5) {
            val n = (0 until allProducts.size).random()
            allProducts[n].isHit = true
            val m = (0 until allProducts.size).random()
            allProducts[m].isDiscount = true
            allProducts[m].minusPercent = dataLang.discounts.random()
        }
    }

    override fun changeLang(dataLang: DataLang): ArrayList<Product> {
        allProducts = allProducts.map {
            when (it.id) {
                in 0..5 -> {
                    it.copy(
                        name = dataLang.fruitsName[it.id],
                        group = dataLang.fruitGroup,
                        unitWeight = dataLang.fruitUnitWeight[it.id],
                        country = (if (it.id == 0 || it.id == 5) dataLang.country[1] else dataLang.country[0]),
                        storage = dataLang.storage[1],
                        pack = dataLang.pack,
                        buttonAdd = dataLang.buttonAdd,
                        buttonDelete = dataLang.buttonDel
                    )
                }

                in 6..11 -> {
                    it.copy(
                        name = dataLang.vegetablesName[it.id - 6],
                        group = dataLang.vegetableGroup,
                        unitWeight = dataLang.vegetableUnitWeight[it.id - 6],
                        country = dataLang.country[0],
                        storage = dataLang.storage[1],
                        pack = dataLang.pack,
                        buttonAdd = dataLang.buttonAdd,
                        buttonDelete = dataLang.buttonDel
                    )
                }

                in 12..17 -> {
                    it.copy(
                        name = dataLang.bakeryName[it.id - 12],
                        group = dataLang.bakeryGroup,
                        unitWeight = dataLang.bakeryUnitWeight[it.id - 12],
                        country = dataLang.country[0],
                        storage = dataLang.storage[0],
                        pack = dataLang.pack,
                        buttonAdd = dataLang.buttonAdd,
                        buttonDelete = dataLang.buttonDel
                    )
                }

                else -> {
                    it.copy()
                }
            }
        } as ArrayList<Product>

        return allProducts
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
        allProducts = allProducts.onEach {
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

    override suspend fun checkSignIn(login: String) = userDao.getUser(login)?.toDto()

    override suspend fun signUp(login: String, password: String, name: String): User? {
        val user = User(firstName = name, username = login, password = password, token = getToken())
        userDao.insert(UserEntity.fromDto(user))
        return checkSignIn(login)
    }

    private fun getToken(): String {
        var token = ""
        for (i in 0..20) {
            token += DataLang.aZ[(0 until DataLang.aZ.length).random()]
        }
        return token
    }

    override suspend fun getHistory(login: String): List<DataHistory> =
        dataHistoryDao.getDataHistory(login).toDto()

    override suspend fun addHistory(dataHistory: DataHistory) {
        dataHistoryDao.insert(DataHistoryEntity.fromDto(dataHistory))
    }

    override suspend fun signInApi(request: AuthRequest) = apiService.auth(request)
}