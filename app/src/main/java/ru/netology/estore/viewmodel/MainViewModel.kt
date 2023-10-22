package ru.netology.estore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import ru.netology.estore.activity.allProduct
import ru.netology.estore.dto.Data
import ru.netology.estore.model.FullProduct
import ru.netology.estore.dto.Product

class MainViewModel:ViewModel() {

    //private val allProduct = Data.fillAllProducts()
    //val data = MutableLiveData(allProduct)

    val dataFull = MutableLiveData(FullProduct())

    init {
        getAll()
    }

    fun getAll() {
        dataFull.value = FullProduct(products = Data.fillAllProducts(), status = Data.allGroup)
    }
    fun like(product: Product) {
        dataFull.value = dataFull.value?.copy(products = Data.like(product))
    }

    fun addToBasket(product: Product) {
        dataFull.value = dataFull.value?.copy(products = Data.addToBasket(product))
    }
    fun addToBasketAgain(product: Product) {
        dataFull.value = dataFull.value?.copy(products = Data.addToBasketAgain(product))
    }

    fun deleteFromBasket(product: Product) {
//        val list = Data.deleteFromBasket(product)
//        dataFull.value = dataFull.value?.copy(products = Data.deleteFromBasket(product), isEmptyBasket = list.isEmpty())

        dataFull.value = dataFull.value?.copy(products = Data.deleteFromBasket(product))
    }

    fun weightPLus(product: Product) {
        dataFull.value = dataFull.value?.copy(products = Data.weightPLus(product))
    }

    fun weightMinus(product: Product) {
        dataFull.value = dataFull.value?.copy(products = Data.weightMinus(product))
    }

    fun deleteFromBasketWeightZero() {
        dataFull.value = dataFull.value?.copy(products = Data.deleteFromBasketWeightZero())
    }

}