package com.example.androidproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MyAdapter(context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    val mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.user_card, parent, false)

        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val user: User = userList[position]
        holder.fullName.text =
            mContext.getString(R.string.userFullName, user.firstName, user.lastName)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    public class MyViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val fullName: TextView = itemView.findViewById(R.id.userName)

        // Constructor
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}