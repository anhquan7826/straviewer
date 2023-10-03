package com.anhquan.straviewer.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "athlete")
data class AthleteModel(
    @PrimaryKey val id: Int,
    val username: String?,
    val firstname: String?,
    val lastname: String?,
    val state: String?,
    val country: String?,
    val profile: String?
)