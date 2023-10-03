package com.anhquan.straviewer.ui.activity_detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.vnpttech.straviewer.R
import com.anhquan.straviewer.data.models.ActivityModel
import com.vnpttech.straviewer.databinding.ActivityActivityDetailBinding
import com.anhquan.straviewer.utils.UnitFormatter
import com.anhquan.straviewer.utils.enums.LoadStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityActivityDetailBinding
    private lateinit var viewModel: ActivityDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[ActivityDetailViewModel::class.java]
        viewModel.loadActivity(intent.getLongExtra("activity_id", -1))
        binding = ActivityActivityDetailBinding.inflate(layoutInflater)
        buildObserver()
        buildEventListener()
        setContentView(binding.root)
    }

    private fun buildObserver() {
        viewModel.loadStatus.observe(this@ActivityDetailActivity) {
            binding.apply {
                loading.visibility = if(it == LoadStatus.LOADING) VISIBLE else GONE
                loaded.visibility = if(it == LoadStatus.LOADED) VISIBLE else GONE
                error.visibility = if(it == LoadStatus.ERROR) VISIBLE else GONE
            }
            if (it == LoadStatus.LOADED) buildLayout()
        }
    }

    private fun buildEventListener() {
        binding.appbar.setNavigationOnClickListener {
            finish()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buildLayout() {
        binding.activityName.text = viewModel.activity.name
        if (viewModel.activity.description?.isEmpty() == false) {
            binding.activityDescription.visibility = VISIBLE
            binding.activityDescription.text = viewModel.activity.description
        }

        binding.activityDistance.setValue(UnitFormatter.distance(viewModel.activity.distance))
        binding.activityElevGain.setValue(UnitFormatter.distance(viewModel.activity.elevationGain))
        binding.activityTime.setValue(UnitFormatter.duration(viewModel.activity.time))
        binding.activitySportType.setValue(viewModel.activity.sportType.toString())
        binding.activityStartDate.setValue(UnitFormatter.time(viewModel.activity.startDate))

        val supportMapFragment = SupportMapFragment()
        supportFragmentManager.beginTransaction().add(R.id.activity_map, supportMapFragment)
            .commit()
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        setPosition(p0, viewModel.activity)
    }

    private fun setPosition(
        gmap: GoogleMap,
        activity: ActivityModel
    ) {
        val startPos = LatLng(
            activity.startLatLng.first(),
            activity.startLatLng.last()
        )
        val finishPos = LatLng(
            activity.endLatLng.first(),
            activity.endLatLng.last()
        )
        gmap.addMarker(MarkerOptions().position(startPos).title("Start"))
        gmap.addMarker(MarkerOptions().position(finishPos).title("Finish"))
        val posBound = LatLngBounds.builder().include(startPos).include(finishPos).build()
        gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(posBound, 64))
        gmap.addPolyline(
            PolylineOptions().addAll(
                PolyUtil.decode(activity.map.polyline)
            ).color(Color.BLUE).width(5f)
        )
    }
}