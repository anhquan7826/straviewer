package com.anhquan.straviewer.data.services

import com.anhquan.straviewer.data.models.ActivityModel
import com.anhquan.straviewer.data.models.AuthorizeModel
import com.anhquan.straviewer.data.models.DeauthorizeModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StravaServices {
    // Authorization

    @FormUrlEncoded
    @POST(StravaAPI.AUTHORIZE)
    fun authorize(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): Single<AuthorizeModel>

    @FormUrlEncoded
    @POST(StravaAPI.AUTHORIZE)
    fun reauthorize(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): Single<AuthorizeModel>

    @FormUrlEncoded
    @POST(StravaAPI.DEAUTHORIZE)
    fun deauthorize(@Field("access_token") accessToken: String): Single<DeauthorizeModel>

    // Activity

    @GET(StravaAPI.ACTIVITIES_ALL)
    fun getAllActivities(): Single<List<ActivityModel>>

    @GET(StravaAPI.ACTIVITY_GET)
    fun getActivity(@Path("id") id: Long): Single<ActivityModel>
}