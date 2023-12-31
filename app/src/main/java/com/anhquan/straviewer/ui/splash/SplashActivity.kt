package com.anhquan.straviewer.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.vnpttech.straviewer.BuildConfig
import com.anhquan.straviewer.data.services.StravaServices
import com.vnpttech.straviewer.databinding.ActivitySplashBinding
import com.anhquan.straviewer.dependency_injection.app_preferences.AppPreferences
import com.anhquan.straviewer.ui.home.HomeActivity
import com.anhquan.straviewer.ui.login.LoginActivity
import com.anhquan.straviewer.utils.delayFor
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
                startActivity(Intent(this, LoginActivity::class.java))
            }
            if (Date(expiresIn.toLong() * 1000).before(Date())) {
                services.reauthorize(
                    clientId = BuildConfig.CLIENT_ID,
                    clientSecret = BuildConfig.CLIENT_SECRET,
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
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun openHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}