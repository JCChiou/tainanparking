package com.example.retrofitpractice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecShowDbAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val editTitle = view.findViewById<TextView>(R.id.tv_edit_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.edit_userdb,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    fun reload(){   /** update recyclerView */
//        fruit.clear()
//        fruit.addAll(list)
//        notifyDataSetChanged()
    }
}