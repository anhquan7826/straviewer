package com.anhquan.straviewer.data.auth

import android.content.Intent
import android.net.Uri

class StravaOAuth private constructor() {
    companion object {
        fun createInstance(): StravaOAuth {
            return StravaOAuth()
        }
    }

    private val uri = Uri.parse("https://www.strava.com/oauth/mobile/authorize").buildUpon()

    fun setClientId(clientId: String): StravaOAuth {
        uri.appendQueryParameter("client_id", clientId)
        return this
    }

    fun setRedirectUri(uri: String): StravaOAuth {
        this.uri.appendQueryParameter("redirect_uri", uri)
        return this
    }

    fun setResponseType(type: String): StravaOAuth {
        uri.appendQueryParameter("response_type", type)
        return this
    }

    fun setApprovalPrompt(prompt: String): StravaOAuth {
        uri.appendQueryParameter("approval_prompt", prompt)
        return this
    }

    fun setScope(scopes: List<StravaScope>): StravaOAuth {
        uri.appendQueryParameter("scope", scopes.joinToString(separator = ","))
        return this
    }

    fun buildIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, uri.build())
    }
}