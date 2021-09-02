package com.rna_records.tracktrigger.Tracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rna_records.tracktrigger.Utils.FirebaseRepo
import com.rna_records.tracktrigger.Utils.TrackerCategory

class TrackerViewModel:ViewModel() {

    val toDoActivitiesList : LiveData<List<TrackerCategory>> = FirebaseRepo().getTrackerCategories()

}