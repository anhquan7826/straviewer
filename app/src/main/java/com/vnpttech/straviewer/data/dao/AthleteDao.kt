package com.vnpttech.straviewer.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.vnpttech.straviewer.data.models.AthleteModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface AthleteDao {
    @Query("SELECT * FROM athlete WHERE id = :id")
    fun getAthlete(id: Int): Single<AthleteModel>

    @Insert(onConflict = REPLACE)
    fun setAthlete(athlete: AthleteModel): Completable

    @Delete
    fun deleteAthlete(athlete: AthleteModel): Completable
}