package ru.netology.estore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel:ViewModel() {

    var showPoint1 = MutableLiveData(0)
    var showPoint2 = MutableLiveData(0)
    var showPoint3 = MutableLiveData(0)
    var showPoint4 = MutableLiveData(0)
    var typeOfDelivery:String = "Сами заберете или Вам привезти?"
    var addressPickUp:String = "Выберите магазин, откуда заберете"
    var addressDelivery:String = "Куда Вам привезти?"
    var timeDelivery = ""
    var typeOfPayment = ""
}