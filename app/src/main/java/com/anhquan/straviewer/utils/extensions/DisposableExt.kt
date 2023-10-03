package com.anhquan.straviewer.utils.extensions

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

fun Disposable.addTo(composite: CompositeDisposable) {
    composite.add(this)
}