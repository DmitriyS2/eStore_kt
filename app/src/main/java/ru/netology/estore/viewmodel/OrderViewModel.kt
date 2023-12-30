package ru.netology.estore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel:ViewModel() {

    var showPoint1 = MutableLiveData(0)
    var showPoint2 = MutableLiveData(0)
    var typeOfDelivery:String = "Сами заберете или Вам привезти?"
    var addressDelivery:String = ""
    var timeDelivery = ""
    var typeOfPayment = ""
}