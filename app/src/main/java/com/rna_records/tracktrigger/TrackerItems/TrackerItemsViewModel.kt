package com.rna_records.tracktrigger.TrackerItems

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rna_records.tracktrigger.Utils.FirebaseRepo
import com.rna_records.tracktrigger.Utils.TriggerToDoActivity

class TrackerItemsViewModel(title:String):ViewModel() {

    val categoryItemMap : LiveData<Map<String,String>> = FirebaseRepo().getCategoryItems(title)

}