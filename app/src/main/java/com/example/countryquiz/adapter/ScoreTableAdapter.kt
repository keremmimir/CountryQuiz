package com.example.countryquiz.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.parser.ColorParser
import com.example.countryquiz.R
import com.example.countryquiz.databinding.ScoreTableRowBinding
import com.example.countryquiz.model.ScoreTable

class ScoreTableAdapter(
    private var list: List<ScoreTable>,
    private val id: String,
    private val context: Context
) :
    RecyclerView.Adapter<ScoreTableAdapter.Holder>() {

    inner class Holder(private var binding: ScoreTableRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(list: ScoreTable, rank: Int) {
            binding.apply {
                textRank.text = "$rank."
                textUsername.text = list.username
                textScore.text = list.score.toString()

                if (list.id == id) {
                    textRank.setTextColor(ContextCompat.getColor(context, R.color.startButton))
                    textUsername.setTextColor(ContextCompat.getColor(context, R.color.startButton))
                    textScore.setTextColor(ContextCompat.getColor(context, R.color.startButton))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ScoreTableRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val list = list[position]
        holder.bind(list, position + 1)
    }
}