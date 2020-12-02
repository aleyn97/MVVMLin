package com.pcl.mvvm.di

import android.content.Context
import androidx.room.Room
import com.pcl.mvvm.data.db.LinDatabase
import com.pcl.mvvm.data.db.dao.HomeDao
import com.pcl.mvvm.data.db.migration.MIGRATION
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 *   @auther : Aleyn
 *   time   : 2020/11/27
 */
@Module
@InstallIn(ApplicationComponent::class)
object MainModule {

    @Singleton
    @Provides
    fun roomData(@ApplicationContext application: Context): LinDatabase {
        return Room.databaseBuilder(
            application,
            LinDatabase::class.java,
            "lin_db"
        )
            .addMigrations(MIGRATION.MIGRATION_1_2)
            .build()
    }

    @Singleton
    @Provides
    fun locaData(database: LinDatabase): HomeDao {
        return database.homeLocaData()
    }

}