package com.rna_records.tracktrigger

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rna_records.tracktrigger.Utils.FirebaseRepo
import com.rna_records.tracktrigger.Utils.TrackerCategory

class AuthenticationActivity : AppCompatActivity() {

    companion object{
        var userUid:String = ""
        var emailId:String = ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in

                val user = FirebaseAuth.getInstance().currentUser
                userUid = user!!.uid
                emailId = user.email.toString()
                val db = Firebase.firestore.collection(user!!.uid).document("Registration")
                db.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()


                        } else {
                            val intent:Intent = Intent(this,RegistrationActivity::class.java)
                            val grocery = TrackerCategory("Grocery","https://firebasestorage.googleapis.com/v0/b/tracktrigger-a7c74.appspot.com/o/images%2Fgrocerynew.jpg?alt=media&token=6cb92029-aecc-4a19-949a-06b1854ed1c1")
                            val furniture = TrackerCategory("Furniture","https://firebasestorage.googleapis.com/v0/b/tracktrigger-a7c74.appspot.com/o/images%2Ffurniture.jpg?alt=media&token=9956312a-8d49-4f17-bb61-b268cd40d60c")
                            val books = TrackerCategory("Books","https://firebasestorage.googleapis.com/v0/b/tracktrigger-a7c74.appspot.com/o/images%2Fbooks.jpeg?alt=media&token=5a57ccd2-1360-47a0-8c00-6441b69a44f8")
                            val firebase = FirebaseRepo()
                            firebase.addTrackerCategory(grocery)
                            firebase.addTrackerCategory(furniture)
                            firebase.addTrackerCategory(books)
                            intent.putExtra("user",user.uid)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(applicationContext,"Database Failure",Toast.LENGTH_LONG).show()
                    }



                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, response!!.error!!.errorCode, Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build())

// Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            1)




    }
}