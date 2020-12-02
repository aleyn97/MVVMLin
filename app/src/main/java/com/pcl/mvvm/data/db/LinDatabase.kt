package com.pcl.mvvm.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils
import com.pcl.mvvm.data.db.dao.HomeDao
import com.pcl.mvvm.data.db.migration.MIGRATION
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.network.entity.HomeListBean

/**
 *   @auther : Aleyn
 *   time   : 2020/03/12
 */
@Database(entities = [HomeListBean::class, BannerBean::class], version = 2, exportSchema = false)
abstract class LinDatabase : RoomDatabase() {

    abstract fun homeLocaData(): HomeDao
}