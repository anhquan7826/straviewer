package com.vnpttech.straviewer.utils

import android.annotation.SuppressLint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
fun delayFor(duration: Long, unit: TimeUnit, callback: () -> Unit) {
    Observable.timer(duration, unit).observeOn(AndroidSchedulers.mainThread()).subscribe {
        callback()
    }
}