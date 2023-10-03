package com.anhquan.straviewer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anhquan.straviewer.data.dao.AthleteDao
import com.anhquan.straviewer.data.models.AthleteModel

@Database(entities = [AthleteModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun athleteDao(): AthleteDao
}