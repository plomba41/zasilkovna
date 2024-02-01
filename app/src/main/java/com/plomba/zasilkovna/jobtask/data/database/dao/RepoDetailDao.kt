package com.plomba.zasilkovna.jobtask.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.plomba.zasilkovna.jobtask.data.database.entity.RepoDetailEntity

@Dao
interface RepoDetailDao {

    @Query("SELECT * FROM repo_details")
    fun getAll(): List<RepoDetailEntity>

    @Query("SELECT * FROM repo_details WHERE id = (:repoId)")
    fun getSingle(repoId: String): RepoDetailEntity

    @Upsert
    fun upsert(detail: RepoDetailEntity): Long

    @Delete
    fun delete(detail: RepoDetailEntity): Int
}