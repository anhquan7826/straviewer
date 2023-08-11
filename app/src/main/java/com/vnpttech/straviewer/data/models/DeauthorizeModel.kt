package com.vnpttech.straviewer.data.models

import com.google.gson.annotations.SerializedName

data class DeauthorizeModel(
    @SerializedName("access_token") val accessToken: String
)