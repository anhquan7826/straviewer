package com.vnpttech.straviewer.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.vnpttech.straviewer.R
import com.vnpttech.straviewer.data.services.StravaServices
import com.vnpttech.straviewer.databinding.ActivitySplashBinding
import com.vnpttech.straviewer.dependency_injection.app_preferences.AppPreferences
import com.vnpttech.straviewer.ui.home.HomeActivity
import com.vnpttech.straviewer.ui.login.LoginActivity
import com.vnpttech.straviewer.utils.delayFor
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject lateinit var preferences: AppPreferences
    @Inject lateinit var services: StravaServices

    private lateinit var binding: ActivitySplashBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        delayFor(1, TimeUnit.SECONDS) {
            val expiresIn = preferences.getInt("expires_in")
            if (expiresIn == -1) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
            if (Date(expiresIn.toLong() * 1000).before(Date())) {
                services.reauthorize(
                    clientId = resources.getString(R.string.client_id),
                    clientSecret = resources.getString(R.string.client_secret),
                    refreshToken = preferences.getString("refresh_token")!!
                ).subscribeOn(Schedulers.io()).subscribe({
                    with(preferences) {
                        putInt("expires_at", it.expiresAt)
                        putInt("expires_in", it.expiresIn)
                        putString("refresh_token", it.refreshToken)
                        putString("access_token", it.accessToken)
                    }
                    openHomeActivity()
                }, {
                    openLoginActivity()
                })
            } else {
                openHomeActivity()
            }
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