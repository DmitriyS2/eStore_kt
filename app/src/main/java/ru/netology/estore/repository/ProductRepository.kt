package ru.netology.estore.repository

import retrofit2.Response
import ru.netology.estore.dto.AuthRequest
import ru.netology.estore.dto.DataHistory
import ru.netology.estore.dto.DataLang
import ru.netology.estore.dto.Product
import ru.netology.estore.dto.User
import java.math.BigDecimal

interface ProductRepository {

    var allProductsOriginal:ArrayList<Product>

    fun fillAllProducts(dataLang: DataLang):ArrayList<Product>
    fun like(product: Product):ArrayList<Product>
    fun addToBasket(product: Product):ArrayList<Product>
    fun addToBasketAgain(product: Product):ArrayList<Product>
    fun deleteFromBasket(product: Product):ArrayList<Product>
    fun weightPLus(product: Product):ArrayList<Product>
    fun weightMinus(product: Product):ArrayList<Product>
    fun deleteFromBasketWeightZero():ArrayList<Product>
 //   fun countOrder(list:List<Product>):Double
    fun countOrder(list: List<Product>):BigDecimal
    fun cleanBasket():ArrayList<Product>
    fun reNewDataFull(): ArrayList<Product>
    fun changeLang(dataLang: DataLang): ArrayList<Product>

    suspend fun checkSignIn(login:String): User?
    suspend fun signUp(login: String, password: String, name: String):User?
    suspend fun signInApi(request: AuthRequest):Response<User>

    suspend fun getHistory(login: String): List<DataHistory>?
    suspend fun addHistory(dataHistory: DataHistory)
    suspend fun deleteHistoryById(id:Int)

}