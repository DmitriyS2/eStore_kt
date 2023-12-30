package ru.netology.estore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
//import ru.netology.estore.activity.allProduct
import ru.netology.estore.dto.Data
import ru.netology.estore.model.FullProduct
import ru.netology.estore.dto.Product
import ru.netology.estore.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ProductRepository
):ViewModel() {

    //private val allProduct = Data.fillAllProducts()
    //val data = MutableLiveData(allProduct)

    val dataFull = MutableLiveData(FullProduct())

    val amountOrder = MutableLiveData<Double>()

    init {
        getAll()
    }

    fun getAll() {
        dataFull.value = FullProduct(products = repository.fillAllProducts(), status = Data.allGroup)
    }
    fun like(product: Product) {
        dataFull.value = dataFull.value?.copy(products = repository.like(product))
    }

    fun addToBasket(product: Product) {
        val list = repository.addToBasket(product)
        val statusBasket = list.none { it.inBasket }
        dataFull.value = dataFull.value?.copy(products = list, isEmptyBasket = statusBasket)

      //  dataFull.value = dataFull.value?.copy(products = Data.addToBasket(product))
    }
    fun addToBasketAgain(product: Product) {
        val list = repository.addToBasketAgain(product)
        val statusBasket = list.none { it.inBasket }
        dataFull.value = dataFull.value?.copy(products = list, isEmptyBasket = statusBasket)

   //     dataFull.value = dataFull.value?.copy(products = Data.addToBasketAgain(product))
    }

    fun deleteFromBasket(product: Product) {
        val list = repository.deleteFromBasket(product)
        val statusBasket = list.none { it.inBasket }
        dataFull.value = dataFull.value?.copy(products = list, isEmptyBasket = statusBasket)

 //       dataFull.value = dataFull.value?.copy(products = Data.deleteFromBasket(product))
    }

    fun weightPLus(product: Product) {
        dataFull.value = dataFull.value?.copy(products = repository.weightPLus(product))
    }

    fun weightMinus(product: Product) {
        dataFull.value = dataFull.value?.copy(products = repository.weightMinus(product))
    }

    fun deleteFromBasketWeightZero() {
        val list = repository.deleteFromBasketWeightZero()
        val statusBasket = list.none { it.inBasket }
        dataFull.value = dataFull.value?.copy(products = list, isEmptyBasket = statusBasket)

     //   dataFull.value = dataFull.value?.copy(products = Data.deleteFromBasketWeightZero())
    }
    fun deleteFromBasketWeightZeroFromRepo():ArrayList<Product>{
        return repository.deleteFromBasketWeightZero()
    }

    fun countOrder(list: List<Product>):Double {
        return repository.countOrder(list)
    }
}