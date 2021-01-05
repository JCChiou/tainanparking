package com.example.retrofitpractice

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView


class RecShowDbAdapter() : RecyclerView.Adapter<RecShowDbAdapter.ViewHolder>(){
    /** edit userDB List */
    var userDbList : MutableList<UserDisplay> = mutableListOf()

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val editTitle = view.findViewById<TextView>(R.id.tv_edit_title)
        val editPosition = view.findViewById<TextView>(R.id.tv_edPosition)
        fun bindData(data: UserDisplay){
            editTitle.text = data.pName
            editPosition.text = data.lnglat
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.edit_userdb,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userDbList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(userDbList[position])
    }

    fun reload(list: List<UserDisplay>){   /** update recyclerView */
        userDbList.clear()
        userDbList.addAll(list)
        notifyDataSetChanged()
    }
}

class SingleItemClickListener() : RecyclerView.SimpleOnItemTouchListener() {
    private var clickListener: OnItemClickListener? = null
    private var gestureDetector: GestureDetectorCompat? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }
    constructor (recyclerView: RecyclerView, listener: OnItemClickListener) :this(){
        this.clickListener = listener
        gestureDetector =  GestureDetectorCompat( recyclerView.context, object : GestureDetector.SimpleOnGestureListener(){
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
//                val childView : View? = e?.x?.let { recyclerView.findChildViewUnder(it, e.y) }
//                if(childView != null && clickListener != null && gestureDetector?.onTouchEvent(e)!!){
//                    clickListener!!.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))
//                }
//                return super.onSingleTapUp(e)
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                val childView : View? = e?.x?.let { recyclerView.findChildViewUnder(it, e.y) }
                if(childView != null && clickListener != null){
                    clickListener!!.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
                super.onLongPress(e)
            }

        })
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        super.onTouchEvent(rv, e)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView : View? = e.x.let { rv.findChildViewUnder(it, e.y) }
        //Log.d("call touch", "count")
        if(childView != null && clickListener != null && gestureDetector?.onTouchEvent(e)!!){
            clickListener!!.onItemClick(childView, rv.getChildAdapterPosition(childView))
        }
        return super.onInterceptTouchEvent(rv, e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        super.onRequestDisallowInterceptTouchEvent(disallowIntercept)
    }


}