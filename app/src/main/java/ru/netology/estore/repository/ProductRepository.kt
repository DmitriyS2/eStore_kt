package ru.netology.estore.repository

import ru.netology.estore.dto.Product
import ru.netology.estore.dto.User

interface ProductRepository {

    fun fillAllProducts():ArrayList<Product>
    fun like(product: Product):ArrayList<Product>
    fun addToBasket(product: Product):ArrayList<Product>
    fun addToBasketAgain(product: Product):ArrayList<Product>
    fun deleteFromBasket(product: Product):ArrayList<Product>
    fun weightPLus(product: Product):ArrayList<Product>
    fun weightMinus(product: Product):ArrayList<Product>
    fun deleteFromBasketWeightZero():ArrayList<Product>
    fun countOrder(list:List<Product>):Double

    suspend fun checkSignIn(login:String, password:String): User?
}