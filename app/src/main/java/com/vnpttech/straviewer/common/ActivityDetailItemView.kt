package com.vnpttech.straviewer.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.vnpttech.straviewer.R
import com.vnpttech.straviewer.databinding.CommonActivityDetailItemViewBinding

class ActivityDetailItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding =
        CommonActivityDetailItemViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ActivityDetailItemView)

        val title = typedArray.getString(R.styleable.ActivityDetailItemView_title)
        val value = typedArray.getString(R.styleable.ActivityDetailItemView_value)

        setTitle(title ?: "")
        setValue(value ?: "")

        typedArray.recycle()
    }

    fun setTitle(title: String) {
        binding.activityDetailTitle.text = title
    }

    fun setValue(value: String) {
        binding.activityDetailValue.text = value
    }
}