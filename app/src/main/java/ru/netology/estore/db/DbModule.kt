package ru.netology.estore.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.estore.dao.DataHistoryDao
import ru.netology.estore.dao.UserDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): AppDb {
        return Room.databaseBuilder(context, AppDb::class.java, "app.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(
        appDb: AppDb
    ): UserDao = appDb.userDao()

    @Provides
    @Singleton
    fun provideDataHistoryDao(
        appDb: AppDb
    ): DataHistoryDao = appDb.dataHistoryDao()

}