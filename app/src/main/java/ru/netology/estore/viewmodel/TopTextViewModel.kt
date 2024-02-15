package ru.netology.estore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TopTextViewModel : ViewModel() {
    val text = MutableLiveData("eStore")
}