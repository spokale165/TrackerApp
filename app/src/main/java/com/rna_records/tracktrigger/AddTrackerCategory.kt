package com.rna_records.tracktrigger

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rna_records.tracktrigger.Utils.FirebaseRepo
import com.rna_records.tracktrigger.Utils.TrackerCategory


class AddTrackerCategory:Fragment() {

    lateinit var title:EditText
    lateinit var chooseImageButton:Button
    lateinit var saveButton: Button
    lateinit var imageView: ImageView
    lateinit var spinner:ProgressBar
    var imageUri:Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode === RESULT_OK) {
                val thumbnail = MediaStore.Images.Media.getBitmap(
                    context?.contentResolver, imageUri);
                imageView.setImageBitmap(thumbnail);
            }
            1 -> if (resultCode === RESULT_OK) {
                val selectedImage: Uri? = data?.data
                imageView.setImageURI(selectedImage)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_addtracker_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = requireView().findViewById(R.id.title)
        chooseImageButton = requireView().findViewById(R.id.chooseButton)
        saveButton = requireView().findViewById(R.id.saveButton)
        imageView = requireView().findViewById(R.id.imageView)
        spinner = requireView().findViewById(R.id.spinner)
        val permission = arrayOf<String>(android.Manifest.permission.READ_EXTERNAL_STORAGE)


        ActivityCompat.requestPermissions(requireActivity(), permission,1);

        chooseImageButton.setOnClickListener {
            val items = arrayOf("Camera", "Gallery")

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Choose Image From")
                .setItems(items) { dialog, which ->
                    if(which.toString() == "0")
                    {
                        val values = ContentValues()
                        values.put(MediaStore.Images.Media.TITLE, "New Picture")
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                        imageUri = context?.contentResolver?.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                        )
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        startActivityForResult(intent, 0)
                    }else if(which.toString() == "1")
                    {
                        val pickPhoto = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(pickPhoto, 1)
                    }
                }
                .show()



        }
        saveButton.setOnClickListener {
            val title = title.text.toString()
            if(title.isBlank()||imageView.drawable==null)
            {
                Toast.makeText(requireContext(),"Title and Image cannot be blank!",Toast.LENGTH_LONG).show()

            }else
            {

                FirebaseRepo().uploadPicToCloud(imageView,title)
                spinner.visibility = ProgressBar.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    spinner.visibility = ProgressBar.INVISIBLE
                    requireView().findNavController().navigate(R.id.action_addTrackerCategory_to_trackerFragment)
                }, 5000)



            }
        }



    }

}