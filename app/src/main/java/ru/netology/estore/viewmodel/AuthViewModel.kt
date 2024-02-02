package ru.netology.estore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ru.netology.estore.auth.AppAuth
import ru.netology.estore.auth.AuthState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    auth: AppAuth
) : ViewModel() {
    val data = auth.authStateFlow

    val authenticated:Boolean
        get() = data.value.id !=0L

//        val dataAuth: LiveData<AuthState> = auth
//        .authStateFlow
//        .asLiveData(Dispatchers.Default)
//
//    val authenticatedAuth: Boolean
//        get() = dataAuth.value?.id != 0L

}