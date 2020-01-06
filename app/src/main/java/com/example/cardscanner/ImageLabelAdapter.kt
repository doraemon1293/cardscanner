package com.example.cardscanner

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import kotlinx.android.synthetic.main.item_row.view.*

class ImageLabelAdapter(private val firebaseVisionList: List<Any>) : RecyclerView.Adapter<ImageLabelAdapter.ItemHolder>() {
    lateinit var context: Context

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(currentItem: FirebaseVisionImageLabel) {
            when {
                currentItem.confidence > .70 -> itemView.itemAccuracy.setTextColor(ContextCompat.getColor(context, R.color.green))
                currentItem.confidence < .30 -> itemView.itemAccuracy.setTextColor(ContextCompat.getColor(context, R.color.red))
                else -> itemView.itemAccuracy.setTextColor(ContextCompat.getColor(context, R.color.orange))
            }
            itemView.itemName.text = currentItem.text
            itemView.itemAccuracy.text = "Probability : ${(currentItem.confidence * 100).toInt()}%"
        }


    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val currentItem = firebaseVisionList[position]
        holder.bind(currentItem as FirebaseVisionImageLabel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        context = parent.context
        return ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_row, parent, false))
    }

    override fun getItemCount() = firebaseVisionList.size
}