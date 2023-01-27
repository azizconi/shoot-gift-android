package com.example.myapplication.presentation.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.local.entity.history.HistoryEntity

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var histories: List<HistoryEntity> = emptyList()

    fun setData(histories: List<HistoryEntity>) {
        this.histories = histories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_history,
            parent, false
        )
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = histories[position]
        holder.prize.text = currentItem.prizeWon
        holder.level.text =  "Уровень: ${currentItem.level}"
        holder.date.text = currentItem.date.toString()//convertDate(currentItem.date.toString()/*toHttpDateString()*/)
    }

    override fun getItemCount(): Int {
        return histories.size
    }


    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val prize: TextView = itemView.findViewById(R.id.won_prize_txt)
        val level: TextView = itemView.findViewById(R.id.won_level_txt)
        val date: TextView = itemView.findViewById(R.id.won_date_txt)
    }



}