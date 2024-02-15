package ru.netology.estore.repository

import ru.netology.estore.api.ApiService
import ru.netology.estore.dao.DataHistoryDao
import ru.netology.estore.dao.UserDao
import ru.netology.estore.dto.AuthRequest
import ru.netology.estore.dto.DataHistory
import ru.netology.estore.dto.DataLang
import ru.netology.estore.dto.DataLang.Companion.bigDecZero
import ru.netology.estore.dto.DataLang.Companion.bigDecZeroZero
import ru.netology.estore.dto.Product
import ru.netology.estore.dto.User
import ru.netology.estore.entity.DataHistoryEntity
import ru.netology.estore.entity.UserEntity
import ru.netology.estore.entity.toDto
import java.math.BigDecimal
import java.math.RoundingMode
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
                    unitWeight = dataLang.fruitUnitWeight[i],
                    country = (if (i == 0 || i == 5) dataLang.country[1] else dataLang.country[0]),
                    storage = dataLang.storage[1],
                    pack = dataLang.pack,
                    priceN = dataLang.fruitsPriceN[i].setScale(2, RoundingMode.HALF_UP),
                    oneUnitN = dataLang.fruitOneUnitN[i].setScale(1, RoundingMode.HALF_UP),
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
                    unitWeight = dataLang.vegetableUnitWeight[i],
                    country = dataLang.country[0],
                    storage = dataLang.storage[1],
                    pack = dataLang.pack,
                    priceN = dataLang.vegetablesPriceN[i].setScale(2, RoundingMode.HALF_UP),
                    oneUnitN = dataLang.vegetablesOneUnitN[i].setScale(1, RoundingMode.HALF_UP),
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
                    unitWeight = dataLang.bakeryUnitWeight[i],
                    country = dataLang.country[0],
                    storage = dataLang.storage[0],
                    pack = dataLang.pack,
                    priceN = dataLang.bakeryPriceN[i].setScale(2, RoundingMode.HALF_UP),
                    oneUnitN = dataLang.bakeryOneUnitN[i].setScale(1, RoundingMode.HALF_UP),
                )
            )
        }
        return bakeries
    }

    private fun addHitAndAction(dataLang: DataLang) {
        for (i in 0..5) {
            val n = (1..allProducts.size).random()
            val m = (1..allProducts.size).random()
            allProducts = ArrayList(
                allProducts.map {
                    it.copy(
                        isHit = if (it.id == n) true else it.isHit,
                        isDiscount = if (it.id == m) true else it.isDiscount,
                        minusPercent = if (it.id == m) dataLang.discounts.random() else it.minusPercent
                    )
                }
            )
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
                weightN = (if (!it.inBasket) it.oneUnitN else bigDecZero).setScale(
                    1,
                    RoundingMode.HALF_UP
                ),
                sumN = (if (it.inBasket) bigDecZeroZero else (it.oneUnitN * (if (it.isDiscount) (it.priceN * BigDecimal(
                    ((100 - it.minusPercent) / 100.0)
                )) else it.priceN)))
            )
        } as ArrayList
        return allProducts
    }

    override fun addToBasketAgain(product: Product): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it != product) it else it.copy(
                weightN = it.oneUnitN,
                sumN = (it.oneUnitN * (if (it.isDiscount) (it.priceN * BigDecimal((100 - it.minusPercent) / 100.0)) else it.priceN))
            )
        } as ArrayList
        return allProducts
    }

    override fun deleteFromBasket(product: Product): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it != product) it else it.copy(
                inBasket = false,
                weightN = bigDecZero,
                sumN = bigDecZeroZero
            )
        } as ArrayList
        return allProducts
    }

    override fun weightPLus(product: Product): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it != product) it else it.copy(
                weightN = it.weightN + it.oneUnitN,
                sumN = (it.weightN + it.oneUnitN) * (if (it.isDiscount) (it.priceN * BigDecimal((100 - it.minusPercent) / 100.0)) else it.priceN)
            )
        } as ArrayList
        return allProducts
    }

    override fun weightMinus(product: Product): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it != product) it else it.copy(
                weightN = it.weightN - it.oneUnitN,
                sumN = (it.weightN - it.oneUnitN) * (if (it.isDiscount) (it.priceN * BigDecimal((100 - it.minusPercent) / 100.0)) else it.priceN)
            )
        } as ArrayList
        return allProducts
    }

    override fun deleteFromBasketWeightZero(): ArrayList<Product> {
        allProducts = allProducts.map {
            if (it.inBasket && it.weightN == bigDecZero) it.copy(
                inBasket = false
            ) else it
        } as ArrayList
        return allProducts
    }

    override fun countOrder(list: List<Product>): BigDecimal {
        var sumN = bigDecZeroZero
        for (item in list) {
            sumN += item.sumN
        }
        return sumN
    }

    override fun cleanBasket(): ArrayList<Product> {
        allProducts = ArrayList(
            allProducts.map {
                it.copy(
                    weightN = bigDecZero,
                    inBasket = false,
                    sumN = bigDecZeroZero
                )
            }
        )
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

    override suspend fun signInApi(request: AuthRequest) = apiService.auth(request)

    override suspend fun addHistory(dataHistory: DataHistory) {
        dataHistoryDao.insert(DataHistoryEntity.fromDto(dataHistory))
    }

    override suspend fun deleteHistoryById(id: Int) {
        dataHistoryDao.deleteHistoryById(id)
    }

}