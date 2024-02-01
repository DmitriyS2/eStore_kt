package ru.netology.estore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.estore.entity.UserEntity

@Dao
interface UserDao {
    //проверка на уникальность логин
    @Query("SELECT username FROM UserEntity")
    suspend fun getLogins():List<String>

    //добавление нового user'а
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    //дать user'а по логину
    @Query("SELECT * FROM UserEntity WHERE username = :login")
    suspend fun getUser(login:String): UserEntity?
}