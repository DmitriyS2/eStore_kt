package ru.netology.estore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.estore.auth.AppAuth
import ru.netology.estore.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val auth: AppAuth
): ViewModel() {

    fun signIn(login: String, password: String) {
        try{
            viewModelScope.launch {
                val user = repository.checkSignIn(login)
               user?.let {
                   if (it.password==password) {
                       auth.setAuth(it.id, it.token, it.name, it.login)
                   }
               }
            }
        } catch (e:Exception) {
            throw Exception("error SignIn")
        }
    }
}