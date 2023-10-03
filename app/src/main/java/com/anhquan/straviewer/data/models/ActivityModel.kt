package com.anhquan.straviewer.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(
    tableName = "activity",
    foreignKeys = [ForeignKey(
        entity = PolylineMapModel::class,
        parentColumns = ["id"],
        childColumns = ["mapId"],
        onDelete = CASCADE,
    )]
)
data class ActivityModel(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String?,
    val distance: Double,
    @SerializedName("total_elevation_gain") val elevationGain: Double,
    @SerializedName("moving_time") val time: Int,
    @SerializedName("sport_type") val sportType: SportType,
    @SerializedName("start_date") val startDate: Date,
    @SerializedName("start_latlng") val startLatLng: List<Double>,
    @SerializedName("end_latlng") val endLatLng: List<Double>,
    @Embedded val map: PolylineMapModel,
)
