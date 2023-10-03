package com.anhquan.straviewer.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vnpttech.straviewer.BuildConfig
import com.vnpttech.straviewer.R
import com.anhquan.straviewer.data.auth.StravaOAuth
import com.anhquan.straviewer.data.auth.StravaScope
import com.vnpttech.straviewer.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.signInButton.setOnClickListener {
            startActivity(buildOAuthIntent())
            finish()
        }
        setContentView(binding.root)
    }

    private fun buildOAuthIntent(): Intent {
        return StravaOAuth.createInstance().setClientId(BuildConfig.CLIENT_ID)
            .setRedirectUri(resources.getString(R.string.redirect_uri))
            .setResponseType(resources.getString(R.string.response_type))
            .setApprovalPrompt(resources.getString(R.string.approval_prompt))
            .setScope(listOf(StravaScope.ACTIVITY_READ_ALL)).buildIntent()
    }
}