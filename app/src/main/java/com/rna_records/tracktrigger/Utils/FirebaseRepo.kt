package com.rna_records.tracktrigger.Utils

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.rna_records.tracktrigger.AuthenticationActivity
import com.rna_records.tracktrigger.MainActivity
import java.io.ByteArrayOutputStream
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp

class FirebaseRepo {

    fun addTriggerToDoActivity(toDoActivity: TriggerToDoActivity)
    {
        Firebase.firestore.collection(AuthenticationActivity.userUid).document("Trigger").collection("Trigger").document(toDoActivity.title).set(toDoActivity)
    }
    fun deleteTriggerActivity(title:String)
    {
        Firebase.firestore.collection(AuthenticationActivity.userUid).document("Trigger").collection("Trigger").document(title).delete()
    }
     fun getTriggerActivities(): LiveData<List<TriggerToDoActivity>>
    {
        val toDoActivities = mutableListOf<TriggerToDoActivity>()
        val toDoActivitiesLiveData = MutableLiveData<List<TriggerToDoActivity>>()
        Firebase.firestore.collection(AuthenticationActivity.userUid).document("Trigger").collection("Trigger")
            .get()
        .addOnSuccessListener { result ->
            for (document in result) {

                val title = document.data["title"] as String
                val description = document.data["description"] as String
                val timeStamp = document.data["timestamp"] as com.google.firebase.Timestamp
                toDoActivities.add(TriggerToDoActivity(title,description,timeStamp))
            }
            toDoActivitiesLiveData.value = toDoActivities
        }
        .addOnFailureListener { exception ->
            exception.message?.let { Log.e("ERROR", it) }
        }
        return toDoActivitiesLiveData
    }
    fun getTrackerCategories(): LiveData<List<TrackerCategory>>
    {
        val categories = mutableListOf<TrackerCategory>()
        val categoriesLiveData = MutableLiveData<List<TrackerCategory>>()

        Firebase.firestore.collection(AuthenticationActivity.userUid).document("Tracker").collection("Tracker")
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    val title = document.id
                    val url = document.data["url"] as String
                    val category:TrackerCategory = TrackerCategory(title,url)
                    categories.add(category)
                }
                categoriesLiveData.value = categories

            }
            .addOnFailureListener { exception ->
                exception.message?.let { Log.e("ERROR", it) }
            }
        return categoriesLiveData

    }
    fun addTrackerCategory(category: TrackerCategory)
    {
        val newCategory = hashMapOf(
            "url" to category.url
        )
        Firebase.firestore.collection(AuthenticationActivity.userUid).document("Tracker").collection("Tracker").document(category.title).set(newCategory)
            .addOnSuccessListener {
                Log.e("Category Added","Added to firestore");
            }

    }
    fun uploadPicToCloud(imageView: ImageView,title: String)
    {
        var downloadUrl:String = ""
        val storageRef = Firebase.storage.reference.child("images/$title")
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.e("ERROR",it.toString())
        }.addOnSuccessListener { taskSnapshot ->
          storageRef.downloadUrl.addOnSuccessListener {

              addTrackerCategory(TrackerCategory(title,it.toString()))
          }


        }

    }
    fun getCategoryItems(title: String):LiveData<Map<String,String>> {

        val itemMap = mutableMapOf<String,String>()
        val itemMapLiveData = MutableLiveData<Map<String,String>>()
        val docRef =
            Firebase.firestore.collection(AuthenticationActivity.userUid).document("Tracker")
                .collection("Tracker").document(title)
                docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val items:Map<String,String> = document.data as Map<String, String>
                        for(item in items)
                        {
                            if(item.key.equals("url"))
                            {
                                continue
                            }else
                            {
                                itemMap[item.key] = item.value
                            }
                            itemMapLiveData.value = itemMap
                        }

                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        return itemMapLiveData
    }
    fun addTrackerItem(itemName:String,quantity:String,categoryTitle:String)
    {
        val mapToAdd= mutableMapOf<String,Any>()
        mapToAdd[itemName] = quantity
        Firebase.firestore.collection(AuthenticationActivity.userUid).document("Tracker").collection("Tracker").document(categoryTitle).update(mapToAdd)

    }
    fun deleteTrackerCategory(title: String)
    {
        Firebase.firestore.collection(AuthenticationActivity.userUid).document("Tracker").collection("Tracker").document(title).delete()

    }
    fun changeTrackerItemsQuantity(quantity: String,title: String,subTitle:String)
    {
        val mapToAdd= mutableMapOf<String,Any>()
        mapToAdd[subTitle] = quantity
       Firebase.firestore.collection(AuthenticationActivity.userUid).document("Tracker").collection("Tracker").document(title).update(mapToAdd)
    }
    fun deleteTrackerItem(title: String,subTitle: String)
    {
        val updates = hashMapOf<String, Any>(
            subTitle to FieldValue.delete()
        )
        Firebase.firestore.collection(AuthenticationActivity.userUid).document("Tracker").collection("Tracker").document(title).update(updates)

    }
    fun getRegistrationDetails()
    {
        Firebase.firestore.collection(AuthenticationActivity.userUid).document("Registration").get()
            .addOnSuccessListener {
                MainActivity.userName = it["userName"] as String
                MainActivity.phone = it["phone"] as String

            }
    }

}