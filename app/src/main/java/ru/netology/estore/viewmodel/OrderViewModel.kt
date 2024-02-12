package ru.netology.estore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.estore.dto.DataLang
import ru.netology.estore.dto.DataRus

class OrderViewModel : ViewModel() {

    val showPoint1 = MutableLiveData(0)
    val showPoint2 = MutableLiveData(0)
    val showPoint3 = MutableLiveData(0)
    val showPoint4 = MutableLiveData(0)
    val showPoint5 = MutableLiveData(0)
    val goToFinalOrder = MutableLiveData(0)
    var typeOfDelivery: String = "Сами заберете или Вам привезти?"
    var addressPickUp: String = "Выберите магазин, откуда заберете"
    var addressDelivery: String = "Куда Вам привезти?"
    var typeOfPayment: String = "Выберите способ оплаты"
    var flagPickUp = false

    init {
        cancelOrder(DataRus)
    }

    fun cancelOrder(dataLang: DataLang) {
        showPoint1.value = 0
        showPoint2.value = 0
        showPoint3.value = 0
        showPoint4.value = 0
        showPoint5.value = 0
        goToFinalOrder.value = 0
        typeOfDelivery = dataLang.typeOfDelivery
        addressPickUp = dataLang.addressPickUp
        addressDelivery = dataLang.addressDelivery
        typeOfPayment = dataLang.typeOfPayment
        flagPickUp = false
    }
}