package com.vnpttech.straviewer.utils

import androidx.lifecycle.MutableLiveData

fun <T : Any?> newLiveData(value: T? = null): MutableLiveData<T> {
    val result by lazy {
        if (value != null) MutableLiveData<T>(value) else MutableLiveData<T>()
    }
    return result
}