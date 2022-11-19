package com.harvin.evento.Company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.harvin.evento.R
import com.harvin.evento.databinding.ActivityCmpLoginVerifyBinding
import com.harvin.evento.databinding.ActivityCmpResgistrationBinding
import java.util.concurrent.TimeUnit

class CmpLoginVerifyActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCmpLoginVerifyBinding
    private var vId: String? = null
    private var phone: String? = null
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCmpLoginVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editTextInput()
        vId = intent.getStringExtra("vId").toString()
        phone = intent.getStringExtra("phone").toString()
        binding.textView2.text= "+91 $phone"

        binding.verifyLogOtp.setOnClickListener{
            binding.rProgLayout.visibility=View.VISIBLE
            if (binding.etLC1.text.toString().trim().isEmpty() ||
                binding.etLC2.text.toString().trim().isEmpty() ||
                binding.etLC3.text.toString().trim().isEmpty() ||
                binding.etLC4.text.toString().trim().isEmpty() ||
                binding.etLC5.text.toString().trim().isEmpty() ||
                binding.etLC6.text.toString().trim().isEmpty()
            ) {
                Toast.makeText(this@CmpLoginVerifyActivity, "Enter valid  OTP", Toast.LENGTH_SHORT).show()
                binding.rProgLayout.visibility=View.GONE
            } else {
                if (vId != null) {
                    val code: String = binding.etLC1.text.toString().trim()+
                            binding.etLC2.text.toString().trim()+
                            binding.etLC3.text.toString().trim()+
                            binding.etLC4.text.toString().trim()+
                            binding.etLC5.text.toString().trim()+
                            binding.etLC6.text.toString().trim()
                    val credential = PhoneAuthProvider.getCredential(vId!!, code)
                    firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            binding.rProgLayout.visibility=View.GONE
                            val i = Intent(this@CmpLoginVerifyActivity, CmpHomeActivity::class.java)
                            i.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                        } else {
                            binding.rProgLayout.visibility=View.GONE
                            Toast.makeText(this@CmpLoginVerifyActivity, "Otp is not Valid", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.logResentOtp.setOnClickListener {
            binding.rProgLayout.visibility=View.VISIBLE
            otpSend(phone!!)
        }
    }

    private fun editTextInput(){
        binding.etLC1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.etLC2.requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.etLC2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.etLC3.requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.etLC3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.etLC4.requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.etLC4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.etLC5.requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.etLC5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.etLC6.requestFocus()
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun otpSend(phone:String) {

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@CmpLoginVerifyActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("Error", e.localizedMessage)
                binding.rProgLayout.visibility=View.GONE
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                binding.rProgLayout.visibility=View.GONE
                val intent = Intent(applicationContext, CmpLoginVerifyActivity::class.java)
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
            .setCallbacks(callbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
}