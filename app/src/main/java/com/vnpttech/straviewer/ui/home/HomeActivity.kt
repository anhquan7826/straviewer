package com.vnpttech.straviewer.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vnpttech.straviewer.R
import com.vnpttech.straviewer.databinding.ActivityHomeBinding
import com.vnpttech.straviewer.ui.activity_detail.ActivityDetailActivity
import com.vnpttech.straviewer.ui.home.adapter.ActivityAdapter
import com.vnpttech.straviewer.ui.login.LoginActivity
import com.vnpttech.straviewer.utils.enums.LoadStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var activityListAdapter: ActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding = ActivityHomeBinding.inflate(layoutInflater)
        buildObserver()
        buildEventListener()
        setContentView(binding.root)
    }

    @SuppressLint("SetTextI18n")
    private fun buildObserver() {
        viewModel.loadStatus.observe(this@HomeActivity) {
            when (it) {
                LoadStatus.LOADED -> {
                    binding.loading.visibility = GONE
                    binding.loaded.visibility = VISIBLE
                    binding.error.visibility = GONE
                    buildLayout()
                }

                LoadStatus.LOADING -> {
                    binding.loading.visibility = VISIBLE
                    binding.loaded.visibility = GONE
                    binding.error.visibility = GONE
                }

                else -> {
                    binding.loading.visibility = GONE
                    binding.loaded.visibility = GONE
                    binding.error.visibility = VISIBLE
                }
            }
        }

        viewModel.isLoggedIn.observe(this@HomeActivity) {
            if (!it) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun buildEventListener() {
        binding.appbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.logout_alert_title)
                        .setMessage(R.string.logout_alert_message)
                        .setNegativeButton(R.string.logout_alert_cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(R.string.logout_alert_logout) { dialog, _ ->
                            dialog.dismiss()
                            viewModel.logout()
                        }
                        .show()
                    true
                }

                else -> true
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun buildLayout() {
        binding.appbar.menu[0].isVisible = true

        Glide.with(this).load(viewModel.athlete.profile).placeholder(R.mipmap.ic_profile_default)
            .circleCrop().into(binding.profile)
        binding.name.text = viewModel.athlete.firstname + " " + viewModel.athlete.lastname
        activityListAdapter = ActivityAdapter(viewModel.activities) {
            startActivity(
                Intent(this, ActivityDetailActivity::class.java).putExtra(
                    "activity_id",
                    it.id
                )
            )
        }

        binding.homeActivityList.apply {
            adapter = activityListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}
