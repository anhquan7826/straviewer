package com.vnpttech.straviewer.data.models

import com.google.gson.annotations.SerializedName

data class AuthorizeModel(
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_at") val expiresAt: Int,
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("access_token") val accessToken: String,
    val athlete: AthleteModel?
)
