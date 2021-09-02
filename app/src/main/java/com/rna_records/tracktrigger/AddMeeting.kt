package com.rna_records.tracktrigger

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.rna_records.tracktrigger.EmailSendUtil.GMailSender
import com.rna_records.tracktrigger.Utils.AlertReceiver
import com.rna_records.tracktrigger.Utils.FirebaseRepo
import com.rna_records.tracktrigger.Utils.TriggerToDoActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.util.*
import java.util.jar.Manifest

class AddMeeting: Fragment() {

    lateinit var timePicker: TimePicker
    lateinit var datePicker: DatePicker
    lateinit var addReminder:Button
    lateinit var titleEditText: EditText
    lateinit var descriptionEditText:EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_addmeeting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timePicker = requireView().findViewById(R.id.timePicker)
        datePicker = requireView().findViewById(R.id.datePicker)
        addReminder = requireView().findViewById(R.id.addReminderButton)
        titleEditText = requireView().findViewById(R.id.title)
        descriptionEditText = requireView().findViewById(R.id.description)

        val permission = arrayOf<String>(android.Manifest.permission.SEND_SMS)


        ActivityCompat.requestPermissions(requireActivity(), permission,1);



        addReminder.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(
                (datePicker.year), datePicker.month, datePicker.dayOfMonth,
                timePicker.hour, timePicker.minute, 0
            )

            val startTime: Long = calendar.timeInMillis
            val title = titleEditText.text.toString()
            val timeStamp = com.google.firebase.Timestamp(startTime / 1000, 0)
            val description = descriptionEditText.text.toString()
            if (title.isBlank()) {
                Toast.makeText(requireContext(), "Title cannot be blank!", Toast.LENGTH_LONG).show()
            } else {

                FirebaseRepo().addTriggerToDoActivity(
                    TriggerToDoActivity(
                        title,
                        description,
                        timeStamp
                    )
                )

                GlobalScope.launch(Dispatchers.IO) {

                    startAlarm(title, description, timeStamp)
                }
                findNavController().navigate(R.id.action_addMeeting_to_triggerFragment)
            }
        }
    }

    suspend fun startAlarm(title: String,description: String,timeStamp: Timestamp)
    {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context,AlertReceiver::class.java)
        val parameters = arrayOf<String>(title,description)
        intent.putExtra("params",parameters)

        val id = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getBroadcast(context,id,intent,0)
        Log.e("CHECK12345", "$title $description $timeStamp")


        alarmManager?.setExact(AlarmManager.RTC_WAKEUP,((timeStamp.seconds*1000)-3600000),pendingIntent)
    }
    }

