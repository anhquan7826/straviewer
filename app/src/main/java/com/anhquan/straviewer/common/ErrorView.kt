package com.anhquan.straviewer.common

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.vnpttech.straviewer.R

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        inflate(context, R.layout.common_error_view, this)
    }
}