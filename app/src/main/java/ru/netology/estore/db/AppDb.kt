package ru.netology.estore.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.netology.estore.dao.UserDao
import ru.netology.estore.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun userDao(): UserDao

}