package ru.netology.estore.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.estore.auth.AppAuth
import javax.inject.Inject

@HiltAndroidApp
class EStoreApplication:Application() {
//    override fun onCreate() {
//        super.onCreate()
//        AppAuth.initApp(this)
//    }

//    private val appScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var auth: AppAuth

    override fun onCreate() {
        super.onCreate()
        setupAuth()
    }

    private fun setupAuth() {
  //      appScope.launch {
 //           auth.sendPushToken()
   //     }
    }
}