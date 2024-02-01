package ru.netology.estore.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.estore.api.ApiService
import ru.netology.estore.auth.AppAuth
import ru.netology.estore.dto.AuthRequest
import ru.netology.estore.repository.ProductRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val auth: AppAuth,
    private val apiService: ApiService,
): ViewModel() {

    val stateAuth: MutableLiveData<Int> = MutableLiveData(0)

    fun signIn(username: String, password: String) {
        try{
            viewModelScope.launch {
                val user = repository.checkSignIn(username)
                if(user==null) {
                    stateAuth.value = -2
                } else {
                    if (user.password==password) {
                        auth.setAuth(user.id, user.token, user.firstName, user.username)
                        stateAuth.value = 1
                    } else {
                        stateAuth.value = -2
                    }
                }
//               user?.let {
//                   if (it.password==password) {
//                       auth.setAuth(it.id, it.token, it.firstName, it.username)
//                   }
//               }
            }
        } catch (e:Exception) {
            throw Exception("error SignIn")
        }
    }

    fun signInApi(username: String, password: String) {
        viewModelScope.launch {
            try {
                val request = AuthRequest(username, password)
                val response = apiService.auth(request)
                if (!response.isSuccessful) {
                    Log.d("MyLog", "signInApiError")
                    stateAuth.value = -1
                    return@launch
              //      throw Error(response.message())
                }
//                val body =
//                    response.body() ?: throw Error(response.message())
                val body =
                    response.body()
                Log.d("MyLog", "response: id=${body?.id} firstname=${body?.firstName}, username=${body?.username}, ")
                if(body==null) {
                    stateAuth.value = -1
                } else {
                    auth.setAuth(body.id, body.token, body.firstName, body.username)
                    stateAuth.value = 1
                }
//                body?.let{
//                    auth.setAuth(body.id, body.token, body.firstName, body.username)
//                    stateAuth.value = 1
//                }
            } catch (e: IOException) {
                throw Error(e)
            }
        }
    }
}