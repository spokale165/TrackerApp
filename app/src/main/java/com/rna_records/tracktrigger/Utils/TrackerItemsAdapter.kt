package com.rna_records.tracktrigger.Utils

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rna_records.tracktrigger.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class TrackerItemsAdapter(var context: Context,var title:String) :
    RecyclerView.Adapter<TrackerItemsAdapter.TrackerItemsViewHolder>() {

    var itemsMap = mapOf<String, String>()
    var keyList: ArrayList<String> = ArrayList<String>(itemsMap.keys)
    var valueList: ArrayList<String> = ArrayList<String>(itemsMap.values)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackerItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.tracker_items_itemholder, parent, false)
        return TrackerItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackerItemsViewHolder, position: Int) {

        holder.itemNameTv.text = keyList[position]
        holder.quantityTv.text = "Quantity: "+valueList[position]
        holder.minusButton.setOnClickListener {
            var minus:Int = Integer.parseInt(valueList[position])
            minus-=1
            valueList[position] = minus.toString()
            notifyDataSetChanged()
            FirebaseRepo().changeTrackerItemsQuantity(minus.toString(),title,keyList[position])

        }
        holder.addButton.setOnClickListener {
            var plus:Int = Integer.parseInt(valueList[position])
            plus+=1
            valueList[position] = plus.toString()
            notifyDataSetChanged()
            FirebaseRepo().changeTrackerItemsQuantity(plus.toString(),title,keyList[position])


        }
        holder.deleteButton.setOnClickListener {

            FirebaseRepo().deleteTrackerItem(title,keyList[position])
            keyList.removeAt(position)
            valueList.removeAt(position)
            notifyDataSetChanged()
        }
        holder.shareButton.setOnClickListener {
            val shareDetails = "ItemName: "+keyList[position]+"\n"+"Quantity: "+valueList[position]
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, shareDetails)
            context.startActivity(Intent.createChooser(shareIntent, "Share via"));
        }


    }

    override fun getItemCount(): Int {
        return keyList.size
    }

    class TrackerItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemNameTv:TextView = itemView.findViewById(R.id.itemName)
        val quantityTv:TextView = itemView.findViewById(R.id.quantity)
        val minusButton:ImageButton = itemView.findViewById(R.id.removeQuantity)
        val addButton:ImageButton = itemView.findViewById(R.id.addQuantity)
        val deleteButton:ImageButton = itemView.findViewById(R.id.deleteButton)
        val shareButton:ImageButton = itemView.findViewById(R.id.shareButton)

    }

    fun setAdapter(itemsMap: Map<String, String>) {
        this.itemsMap = itemsMap
        keyList = ArrayList<String>(itemsMap.keys)
        valueList = ArrayList<String>(itemsMap.values)
        notifyDataSetChanged()
    }


}

