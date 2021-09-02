package com.rna_records.tracktrigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.rna_records.tracktrigger.Utils.FirebaseRepo
import kotlinx.android.synthetic.main.fragment_addtracker_category.*

class AddTrackerItem:Fragment()
{
    lateinit var itemNameEt:EditText
    lateinit var quantityEt:EditText
    lateinit var addItemButton:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_fragment_tracker_item,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemNameEt = requireView().findViewById(R.id.itemName)
        quantityEt = requireView().findViewById(R.id.quantity)
        addItemButton = requireView().findViewById(R.id.addItemButton)

        val bundle = AddTrackerItemArgs.fromBundle(requireArguments())
        val categoryTitle = bundle.categoryTitle

        addItemButton.setOnClickListener {
            val itemName = itemNameEt.text.toString()
            val quantity = quantityEt.text.toString()
            if(itemName.isBlank()||quantity.isBlank())
            {
                Toast.makeText(requireContext(),"Item and Quantity field cannot be left blank!!",Toast.LENGTH_LONG).show()
            }else
            {
                FirebaseRepo().addTrackerItem(itemName,quantity,categoryTitle)
                view.findNavController().navigate(AddTrackerItemDirections.actionAddTrackerItemToTrackerItemsFragment(categoryTitle))

            }
        }

    }
}