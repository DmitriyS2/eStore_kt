package ru.netology.estore.auth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val idKey = "id"
    private val tokenKey = "token"
    private val firstnameKey = "name"
    private val usernameKey = "login"

    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val id = prefs.getLong(idKey, 0)
        val token = prefs.getString(tokenKey, null)
        val firstname = prefs.getString(firstnameKey, null)
        val username = prefs.getString(usernameKey, null)

        if (id == 0L || token == null || username == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(AuthState(id, token, firstname, username))
        }

     //   sendPushToken()
    }

    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

//    @InstallIn(SingletonComponent::class)
//    @EntryPoint
//    interface AppAuthEntryPoint {
//        fun apiService(): ApiService
//    }

    @Synchronized
    fun setAuth(id: Long, token: String, firstname:String, username:String) {
        _authStateFlow.value = AuthState(id, token, firstname, username)
        with(prefs.edit()) {
            putLong(idKey, id)
            putString(tokenKey, token)
            putString(firstnameKey, firstname)
            putString(usernameKey, username)
            apply()
        }

     //   sendPushToken()
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        with(prefs.edit()) {
            clear()
            commit()
        }
    //    sendPushToken()
    }

//    fun sendPushToken(token: String? = null) {
//        CoroutineScope(Dispatchers.Default).launch {
//            try {
//                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
//                println("token=$pushToken")
//                getApiService(context).save(pushToken)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

//    private fun getApiService(context: Context): ApiService {
//        val hiltEntryPoint = EntryPointAccessors.fromApplication(
//            context,
//            AppAuthEntryPoint::class.java
//        )
//        return hiltEntryPoint.apiService()
//    }

}
data class AuthState(val id: Long? = 0, val token: String? = null, val firstname: String? = null, val username: String?=null)