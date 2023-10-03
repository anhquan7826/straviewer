package com.anhquan.straviewer.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anhquan.straviewer.data.database.AppDatabase
import com.anhquan.straviewer.data.models.ActivityModel
import com.anhquan.straviewer.data.models.AthleteModel
import com.anhquan.straviewer.data.services.StravaServices
import com.anhquan.straviewer.dependency_injection.app_preferences.AppPreferences
import com.anhquan.straviewer.utils.enums.LoadStatus
import com.anhquan.straviewer.utils.extensions.addTo
import com.anhquan.straviewer.utils.newLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferences: AppPreferences,
    private val services: StravaServices,
    private val database: AppDatabase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _loadStatus = newLiveData(LoadStatus.LOADING)
    val loadStatus: LiveData<LoadStatus> get() = _loadStatus

    private val _isLoggedIn = newLiveData(true)
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    lateinit var athlete: AthleteModel private set

    lateinit var activities: List<ActivityModel> private set

    init {
        val athleteId = preferences.getInt("athlete_id")
        database.athleteDao().getAthlete(athleteId)
            .subscribeOn(Schedulers.io()).subscribe({ athModel ->
                this.athlete = athModel
                services.getAllActivities().subscribeOn(Schedulers.io()).subscribe({ actModels ->
                    activities = actModels
                    _loadStatus.postValue(LoadStatus.LOADED)
                }, {
                    _loadStatus.postValue(LoadStatus.ERROR)
                }).addTo(compositeDisposable)
            }, {
                _loadStatus.postValue(LoadStatus.ERROR)
            }).addTo(compositeDisposable)
    }

    @SuppressLint("CheckResult")
    fun logout() {
        val token = preferences.getString("access_token") ?: ""
        services.deauthorize(token).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                preferences.clearKeys(
                    listOf(
                        "expires_at", "expires_in", "access_token", "refresh_token", "athlete_id"
                    )
                )
                database.athleteDao().deleteAthlete(athlete).subscribeOn(Schedulers.io())
                    .subscribe {
                        _isLoggedIn.postValue(false)
                    }
            }, {
                _isLoggedIn.postValue(true)
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}