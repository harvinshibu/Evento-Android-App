package com.harvin.evento.Users

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.harvin.evento.Model.UserModel
import com.harvin.evento.R
import com.harvin.evento.databinding.ActivityUserEditProfileBinding

class UserEditProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserEditProfileBinding
    private var databaseUsr: DatabaseReference? = null
    var userID:String?=null
    var user: FirebaseUser? = null
    var emailId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance().currentUser
        userID= Firebase.auth.currentUser?.uid.toString()
        databaseUsr = FirebaseDatabase.getInstance().getReference("users").child(userID!!)
        /*loadUsrDetails(databaseUsr!!,userID!!)*/
        binding.usrEditName.setText(intent.getStringExtra("name"))
        binding.userEditMail.setText(intent.getStringExtra("email"))



        binding.usrEditDetails.setOnClickListener {
            if(checkEditText()){
                val builder = AlertDialog.Builder(this)
                val alertView: View = layoutInflater.inflate(R.layout.pass2_cutom_dialog, null)
                builder.setView(alertView)
                val cancel:AppCompatButton=alertView.findViewById(R.id.btn_action_cancel)
                val ok:AppCompatButton=alertView.findViewById(R.id.btn_action_ok)
                val alertDialog=builder.create()
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.window?.setGravity(Gravity.CENTER)
                alertDialog.show()
                cancel.setOnClickListener {
                    alertDialog.dismiss()
                }
                ok.setOnClickListener {
                    val passEdit=alertView.findViewById<EditText>(R.id.edit_passProf)
                    val passEditLayout=alertView.findViewById<TextInputLayout>(R.id.editPass_layout)
                    binding.rProgLayout.visibility=View.VISIBLE
                    if (passEdit.text.toString().isEmpty()){
                        binding.rProgLayout.visibility=View.GONE
                        passEditLayout.error="Empty Field"
                    }
                    else{
                        val authCredential = EmailAuthProvider.getCredential(user!!.email!!, passEdit.text.toString())
                        user!!.reauthenticate(authCredential).addOnCompleteListener {
                            if(it.isSuccessful){
                                user!!.updateEmail(binding.userEditMail.text.toString()).addOnCompleteListener { task ->
                                    if(task.isSuccessful){
                                        binding.rProgLayout.visibility=View.GONE
                                        val userModel=UserModel(userID,binding.usrEditName.text.toString(),binding.userEditMail.text.toString())
                                        databaseUsr!!.setValue(userModel)
                                        Toast.makeText(this@UserEditProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                                        val i=Intent(applicationContext, UserHomeActivity::class.java)
                                        startActivity(i)
                                        finish()
                                        alertDialog.dismiss()

                                    }else{
                                        binding.rProgLayout.visibility=View.GONE
                                        Toast.makeText(this@UserEditProfileActivity, ""+task.exception.toString(), Toast.LENGTH_SHORT).show()
                                        Log.d("Error----",task.exception.toString())
                                        alertDialog.dismiss()
                                    }
                                }
                            }else{
                                binding.rProgLayout.visibility=View.GONE
                                Toast.makeText(this@UserEditProfileActivity, "Incorrect Old Password !", Toast.LENGTH_SHORT).show()
                                Log.d("ErrorInside----",it.exception.toString())
                                alertDialog.dismiss()
                            }
                        }
                    }
                }
            }
        }



    }

    fun checkEditText():Boolean{
        var status=true
        if(binding.userEditMail.text.toString().isEmpty()){
            binding.usrMailLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.usrEditName.text.toString().isEmpty()){
            binding.nameLayout.error= "Check Fields & Try Again"
            status=false
        }
        return status
    }

    /*private fun loadUsrDetails(databaseUsr: DatabaseReference,userID:String){
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue<UserModel>() != null) {
                    val key = dataSnapshot.key
                    if (key == userID) {
                        binding.usrEditName.setText(dataSnapshot.child("cname").value.toString())
                        binding.userEditMail.setText(dataSnapshot.child("email").value.toString())*//*
                        email=dataSnapshot.child("email").value.toString()

                        Toast.makeText(applicationContext,email,Toast.LENGTH_SHORT).show()*//*
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        databaseUsr.addValueEventListener(valueEventListener)
    }*/
}