package com.vnpttech.straviewer.ui.polymap

import android.graphics.Color
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.vnpttech.straviewer.R
import com.vnpttech.straviewer.data.models.PolylineMapModel
import com.vnpttech.straviewer.utils.log

class PolymapFragment(
    private val map: PolylineMapModel,
    private val startLatLng: List<Double>,
    private val endLatLng: List<Double>
) : Fragment(R.layout.fragment_polymap), OnMapReadyCallback {

    override fun onMapReady(g0: GoogleMap) {
        setPosition(g0)
    }

    private fun setPosition(gmap: GoogleMap) {
        log(map)
        val startPos = LatLng(startLatLng.first(), startLatLng.last())
        val finishPos = LatLng(endLatLng.first(), endLatLng.last())

        gmap.addMarker(MarkerOptions().position(startPos).title("Start"))
        gmap.addMarker(MarkerOptions().position(finishPos).title("Finish"))

        val posBound = LatLngBounds.builder().include(startPos).include(finishPos).build()

        gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(posBound, 12))

        gmap.addPolyline(
            PolylineOptions().addAll(PolyUtil.decode(map.polyline)).color(Color.BLUE).width(3f)
        )
    }
}