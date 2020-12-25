package com.example.retrofitpractice

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class ClusterAdapter(context: Context) : GoogleMap.InfoWindowAdapter {
    var mContext = context
    var mWindow :View = (context as MainActivity).layoutInflater.inflate(R.layout.info_window_layout, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val tvTitle = view.findViewById<TextView>(R.id.clu_name)
        val tvSnippet = view.findViewById<TextView>(R.id.clu_car)
        val tv_moto = view.findViewById<TextView>(R.id.clu_moto)
        tvTitle.text = marker.title
        if (marker.snippet == "2"){
            tvSnippet.text = marker.snippet
        }else {
            tvSnippet.text = marker.snippet.split(",")[0]
            tv_moto.text = marker.snippet.split(",")[1]
        }
    }

    override fun getInfoContents(p0: Marker?): View {
        if (p0 != null) {
//            Log.d("not null", "have view")
            rendowWindowText(p0,mWindow)
        }
        return mWindow
    }

    override fun getInfoWindow(p0: Marker?): View {
        if (p0 != null) {
//            Log.d("not null", "have view")
            rendowWindowText(p0,mWindow)
        }
        return mWindow
    }

}