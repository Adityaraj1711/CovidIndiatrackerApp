package com.covid.covidtracker.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.covid.covidtracker.R

class StateListAdapter(private val context: Context, private val arrayList: List<HashMap<String, String>>) : RecyclerView.Adapter<StateListAdapter.MyViewHolder>(){
    companion object{
        val TAG = "StateListAdapter"
    }

    var counter = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.state_list_item, parent, false)
        val myViewHolder = MyViewHolder(view)
        return myViewHolder
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val state_name = arrayList.get(position).get("state_name")
        val confirm_cases = arrayList.get(position).get("confirm_cases")
        holder.state_nameTv.text = state_name
        holder.confirm_casesTv.text = confirm_cases
        holder.counterTv.text = counter.toString()
        counter += 1
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var state_nameTv: TextView
        lateinit var confirm_casesTv: TextView
        lateinit var counterTv: TextView
        init {
            this.state_nameTv = itemView.findViewById(R.id.state_nameTv)
            this.confirm_casesTv = itemView.findViewById(R.id.confirm_cases)
            this.counterTv = itemView.findViewById(R.id.counter)
        }
    }


}