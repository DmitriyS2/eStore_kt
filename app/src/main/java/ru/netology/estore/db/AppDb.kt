package ru.netology.estore.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.netology.estore.dao.DataHistoryDao
import ru.netology.estore.dao.UserDao
import ru.netology.estore.entity.DataHistoryEntity
import ru.netology.estore.entity.UserEntity

@Database(entities = [UserEntity::class, DataHistoryEntity::class], version = 2, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dataHistoryDao(): DataHistoryDao
}