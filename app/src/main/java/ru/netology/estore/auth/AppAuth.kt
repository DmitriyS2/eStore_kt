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
    private val firstNameKey = "name"
    private val usernameKey = "login"

    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val id = prefs.getLong(idKey, 0)
        val token = prefs.getString(tokenKey, null)
        val firstName = prefs.getString(firstNameKey, null)
        val username = prefs.getString(usernameKey, null)

        if (id == 0L || token == null || username == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(AuthState(id, token, firstName, username))
        }
    }

    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Long, token: String, firstName: String, username: String) {
        _authStateFlow.value = AuthState(id, token, firstName, username)
        with(prefs.edit()) {
            putLong(idKey, id)
            putString(tokenKey, token)
            putString(firstNameKey, firstName)
            putString(usernameKey, username)
            apply()
        }
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        with(prefs.edit()) {
            clear()
            commit()
        }
    }

}

data class AuthState(
    val id: Long? = 0,
    val token: String? = null,
    val firstName: String? = null,
    val username: String? = null
)