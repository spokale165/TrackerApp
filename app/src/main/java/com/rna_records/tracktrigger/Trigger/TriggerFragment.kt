package com.rna_records.tracktrigger.Trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rna_records.tracktrigger.R
import com.rna_records.tracktrigger.Utils.TriggerAdapter

class TriggerFragment: Fragment() {

    lateinit var fab:FloatingActionButton
    lateinit var recyclerView:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_trigger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab = requireView().findViewById(R.id.floating_action_button)
        recyclerView = requireView().findViewById(R.id.triggerRecycler)
        val triggerAdapter = TriggerAdapter(requireContext())
        val layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = triggerAdapter
        recyclerView.layoutManager = layoutManager
        fab.setOnClickListener{
            requireView().findNavController().navigate(R.id.action_triggerFragment_to_addMeeting)

        }
        val viewModel: TriggerViewModel = ViewModelProvider(this).get(TriggerViewModel::class.java)
        viewModel.toDoActivitiesList.observe(viewLifecycleOwner, Observer {

            triggerAdapter.setAdapter(it)

        })


    }

}