package com.anhquan.straviewer.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.vnpttech.straviewer.R
import com.anhquan.straviewer.data.models.ActivityModel
import com.anhquan.straviewer.utils.UnitFormatter

class ActivityAdapter(private val activities: List<ActivityModel>, private val onItemClick: (ActivityModel) -> Unit) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {
    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.materialCardView)

        val name: TextView = itemView.findViewById(R.id.activity_name)
        val distance: TextView = itemView.findViewById(R.id.activity_distance)
        val elevGain: TextView = itemView.findViewById(R.id.activity_elev_gain)
        val time: TextView = itemView.findViewById(R.id.activity_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity_summary, parent, false)
        return ActivityViewHolder(view)
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.name.text = activity.name
        holder.distance.text = UnitFormatter.distance(activity.distance)
        holder.elevGain.text = UnitFormatter.distance(activity.elevationGain)
        holder.time.text = UnitFormatter.duration(activity.time)
        holder.card.setOnClickListener {
            onItemClick(activity)
        }
    }
}