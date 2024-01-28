package ru.netology.estore.viewmodel

import android.util.Log
import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.estore.auth.AppAuth
import ru.netology.estore.dao.UserDao
import ru.netology.estore.dto.User
import ru.netology.estore.repository.ProductRepository
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
   // private val apiService:ApiService
    private val repository: ProductRepository,
    private val userDao: UserDao,
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


//        viewModelScope.launch {
//            try {
//                val response = apiService.updateUser(login, password)
//                if (!response.isSuccessful) {
//                    throw ApiError(response.code(), response.message())
//                }
//                val body =
//                    response.body() ?: throw ApiError(response.code(), response.message())
//                Log.d("MyLog", "id body=${body.id} token body=${body.token}")
//
//                auth.setAuth(body.id, body.token)
//
//            } catch (e: IOException) {
//                throw NetworkError
//            } catch (e: Exception) {
//                Log.d("MyLog", "ошибка signIn")
//                //  throw MyUnknownError
//            }
//
//        }

    }

}