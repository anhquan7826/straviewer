package com.vnpttech.straviewer.data.models

import com.google.gson.annotations.SerializedName

data class PolylineMapModel(
    val id: String,
    val polyline: String,
    @SerializedName("summary_polyline") val summaryPolyline: String,
)