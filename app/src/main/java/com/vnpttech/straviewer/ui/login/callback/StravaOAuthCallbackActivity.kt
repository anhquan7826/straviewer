package com.vnpttech.straviewer.ui.login.callback

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vnpttech.straviewer.R
import com.vnpttech.straviewer.data.database.AppDatabase
import com.vnpttech.straviewer.data.services.StravaServices
import com.vnpttech.straviewer.dependency_injection.app_preferences.AppPreferences
import com.vnpttech.straviewer.ui.home.HomeActivity
import com.vnpttech.straviewer.ui.login.LoginActivity
import com.vnpttech.straviewer.utils.log
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
                clientId = resources.getString(R.string.client_id),
                clientSecret = resources.getString(R.string.client_secret),
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
                with(Toast(applicationContext)) {
                    setText("Error: ${it.message ?: "Unknown!"}")
                    show()
                }
                openLoginActivity()
            })
        }
    }

    private fun openLoginActivity() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }

    private fun openHomeActivity() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()
    }
}