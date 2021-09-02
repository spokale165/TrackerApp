package com.rna_records.tracktrigger.Utils

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rna_records.tracktrigger.R
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class TriggerAdapter(var context: Context) :
    RecyclerView.Adapter<TriggerAdapter.TriggerViewHolder>() {

    var toDoActivityList = listOf<TriggerToDoActivity>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriggerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.trigger_itemholder, parent, false)
        return TriggerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TriggerViewHolder, position: Int) {
        holder.titleTextView.text = toDoActivityList[position].title
        holder.descriptionTextView.text = toDoActivityList[position].description

        holder.deleteButton.setOnClickListener {
            FirebaseRepo().deleteTriggerActivity(toDoActivityList[position].title)
            val newActivityList = mutableListOf<TriggerToDoActivity>()
            newActivityList.addAll(toDoActivityList)
            newActivityList.removeAt(position)
            setAdapter(newActivityList)
        }
        holder.shareButton.setOnClickListener {
            val date = Date(toDoActivityList[position].timestamp.seconds * 1000)

            val shareDetails: String =
                "Title: " + toDoActivityList[position].title + "\n" + "Description: " + toDoActivityList[position].description + "\n" + "Deadline: " + date.date + " " + DateFormatSymbols().months[date.month] + " " + date.hours + ":" + date.minutes
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, shareDetails)
            context.startActivity(Intent.createChooser(shareIntent, "Share via"));

        }

        val timeStamp = toDoActivityList[position].timestamp
        val today = Date()
        val currentTime: Long = today.time
        val expiryTime: Long = timeStamp.seconds - currentTime / 1000


        if (holder.countDownTimer != null) {
            holder.countDownTimer!!.cancel()
        }
        holder.countDownTimer = object : CountDownTimer(expiryTime * 1000, 500) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                val time =
                    "" + hours + ":" + minutes % 60 + ":" + seconds % 60
                holder.timerTextView.text = time
            }

            override fun onFinish() {
                holder.timerTextView.text = "Time up!"
            }
        }.start()


    }

    override fun getItemCount(): Int {
        return toDoActivityList.size
    }

    class TriggerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var countDownTimer: CountDownTimer? = null
        var titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        var timerTextView: TextView = itemView.findViewById(R.id.timerTextView)
        var descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        var deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
        var shareButton: ImageView = itemView.findViewById(R.id.shareButton)

    }

    fun setAdapter(itemsList: List<TriggerToDoActivity>) {
        toDoActivityList = itemsList
        notifyDataSetChanged()
    }

}

