package com.vnpttech.straviewer.ui.activity_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.vnpttech.straviewer.data.models.ActivityModel
import com.vnpttech.straviewer.data.services.StravaServices
import com.vnpttech.straviewer.utils.enums.LoadStatus
import com.vnpttech.straviewer.utils.extensions.addTo
import com.vnpttech.straviewer.utils.log
import com.vnpttech.straviewer.utils.newLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ActivityDetailViewModel @Inject constructor(
    private val services: StravaServices
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _loadStatus = newLiveData(LoadStatus.LOADING)
    val loadStatus: LiveData<LoadStatus> get() = _loadStatus

    lateinit var activity: ActivityModel private set

    fun loadActivity(id: Long) {
        services.getActivity(id).subscribeOn(Schedulers.io()).subscribe(
            {
                activity = it
                log(Gson().toJson(it))
                _loadStatus.postValue(LoadStatus.LOADED)
            },
            {
                _loadStatus.postValue(LoadStatus.ERROR)
            }
        ).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}