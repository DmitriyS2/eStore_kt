package ru.netology.estore.repository

import ru.netology.estore.dto.DataHistory
import ru.netology.estore.dto.Product
import ru.netology.estore.dto.User

interface ProductRepository {

    var allProductsOriginal:ArrayList<Product>

    fun fillAllProducts():ArrayList<Product>
    fun like(product: Product):ArrayList<Product>
    fun addToBasket(product: Product):ArrayList<Product>
    fun addToBasketAgain(product: Product):ArrayList<Product>
    fun deleteFromBasket(product: Product):ArrayList<Product>
    fun weightPLus(product: Product):ArrayList<Product>
    fun weightMinus(product: Product):ArrayList<Product>
    fun deleteFromBasketWeightZero():ArrayList<Product>
    fun countOrder(list:List<Product>):Double
    fun cleanBasket():ArrayList<Product>
    fun reNewDataFull(): ArrayList<Product>
    suspend fun checkSignIn(login:String): User?
    suspend fun signUp(login: String, password: String, name: String):User?
    suspend fun getHistory(login: String): List<DataHistory>?
    suspend fun addHistory(dataHistory: DataHistory)
}