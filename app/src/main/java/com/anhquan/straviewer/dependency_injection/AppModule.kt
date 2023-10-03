package com.anhquan.straviewer.dependency_injection

import android.content.Context
import androidx.room.Room
import com.anhquan.straviewer.data.database.AppDatabase
import com.anhquan.straviewer.data.services.StravaServices
import com.anhquan.straviewer.dependency_injection.app_preferences.AppPreferences
import com.anhquan.straviewer.dependency_injection.app_preferences.AppPreferencesImpl
import com.anhquan.straviewer.dependency_injection.interceptor.ApplicationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Singleton
    @Provides
    fun provideStravaServices(applicationInterceptor: ApplicationInterceptor): StravaServices {
        val networkInterceptor = HttpLoggingInterceptor()
        networkInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(applicationInterceptor)
            .addNetworkInterceptor(networkInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://www.strava.com/api/v3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(StravaServices::class.java)
    }

    @Provides
    fun provideApplicationInterceptor(appPreferences: AppPreferences): ApplicationInterceptor {
        return ApplicationInterceptor(appPreferences)
    }

    @Singleton
    @Provides
    fun provideAppPreferences(appPreferences: AppPreferencesImpl): AppPreferences {
        return appPreferences
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "StraViewer"
        ).build()
    }
}