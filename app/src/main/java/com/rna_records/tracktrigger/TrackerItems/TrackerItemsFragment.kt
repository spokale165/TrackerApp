package com.rna_records.tracktrigger.TrackerItems

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rna_records.tracktrigger.R
import com.rna_records.tracktrigger.Utils.MyViewModelFactory
import com.rna_records.tracktrigger.Utils.TrackerItemsAdapter


class TrackerItemsFragment:Fragment() {

    lateinit var fab: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var editComment:EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracker_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var map:Map<String, String> = mutableMapOf()

        fab = requireView().findViewById(R.id.floating_action_button)
        recyclerView = requireView().findViewById(R.id.itemRecycler)
        editComment = requireView().findViewById(R.id.searchEd)
        val bundle = TrackerItemsFragmentArgs.fromBundle(requireArguments())
        val title = bundle.title
        val trackerItemAdapter = TrackerItemsAdapter(requireContext(), title)
        recyclerView.adapter = trackerItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val viewModel: TrackerItemsViewModel = ViewModelProvider(
            this,
            MyViewModelFactory(title)
        ).get(
            TrackerItemsViewModel::class.java
        )
        viewModel.categoryItemMap.observe(viewLifecycleOwner, Observer {

            trackerItemAdapter.setAdapter(it)
            map = it

        })
        fab.setOnClickListener {
            view.findNavController().navigate(
                TrackerItemsFragmentDirections.actionTrackerItemsFragmentToAddTrackerItem(
                    title
                )
            )

        }
        editComment.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= editComment.getRight() - editComment.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    val searchedString = editComment.text.toString().toLowerCase()
                    for ((key, value) in map.entries) {
                        if (searchedString.equals(key.toLowerCase())) {
                            val alertDialog: AlertDialog = AlertDialog.Builder(context).create()
                            alertDialog.setTitle("Search Result")
                            alertDialog.setMessage(key+" : "+value)
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                            alertDialog.show()
                        }else
                        {
                            Toast.makeText(context,"No results found",Toast.LENGTH_LONG).show()
                        }
                    }
                    return@OnTouchListener true
                }
            }
            false
        })


    }
}