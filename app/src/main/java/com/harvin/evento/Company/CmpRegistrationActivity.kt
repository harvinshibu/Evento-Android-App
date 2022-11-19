package com.harvin.evento.Company

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.harvin.evento.MainActivity
import com.harvin.evento.databinding.ActivityCmpResgistrationBinding
import java.util.concurrent.TimeUnit

class CmpRegistrationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCmpResgistrationBinding
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var callbacks: OnVerificationStateChangedCallbacks? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCmpResgistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cmpSignup.setOnClickListener {

            /*val intent = Intent(applicationContext, CmpVerifyRegActivity::class.java)
            startActivity(intent)*/
            binding.rProgLayout.visibility=View.VISIBLE
            if(binding.cmpSignPhone.text.toString().isEmpty() || binding.cmpSignPhone.text.toString().length<10){
                binding.phoneLayout.error= "Check Fields & Try Again"
                binding.rProgLayout.visibility=View.GONE
            }
            else{
                otpSend(binding.cmpSignPhone.text.toString().trim())
            }
        }
    }



    fun onCmpLoginClick(view: View) {
        startActivity(Intent(this@CmpRegistrationActivity, MainActivity::class.java))
    }

    private fun otpSend(phone:String) {

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@CmpRegistrationActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error", e.localizedMessage)
                binding.rProgLayout.visibility=View.GONE
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                binding.rProgLayout.visibility=View.GONE
                val intent = Intent(applicationContext, CmpVerifyRegActivity::class.java)
                intent.putExtra("phone", phone)
                intent.putExtra("vId", verificationId)
                startActivity(intent)
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }
        }
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+91$phone")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks as OnVerificationStateChangedCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
}