package com.example.retrofitpractice

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator


class CustomClusterRenderer(context: Context, map: GoogleMap, clusterManager: ClusterManager<MyItem>) : DefaultClusterRenderer<MyItem>(context, map, clusterManager) {
    //var mClusterIconGenerator : IconGenerator? = null
    //val mcontext = context
    override fun onBeforeClusterItemRendered(item: MyItem, markerOptions: MarkerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        val markerDescriptor :BitmapDescriptor
        val mytype = item.getMydispType()
        Log.d("改變顏色", "$mytype")
        if(mytype == 1) {
            markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)
            markerOptions.icon(markerDescriptor).snippet(item.title)
            markerOptions.icon(markerDescriptor).snippet(item.snippet)
        } else if (mytype == 2){
            markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            markerOptions.icon(markerDescriptor).snippet(item.title)
            markerOptions.icon(markerDescriptor).snippet(item.snippet)
        }
    }

    override fun onClusterItemUpdated(item: MyItem, marker: Marker) {
        super.onClusterItemUpdated(item, marker)
        Log.d("clustering update","更新了")
    }

    //    override fun onBeforeClusterRendered(cluster: Cluster<MyItem>, markerOptions: MarkerOptions) {
//        super.onBeforeClusterRendered(cluster, markerOptions)
//        mClusterIconGenerator?.setBackground(
//                ContextCompat.getDrawable(mcontext, R.drawable.ic_baseline_place_24)
//        )
//        mClusterIconGenerator?.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_DropDownItem)
//
//    }

}