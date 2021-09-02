package com.rna_records.tracktrigger.Utils

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rna_records.tracktrigger.R
import com.rna_records.tracktrigger.Tracker.TrackerFragmentDirections
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class TrackerCategoryAdapter(var context: Context) :
    RecyclerView.Adapter<TrackerCategoryAdapter.TrackerCategoryViewHolder>() {

    var categoryList = listOf<TrackerCategory>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackerCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.tracker_itemholder, parent, false)
        return TrackerCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackerCategoryViewHolder, position: Int) {

        holder.title.text = categoryList[position].title
        Picasso.get().load(categoryList[position].url).error(R.drawable.no_image).fit().into(holder.image,object:
            Callback
        {
            override fun onSuccess() {
            }
            override fun onError(e: Exception?) {
                e?.message?.let { Log.e("ERROR", it) }
            }

        })
        holder.card.setOnClickListener {

            Navigation.findNavController(it).navigate(TrackerFragmentDirections.actionTrackerFragmentToTrackerItemsFragment(categoryList[position].title))
        }
        holder.delete.setOnClickListener {
            FirebaseRepo().deleteTrackerCategory(categoryList[position].title)
            val newCategoryList = mutableListOf<TrackerCategory>()
            newCategoryList.addAll(categoryList)
            newCategoryList.removeAt(position)
            setAdapter(newCategoryList)
        }

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class TrackerCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title:TextView = itemView.findViewById(R.id.title)
        val image:ImageView = itemView.findViewById(R.id.image)
        val card:CardView = itemView.findViewById(R.id.card)
        val delete: ImageButton = itemView.findViewById(R.id.deleteButton)

    }

    fun setAdapter(itemsList: List<TrackerCategory>) {
        categoryList = itemsList
        notifyDataSetChanged()
    }

}

