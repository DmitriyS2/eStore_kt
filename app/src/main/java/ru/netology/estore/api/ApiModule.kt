package ru.netology.estore.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.estore.BuildConfig
import ru.netology.estore.auth.AppAuth
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object ApiServiceModule {

    private const val BASE_URL = "https://dummyjson.com/"

    @Provides
    @Singleton
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        logging: HttpLoggingInterceptor,
        appAuth: AppAuth
    ): OkHttpClient = OkHttpClient.Builder()
//        .addInterceptor { chain ->
//            appAuth.authStateFlow.value.token?.let { token ->
//                val newRequest = chain.request().newBuilder()
//                    .addHeader("Authorization", token)
//                    .build()
//                return@addInterceptor chain.proceed(newRequest)
//            }
//            chain.proceed(chain.request())
//        }
        .addInterceptor(logging)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService = retrofit.create()
}