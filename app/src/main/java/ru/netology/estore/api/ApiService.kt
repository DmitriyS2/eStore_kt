package ru.netology.estore.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.netology.estore.dto.AuthRequest
import ru.netology.estore.dto.User

interface ApiService {
    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): Response<User>
}