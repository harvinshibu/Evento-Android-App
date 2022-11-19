package com.harvin.evento

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.harvin.evento.Adapters.RecyclerUserHomeAdapter
import com.harvin.evento.Adapters.RecyclerUsrBookAdapter
import com.harvin.evento.Company.CmpHomeActivity
import com.harvin.evento.Company.CmpLoginVerifyActivity
import com.harvin.evento.Guest.GuestHomeActivity
import com.harvin.evento.Model.BookingModel
import com.harvin.evento.Model.CompanyModel
import com.harvin.evento.Model.UserModel
import com.harvin.evento.Users.UserHomeActivity
import com.harvin.evento.Users.UserRegistrationActivity
import com.harvin.evento.databinding.ActivityMainBinding
import java.lang.NullPointerException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private var databaseCmp: DatabaseReference? = null
    private var databaseUsr: DatabaseReference? = null
    private var databasePhone: DatabaseReference? = null
    var userID:String?=null
    var cmpArrayList: ArrayList<CompanyModel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userID= Firebase.auth.currentUser?.uid.toString()
        cmpArrayList= arrayListOf()
        databaseCmp = FirebaseDatabase.getInstance().getReference("company").child(userID!!)
        databaseUsr = FirebaseDatabase.getInstance().getReference("users").child(userID!!)
        databasePhone = FirebaseDatabase.getInstance().getReference("company")

        Log.d("Errorr",cmpArrayList.toString())


        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    if(tab.position==0){
                        binding.cmpLogin.visibility = View.GONE
                        binding.userLogin.visibility = View.VISIBLE
                    }
                    else{
                        binding.userLogin.visibility = View.GONE
                        binding.cmpLogin.visibility = View.VISIBLE
                    }
                }

            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.forPass.setOnClickListener {
            startActivity(Intent(applicationContext,ForgotPasswordActivity::class.java))
        }

        binding.cmpLoginMain.setOnClickListener {
            binding.rProgLayout.visibility=View.VISIBLE
            if(binding.logPhone.text.toString().isEmpty() || binding.logPhone.text.toString().length<10){
                binding.phoneLayout.error= "Check Fields & Try Again"
                binding.rProgLayout.visibility=View.GONE
            }
            else{
                checkPhone(binding.logPhone.text.toString())/*
                otpSend(binding.logPhone.text.toString().trim())*/
            }
        }

        binding.guest.setOnClickListener {
            startActivity(Intent(this@MainActivity,GuestHomeActivity::class.java))
        }

        binding.usrLoginMain.setOnClickListener {
            if (checkEditText()){
                binding.rProgLayout.visibility=View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(binding.logMail.text.toString().trim(), binding.logPass.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            binding.rProgLayout.visibility=View.GONE
                            val i = Intent(this, UserHomeActivity::class.java)
                            i.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                        } else {
                            binding.rProgLayout.visibility=View.GONE
                            Toast.makeText(this@MainActivity, "Error ! " + task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

    fun onSignUpClick(view: View) {
        val i=Intent(this@MainActivity, UserRegistrationActivity::class.java)
        startActivity(i)
    }

    private fun checkPhone(string: String){
        /*var status=true
        val phoneValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                status = dataSnapshot.value != null
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        databasePhone!!.orderByChild("phone").equalTo(binding.logPhone.text.toString()).addListenerForSingleValueEvent(phoneValueEventListener)
        return status*/
        for (objectList in cmpArrayList!!) {
            if(objectList.phone.toString().equals(string)){
                Toast.makeText(this@MainActivity,"true",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this@MainActivity,"false",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun loadDataFromDatabase(){
        if (cmpArrayList!!.isNotEmpty())
            cmpArrayList!!.clear()
        databaseCmp!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try{
                    if (snapshot.exists()){
                        for (postSnapshot in snapshot.children) {
                            val companyModel= postSnapshot.getValue<CompanyModel>()
                            cmpArrayList!!.add(companyModel!!)
                        }
                    }
                }catch (e:NullPointerException){}

            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun otpSend(phone:String) {

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@MainActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
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

    override fun onStart() {
        super.onStart()
        loadDataFromDatabase()
    }

    fun checkEditText():Boolean{
        var status=true
        if(binding.logMail.text.toString().isEmpty()){
            binding.mailLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.logPass.text.toString().isEmpty()){
            binding.passLayout.error= "Check Fields & Try Again"
            status=false
        }
        return status
    }
}


