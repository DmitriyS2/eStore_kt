package ru.netology.estore.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.netology.estore.dao.DataOrderForHistoryDao
import ru.netology.estore.dao.UserDao
import ru.netology.estore.entity.DataOrderForHistoryEntity
import ru.netology.estore.entity.UserEntity

@Database(entities = [UserEntity::class, DataOrderForHistoryEntity::class], version = 4, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dataOrderForHistoryDao(): DataOrderForHistoryDao
}