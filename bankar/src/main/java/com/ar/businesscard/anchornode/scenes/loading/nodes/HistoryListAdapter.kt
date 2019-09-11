package com.ar.businesscard.anchornode.scenes.loading.nodes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ar.bankar.R
import com.ar.businesscard.activity.data.Caller
import com.ar.businesscard.models.history.Movement
import java.text.SimpleDateFormat

class HistoryListAdapter(private val movementList: List<Movement>) : RecyclerView.Adapter<HistoryListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var movemenText: TextView
        var description1: TextView
        var description2: TextView
        var amount: TextView
        var date: TextView
        var month: TextView

        init {
            movemenText = view.findViewById(R.id.movementext)
            description1 = view.findViewById(R.id.description1)
            description2 = view.findViewById(R.id.description2)
            amount = view.findViewById(R.id.amount)
            date = view.findViewById(R.id.date)
            month = view.findViewById(R.id.month)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.account_history_list_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = movementList[position]
        holder.movemenText.setText(movie.movementText)
        holder.description1.setText(movie.description1)
        holder.description2.setText(movie.description2)
        holder.amount.setText(movie.amount)
        holder.date.setText(getDate(movie.executionDate))
        holder.month.setText(getMonth(movie.executionDate))
    }

    override fun getItemCount(): Int {
        return movementList.size
    }

    private fun getMonth(date: String) : String{
        val date = SimpleDateFormat(Caller.DATE_FORMAT).parse(date)
        val format = SimpleDateFormat(Caller.MONTH_FORMAT)
        return format.format(date)
    }

    private fun getDate(date: String) : String{
        val dateFormat = SimpleDateFormat(Caller.DATE_FORMAT).parse(date)
        val format = SimpleDateFormat(Caller.DATE_ONLY_FORMAT)
        return format.format(dateFormat)
    }
}