package com.rna_records.tracktrigger.Utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.google.firebase.Timestamp
import com.rna_records.tracktrigger.AuthenticationActivity
import com.rna_records.tracktrigger.EmailSendUtil.GMailSender
import com.rna_records.tracktrigger.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.util.*

class AlertReceiver : BroadcastReceiver()
{

    override fun onReceive(p0: Context?, p1: Intent?) {

        val paramArray = p1?.getStringArrayExtra("params")
        val title = paramArray?.get(0)
        val description = paramArray?.get(1)


        GlobalScope.launch(Dispatchers.IO) {

            sendEmail(title,description)
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(MainActivity.phone, null, "Hello "+MainActivity.userName+", You have a meeting "+ title+" scheduled in an hour.", null, null)

        }
    }
    suspend fun sendEmail(title: String?, description: String?)
    {
        try {
            val body:String = ("Hello "+MainActivity.userName+"\nYou have a meeting "+ title+" scheduled in an hour.\nTitle: " +title + "\n" + "Description: " + description + "\n")
            val sender =  GMailSender("ooplabproject@gmail.com", "BadiyaPassword@69");
            sender.sendMail(
                "TODO Activity: $title",
                body,
                "ooplabproject@gmail.com",
                AuthenticationActivity.emailId
            );
        } catch (e: Exception) {
            Log.e("SendMail", e.toString());
        }

    }

}