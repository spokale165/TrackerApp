package com.rna_records.tracktrigger.Tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rna_records.tracktrigger.R
import com.rna_records.tracktrigger.Utils.FirebaseRepo
import com.rna_records.tracktrigger.Utils.TrackerCategory
import com.rna_records.tracktrigger.Utils.TrackerCategoryAdapter

class TrackerFragment:Fragment() {

    lateinit var fab: FloatingActionButton
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.container)
        val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        if (currentFragment != null) {
            fragmentTransaction.detach(currentFragment)
        }
        if (currentFragment != null) {
            fragmentTransaction.attach(currentFragment)
        }
        fragmentTransaction.commit()
        fab = requireView().findViewById(R.id.floating_action_button)
        recyclerView = requireView().findViewById(R.id.triggerRecycler)
        val trackerAdapter = TrackerCategoryAdapter(requireContext())
        val layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = trackerAdapter
        recyclerView.layoutManager = layoutManager
        fab.setOnClickListener{
            requireView().findNavController().navigate(R.id.action_trackerFragment_to_addTrackerCategory)

        }
        val viewModel: TrackerViewModel = ViewModelProvider(this).get(TrackerViewModel::class.java)
        viewModel.toDoActivitiesList.observe(viewLifecycleOwner, Observer {

            trackerAdapter.setAdapter(it)

        })


    }
}