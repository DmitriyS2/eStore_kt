package ru.netology.estore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.estore.auth.AppAuth
import ru.netology.estore.repository.ProductRepository
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val auth: AppAuth,
    private val repository: ProductRepository
): ViewModel() {

    fun signUp(login: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                val user = repository.checkSignIn(login)
                if (user==null) {
                    val newUser = repository.signUp(login, password, name)
                    newUser?.let {
                        auth.setAuth(it.id, it.token, it.firstName, it.username)
                    }
                }
            } catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }
}