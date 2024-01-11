package ru.netology.estore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.estore.auth.AppAuth
//import ru.netology.estore.activity.allProduct
import ru.netology.estore.dto.Data
import ru.netology.estore.dto.DataOrderForHistory
import ru.netology.estore.model.FullProduct
import ru.netology.estore.dto.Product
import ru.netology.estore.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val auth: AppAuth
):ViewModel() {

    val dataFull = MutableLiveData(FullProduct())

    val dataHistoryOrders = MutableLiveData<List<DataOrderForHistory>?>()

    val emptyHistoryOfOrders:Boolean
        get()= dataHistoryOrders.value?.isEmpty() ?: true

    val amountOrder = MutableLiveData<Double>()

    init {
        getAll()
        Log.d("MyLog", "dataR = ${dataHistoryOrders.value}")
        auth.authStateFlow.value.login?.let {
            getHistoryOfOrders(auth.authStateFlow.value.login!!)
        }

    }

    fun getAll() {
        dataFull.value = FullProduct(products = repository.fillAllProducts(), status = Data.allGroup)
        repository.allProductsOriginal = dataFull.value?.products.orEmpty() as ArrayList<Product>
    }

    fun getHistoryOfOrders(login:String?) {
        if (login==null) {
            dataHistoryOrders.value = null
            return
        }
        try{
            viewModelScope.launch {
                dataHistoryOrders.value = repository.getHistoryOfOrders(login)
            }
        } catch (e:Exception) {
            throw Exception("error DataHistoryOfOrders")
        }


    }
    fun like(product: Product) {
        dataFull.value = dataFull.value?.copy(products = repository.like(product))
    }

    fun addToBasket(product: Product) {
        val list = repository.addToBasket(product)
   //     val statusBasket = list.none { it.inBasket }
        dataFull.value = dataFull.value?.copy(products = list)

      //  dataFull.value = dataFull.value?.copy(products = Data.addToBasket(product))
    }
    fun addToBasketAgain(product: Product) {
        val list = repository.addToBasketAgain(product)
   //     val statusBasket = list.none { it.inBasket }
        dataFull.value = dataFull.value?.copy(products = list)
    }

    fun deleteFromBasket(product: Product) {
        val list = repository.deleteFromBasket(product)
   //     val statusBasket = list.none { it.inBasket }
        dataFull.value = dataFull.value?.copy(products = list)
    }

    fun weightPLus(product: Product) {
        dataFull.value = dataFull.value?.copy(products = repository.weightPLus(product))
    }

    fun weightMinus(product: Product) {
        dataFull.value = dataFull.value?.copy(products = repository.weightMinus(product))
    }

    fun deleteFromBasketWeightZero() {
        val list = repository.deleteFromBasketWeightZero()
      //  val statusBasket = list.none { it.inBasket }
        dataFull.value = dataFull.value?.copy(products = list)
    }
    fun deleteFromBasketWeightZeroFromRepo():ArrayList<Product>{
        return repository.deleteFromBasketWeightZero()
    }

    fun countOrder(list: List<Product>):Double {
        return repository.countOrder(list)
    }

    fun cleanBasket() {
        dataFull.value = dataFull.value?.copy(products = dataFull.value?.products?.onEach {
            it.weight = 0.0
            it.inBasket=false
            it.sum = 0.0
        }.orEmpty())

//       dataFull.value = dataFull.value?.products. copy(products = repository.allProductsOriginal.onEach {
//           it.isFavorite = dataFull.value.products.filter { prod ->
//               prod.id==it.id
//           }
//       })
    }
}