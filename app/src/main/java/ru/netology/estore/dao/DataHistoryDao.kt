package ru.netology.estore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.estore.entity.DataHistoryEntity

@Dao
interface DataHistoryDao {

    //добавление нового заказа в историю
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dataHistory: DataHistoryEntity)

    //дать все заказы по логину
    @Query("SELECT * FROM DataHistoryEntity WHERE login = :login")
    suspend fun getDataHistory(login:String): List<DataHistoryEntity>

    //удалить заказ из истории заказов
    @Query("DELETE FROM DataHistoryEntity WHERE id = :id")
    suspend fun deleteHistoryById(id:Int)
}