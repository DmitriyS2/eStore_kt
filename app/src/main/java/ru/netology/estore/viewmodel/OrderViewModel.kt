package ru.netology.estore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation.findNavController


import ru.netology.estore.R
import ru.netology.estore.dto.Data
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class OrderViewModel : ViewModel() {

    var showPoint1 = MutableLiveData(0)
    var showPoint2 = MutableLiveData(0)
    var showPoint3 = MutableLiveData(0)
    var showPoint4 = MutableLiveData(0)
    var showPoint5 = MutableLiveData(0)
    var goToFinalOrder = MutableLiveData(0)
    var typeOfDelivery: String = "Сами заберете или Вам привезти?"
    var addressPickUp: String = "Выберите магазин, откуда заберете"
    var addressDelivery: String = "Куда Вам привезти?"
    var typeOfPayment: String = "Выберите способ оплаты"
    var flagPickUp = false


    val timeNow: OffsetDateTime
        get() = OffsetDateTime.now()

    fun cancelOrder() {
        showPoint1.value = 0
        showPoint2.value = 0
        showPoint3.value = 0
        showPoint4.value = 0
        showPoint5.value = 0
        goToFinalOrder.value = 0
        typeOfDelivery = "Сами заберете или Вам привезти?"
        addressPickUp = "Выберите магазин, откуда заберете"
        addressDelivery = "Куда Вам привезти?"
        typeOfPayment = "Выберите способ оплаты"
        flagPickUp = false
        // topTextViewModel.text.value = Data.basketGroup
        // findNavController().navigate(R.id.fragmentForBasket)
    }
}