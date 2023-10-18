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
        //dataFull.value?.products = Data.like(product)
    }

}