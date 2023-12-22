package ru.netology.estore.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.netology.estore.auth.AppAuth
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    auth: AppAuth
) : ViewModel() {
    val data = auth.authStateFlow

    val authenticated:Boolean
        get() = data.value.id !=0L

//    val data: LiveData<AuthState> = AppAuth.getInstance()
//        .authStateFlow
//        .asLiveData(Dispatchers.Default)

//    val authenticated: Boolean
//        get() = AppAuth.getInstance().authStateFlow.value.id != 0L
}