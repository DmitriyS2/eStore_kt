package ru.netology.estore.auth

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    private val idKey = "id"
    private val tokenKey = "token"
    private val nameKey = "name"

    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val id = prefs.getLong(idKey, 0)
        val token = prefs.getString(tokenKey, null)
        val name = prefs.getString(nameKey, null)

        if (id == 0L || token == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(AuthState(id, token, name))
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
    fun setAuth(id: Long, token: String, name:String) {
        _authStateFlow.value = AuthState(id, token, name)
        with(prefs.edit()) {
            putLong(idKey, id)
            putString(tokenKey, token)
            putString(nameKey, name)
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

//    companion object {
//        @Volatile
//        private var instance: AppAuth? = null
//
//        fun getInstance(): AppAuth = synchronized(this) {
//            instance ?: throw IllegalStateException(
//                "AppAuth is not initialized, you must call AppAuth.initializeApp(Context context) first."
//            )
//        }
//
//        fun initApp(context: Context): AppAuth = instance ?: synchronized(this) {
//            instance ?: buildAuth(context).also { instance = it }
//        }
//
//        private fun buildAuth(context: Context): AppAuth = AppAuth(context)
//    }
}
data class AuthState(val id: Long = 0, val token: String? = null, val name: String? = null)