package com.harvin.evento.Company

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.harvin.evento.databinding.ActivityCmpVerifyRegBinding
import java.util.concurrent.TimeUnit

class CmpVerifyRegActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCmpVerifyRegBinding
    private var vId: String? = null
    private var phone: String? = null
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCmpVerifyRegBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editTextInput()
        vId = intent.getStringExtra("vId").toString()
        phone = intent.getStringExtra("phone").toString()
        binding.textView2.text= "+91 $phone"



        binding.verifyRegOtp.setOnClickListener {
            /*val i = Intent(this@CmpVerifyRegActivity, CmpRegDetailsActivity::class.java)
            i.putExtra("phone",phone)
            startActivity(i)*/

            binding.rProgLayout.visibility=View.VISIBLE
                if (binding.etC1.text.toString().trim().isEmpty() ||
                    binding.etC2.text.toString().trim().isEmpty() ||
                    binding.etC3.text.toString().trim().isEmpty() ||
                    binding.etC4.text.toString().trim().isEmpty() ||
                    binding.etC5.text.toString().trim().isEmpty() ||
                    binding.etC6.text.toString().trim().isEmpty()
                ) {
                    Toast.makeText(this@CmpVerifyRegActivity, "Enter valid  OTP", Toast.LENGTH_SHORT).show()
                    binding.rProgLayout.visibility=View.GONE
                } else {
                    if (vId != null) {
                        val code: String = binding.etC1.text.toString().trim()+
                                binding.etC2.text.toString().trim()+
                                binding.etC3.text.toString().trim()+
                                binding.etC4.text.toString().trim()+
                                binding.etC5.text.toString().trim()+
                                binding.etC6.text.toString().trim()
                        val credential = PhoneAuthProvider.getCredential(vId!!, code)
                        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    binding.rProgLayout.visibility=View.GONE
                                    val i = Intent(this@CmpVerifyRegActivity, CmpRegDetailsActivity::class.java)
                                    i.putExtra("phone",phone)
                                    startActivity(i)
                                } else {
                                    binding.rProgLayout.visibility=View.GONE
                                    Toast.makeText(this@CmpVerifyRegActivity, "Otp is not Valid", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
        }

        binding.regResentOtp.setOnClickListener {
            binding.rProgLayout.visibility=View.VISIBLE
            otpSend(phone!!)
        }

    }

    private fun editTextInput(){
        binding.etC1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.etC2.requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.etC2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.etC3.requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.etC3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.etC4.requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.etC4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.etC5.requestFocus()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.etC5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                binding.etC6.requestFocus()
            }
            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun otpSend(phone:String) {

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@CmpVerifyRegActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
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
            .setCallbacks(callbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
}