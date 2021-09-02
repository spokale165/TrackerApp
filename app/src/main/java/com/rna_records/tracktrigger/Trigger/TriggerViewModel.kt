package com.rna_records.tracktrigger.Trigger

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rna_records.tracktrigger.Utils.FirebaseRepo
import com.rna_records.tracktrigger.Utils.TriggerToDoActivity

class TriggerViewModel: ViewModel() {
    val toDoActivitiesList : LiveData<List<TriggerToDoActivity>> = FirebaseRepo().getTriggerActivities()
}