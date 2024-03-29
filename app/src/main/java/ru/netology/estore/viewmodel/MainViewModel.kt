package ru.netology.estore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.estore.auth.AppAuth
import ru.netology.estore.dto.DataHistory
import ru.netology.estore.dto.DataLang
import ru.netology.estore.dto.DataRus
import ru.netology.estore.model.FullProduct
import ru.netology.estore.dto.Product
import ru.netology.estore.repository.ProductRepository
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val auth: AppAuth
) : ViewModel() {

    val dataFull: MutableLiveData<FullProduct> = MutableLiveData()

    val dataHistoryOrders = MutableLiveData<List<DataHistory>?>()

    val emptyHistoryOfOrders: Boolean
        get() = dataHistoryOrders.value?.isEmpty() ?: true

    val amountOrderN = MutableLiveData<BigDecimal>()

    val counterHit
        get() = dataFull.value?.products
            ?.count {
                it.isHit
            }
            .toString()
    val counterDiscount
        get() = dataFull.value?.products
            ?.count {
                it.isDiscount
            }
            .toString()
    val counterFavorite: MutableLiveData<String> = MutableLiveData(null)

    val pointBottomMenu: MutableLiveData<Int> = MutableLiveData(-1)

    val language: MutableLiveData<String?> = MutableLiveData("ru")
    var dataLanguage: DataLang = DataRus

    init {
        getAll(DataRus)
        auth.authStateFlow.value.username?.let {
            getHistory(auth.authStateFlow.value.username)
        }
        counterFavorite.value = dataFull.value?.products?.count {
            it.isFavorite
        }.toString()
    }

    private fun getAll(dataLang: DataLang) {
        dataFull.value =
            FullProduct(products = repository.fillAllProducts(dataLang), status = dataLang.allGroup)
        initAllProductsOriginal()
    }

    private fun initAllProductsOriginal() {
        repository.allProductsOriginal = dataFull.value?.products.orEmpty() as ArrayList<Product>
    }

    fun reNewDataFull() {
        dataFull.value = dataFull.value?.copy(
            products = repository.reNewDataFull(),
            status = dataLanguage.allGroup
        )
        counterFavorite.value = dataFull.value?.products
            ?.count {
                it.isFavorite
            }.toString()
    }

    fun changeLang() {
        dataFull.value = dataFull.value?.copy(
            products = repository.changeLang(dataLanguage),
            status = dataLanguage.allGroup
        )
        initAllProductsOriginal()
    }

    fun like(product: Product) {
        dataFull.value = dataFull.value?.copy(products = repository.like(product))
        counterFavorite.value = dataFull.value?.products
            ?.count {
                it.isFavorite
            }.toString()
    }

    fun addToBasket(product: Product) {
        val list = repository.addToBasket(product)
        dataFull.value = dataFull.value?.copy(products = list)
    }

    fun addToBasketAgain(product: Product) {
        val list = repository.addToBasketAgain(product)
        dataFull.value = dataFull.value?.copy(products = list)
    }

    fun deleteFromBasket(product: Product) {
        val list = repository.deleteFromBasket(product)
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
        dataFull.value = dataFull.value?.copy(products = list)
    }

    fun deleteFromBasketWeightZeroFromRepo(): ArrayList<Product> {
        return repository.deleteFromBasketWeightZero()
    }

    fun countOrder(list: List<Product>): BigDecimal {
        return repository.countOrder(list).setScale(2, RoundingMode.HALF_UP)
    }

    fun cleanBasket() {
        dataFull.value = dataFull.value?.copy(products = repository.cleanBasket())
    }

    fun getHistory(username: String?) {
        if (username == null) {
            dataHistoryOrders.value = null
            return
        }
        viewModelScope.launch {
            try {
                dataHistoryOrders.value = repository.getHistory(username)
            } catch (e: Exception) {
                throw Exception("error GetDataHistoryOfOrders")
            }
        }
    }

    fun addHistory(dataHistory: DataHistory) {
        viewModelScope.launch {
            try {
                repository.addHistory(dataHistory)
            } catch (e: Exception) {
                throw Exception("error AddDataHistoryOfOrders")
            }
        }
    }

    fun deleteHistoryById(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteHistoryById(id)
                getHistory(auth.authStateFlow.value.username)

            } catch (e: Exception) {
                throw Exception("error DeleteOrderById")
            }
        }
    }
}