package ru.netology.estore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.estore.entity.DataOrderForHistoryEntity
import ru.netology.estore.entity.UserEntity

@Dao
interface DataOrderForHistoryDao {

    //добавление нового заказа в историю
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dataOrderForHistory: DataOrderForHistoryEntity)

    //дать все заказы по логину
    @Query("SELECT * FROM DataOrderForHistoryEntity WHERE login = :login")
    suspend fun getDataOrderForHistory(login:String): List<DataOrderForHistoryEntity>
}