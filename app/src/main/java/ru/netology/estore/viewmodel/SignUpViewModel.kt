package ru.netology.estore.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.estore.auth.AppAuth
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
  //  private val apiService: ApiService,
    private val auth: AppAuth
): ViewModel() {

    fun signUp(login: String, password: String, name: String) {
//        viewModelScope.launch {
//            try {
//                val response = apiService.registerUser(login, password, name)
//                if (!response.isSuccessful) {
//                    throw ApiError(response.code(), response.message())
//                }
//                val body =
//                    response.body() ?: throw ApiError(response.code(), response.message())
//                Log.d("MyLog", "signUp id body=${body.id} token body=${body.token}")
//
//                auth.setAuth(body.id, body.token)
//
//            } catch (e: IOException) {
//                throw NetworkError
//            } catch (e: Exception) {
//                Log.d("MyLog", "ошибка signUp")
//                //  throw MyUnknownError
//            }
//        }
    }
}