package com.rna_records.tracktrigger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.AccessController.getContext
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import java.security.AccessController.getContext

class RegistrationActivity : AppCompatActivity() {

    private var userName:String = ""
    private var phoneNo:String = ""
    private var userUid:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val submitbutton = findViewById<Button>(R.id.submitButton)
        val usernameEditText = findViewById<EditText>(R.id.userName)
        val phoneEditText = findViewById<EditText>(R.id.phoneNumber)
        val otpEditText = findViewById<EditText>(R.id.otp)
        val sendOtpButton =findViewById<Button>(R.id.sendOtpButton)
        var credentialClass: PhoneAuthCredential? = null
        var verificationIdClass:String = ""
        userUid = intent.getStringExtra("user").toString()

        sendOtpButton.setOnClickListener{

            userName = usernameEditText.text.toString()
            phoneNo = phoneEditText.text.toString()
            val patternName = Pattern.compile("([a-z]||[A-Z]||[0-9])+")
            val matcherName = patternName.matcher(userName)
            val patternPhone = Pattern.compile("[0-9]{10}")
            val matcherPhone = patternPhone.matcher(phoneNo)
            if(matcherName.matches() && matcherPhone.matches())
            {
                val temp = phoneNo
                phoneNo = "+91$temp"
                submitbutton.isEnabled = true
                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification. In some cases the phone number can be instantly
                        //     verified without needing to send or enter a verification code.
                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
                        //     detect the incoming verification SMS and perform verification without
                        //     user action.
                        credentialClass = credential




                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.


                        if (e is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(applicationContext,"Invalid Phone Number",Toast.LENGTH_LONG).show()
                        } else if (e is FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            // ...
                            Toast.makeText(applicationContext,"Requests Limit Exceeded",Toast.LENGTH_LONG).show()

                        }

                        // Show a message and update the UI
                        // ...
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.

                        // Save verification ID and resending token so we can use them later
                        Toast.makeText(applicationContext,"OTP sent",Toast.LENGTH_LONG).show()
                        verificationIdClass = verificationId

                        // ...
                    }
                }

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNo, // Phone number to verify
                    60, // Timeout duration
                    TimeUnit.SECONDS, // Unit of timeout
                    this, // Activity (for callback binding)
                    callbacks) // OnVerificationStateChangedCallbacks
            }else
            {

                Toast.makeText(this,"Username or phone number is invalid",Toast.LENGTH_LONG).show()

            }



        }
        submitbutton.setOnClickListener{

            val otpEntered:String = otpEditText.text.toString()
            if(otpEntered==null||otpEntered.isEmpty())
            {
                Toast.makeText(this,"OTP field cannot be empty",Toast.LENGTH_LONG).show()
            }else {

                if (credentialClass == null) {
                    credentialClass = PhoneAuthProvider.getCredential(
                        verificationIdClass,
                        otpEditText.text.toString()
                    )
                }

                RegistrationActivity().signInWithPhoneAuthCredential(credentialClass!!, this,userUid,userName,phoneNo,this)
            }

        }


    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,context:Context,userUid:String,userName:String,phoneNo:String,ctx:Context) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information


                    val registrationDetails = hashMapOf<String,String>(
                        "userName" to userName,
                        "phone" to phoneNo
                    )
                    Firebase.firestore.collection(userUid).document("Registration").set(registrationDetails)
                        .addOnSuccessListener { documentReference ->
                            Toast.makeText(context,"Registered Successfully",Toast.LENGTH_LONG).show()
                            val intent = Intent(ctx,MainActivity::class.java)
                            ctx.startActivity(intent)

                    }
                        .addOnFailureListener { e ->
                            Toast.makeText(context,"Error: "+e.message,Toast.LENGTH_LONG).show()
                        }



                    // ...
                } else {
                    // Sign in failed, display a message and update the UI

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(context,"Invalid Code",Toast.LENGTH_LONG).show()
                    }else
                    {
                        Toast.makeText(context,"OTP verification failed",Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

}