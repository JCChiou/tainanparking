package com.example.retrofitpractice

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecShowDbAdapter() : RecyclerView.Adapter<RecShowDbAdapter.ViewHolder>(){
    /** edit userDB List */
    var userDbList : MutableList<UserDisplay> = mutableListOf()

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val editTitle = view.findViewById<TextView>(R.id.tv_edit_title)

        fun bindData(data: UserDisplay){
            Log.d("bind data", "$data")
            editTitle.text = data.pName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("create View", "準備創建VIEW")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.edit_userdb,parent,false)
        Log.d("create View", "創建VIEW")
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("get count ", "${userDbList.size}")
        return userDbList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(userDbList[position])
    }

    fun reload(list: List<UserDisplay>){   /** update recyclerView */
        Log.d("get db data", "$list")
        userDbList.clear()
        userDbList.addAll(list)
        notifyDataSetChanged()
    }
}