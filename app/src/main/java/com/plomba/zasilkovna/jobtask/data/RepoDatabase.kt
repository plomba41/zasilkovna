package com.plomba.zasilkovna.jobtask.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.plomba.zasilkovna.jobtask.data.database.dao.RepoDetailDao
import com.plomba.zasilkovna.jobtask.data.database.entity.RepoDetailEntity

@Database(entities = [RepoDetailEntity::class], version = 1)
abstract class RepoDatabase: RoomDatabase() {
    abstract fun repoDetailDao(): RepoDetailDao

}