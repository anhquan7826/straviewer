package com.vnpttech.straviewer.ui.activity_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.vnpttech.straviewer.R
import com.vnpttech.straviewer.databinding.ActivityActivityDetailBinding
import com.vnpttech.straviewer.ui.polymap.PolymapFragment
import com.vnpttech.straviewer.utils.enums.LoadStatus
import com.vnpttech.straviewer.utils.UnitFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityDetailActivity : AppCompatActivity() {
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
            when (it) {
                LoadStatus.LOADING -> {
                    binding.loading.visibility = VISIBLE
                    binding.loaded.visibility = GONE
                    binding.error.visibility = GONE
                }

                LoadStatus.LOADED -> {
                    binding.loading.visibility = GONE
                    binding.loaded.visibility = VISIBLE
                    binding.error.visibility = GONE
                    buildLayout()
                }

                else -> {
                    binding.loading.visibility = GONE
                    binding.loaded.visibility = GONE
                    binding.error.visibility = VISIBLE
                }
            }
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

        supportFragmentManager.beginTransaction().add(R.id.activity_map, PolymapFragment(
            viewModel.activity.map,
            viewModel.activity.startLatLng,
            viewModel.activity.endLatLng
        )).commit()
    }
}