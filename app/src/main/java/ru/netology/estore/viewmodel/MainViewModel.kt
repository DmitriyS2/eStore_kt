package ru.netology.estore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.estore.activity.allProduct
import ru.netology.estore.dto.Data
import ru.netology.estore.dto.Product
import java.util.Collections.list

class MainViewModel:ViewModel() {

    //val products = Data.fillAllProducts()

    val data = MutableLiveData(allProduct)

}