package com.anhquan.straviewer.ui.login.callback

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vnpttech.straviewer.BuildConfig
import com.vnpttech.straviewer.R
import com.anhquan.straviewer.data.database.AppDatabase
import com.anhquan.straviewer.data.services.StravaServices
import com.anhquan.straviewer.dependency_injection.app_preferences.AppPreferences
import com.anhquan.straviewer.ui.home.HomeActivity
import com.anhquan.straviewer.ui.login.LoginActivity
import com.anhquan.straviewer.utils.log
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class StravaOAuthCallbackActivity : AppCompatActivity() {
    @Inject lateinit var stravaServices: StravaServices

    @Inject lateinit var preferences: AppPreferences

    @Inject lateinit var database: AppDatabase

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_strava_oauth_callback)

        val code = intent.data?.getQueryParameter("code")
        if (code == null) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        } else {
            stravaServices.authorize(
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                code
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
                log(it)
                with(preferences) {
                    putInt("expires_at", it.expiresAt)
                    putInt("expires_in", it.expiresIn)
                    putString("refresh_token", it.refreshToken)
                    putString("access_token", it.accessToken)
                    if (it.athlete != null) putInt("athlete_id", it.athlete.id)
                }
                if (it.athlete != null) {
                    database.athleteDao().setAthlete(it.athlete).subscribeOn(Schedulers.io()).subscribe {
                        openHomeActivity()
                    }
                } else {
                    openHomeActivity()
                }
            }, {
                with(Toast(this)) {
                    setText("Error: ${it.message ?: "Unknown!"}")
                    show()
                }
                openLoginActivity()
            })
        }
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun openHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}