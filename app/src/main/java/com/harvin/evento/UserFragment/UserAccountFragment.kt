package com.harvin.evento.UserFragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.harvin.evento.AboutUsActivity
import com.harvin.evento.MainActivity
import com.harvin.evento.Model.UserModel
import com.harvin.evento.R
import com.harvin.evento.Users.UserEditProfileActivity
import com.harvin.evento.databinding.FragmentUserAccountBinding

class UserAccountFragment : Fragment() {
    private lateinit var binding:FragmentUserAccountBinding
    private var databaseUsr: DatabaseReference? = null
    var userID:String?=null
    var user: FirebaseUser? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentUserAccountBinding.inflate(inflater,container,false)

        userID= Firebase.auth.currentUser?.uid.toString()
        user=FirebaseAuth.getInstance().currentUser
        databaseUsr = FirebaseDatabase.getInstance().getReference("users").child(userID!!)
        loadUsrDetails(databaseUsr!!,userID!!)

        binding.logoutUsr.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val i = Intent(activity, MainActivity::class.java)
            i.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
        binding.textView23.setOnClickListener {
            startActivity(Intent(activity, AboutUsActivity::class.java))
        }

        binding.viewUsrProfile.setOnClickListener {
            val i = Intent(activity, UserEditProfileActivity::class.java)
            i.putExtra("name",binding.usrNameAcc.text.toString())
            i.putExtra("email",binding.usrPhoneAcc.text.toString())
            startActivity(i)
        }

        binding.changeAccPass.setOnClickListener {
            val builder = AlertDialog.Builder(this.requireContext())
            val alertView: View = layoutInflater.inflate(R.layout.pass_change_layout, null)
            builder.setView(alertView)
            val cancel: AppCompatButton =alertView.findViewById(R.id.btn_change_cancel)
            val ok: AppCompatButton =alertView.findViewById(R.id.btn_change_ok)
            val alertDialog=builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window?.setGravity(Gravity.CENTER)
            alertDialog.show()
            cancel.setOnClickListener {
                alertDialog.dismiss()
            }
            ok.setOnClickListener {
                binding.rProgLayout.visibility=View.VISIBLE
                val oldPass=alertView.findViewById<EditText>(R.id.edit_pass1Prof)
                val oldEditLayout=alertView.findViewById<TextInputLayout>(R.id.editPass1_layout)
                val newPass=alertView.findViewById<EditText>(R.id.edit_pass2Prof)
                val newEditLayout=alertView.findViewById<TextInputLayout>(R.id.editPass2_layout)
                if(oldPass.text.toString().isEmpty() || newPass.text.toString().isEmpty()){
                    oldEditLayout.error="Check Fields and Try Again!!"
                    newEditLayout.error="Check Fields and Try Again!!"
                }else{
                    val authCredential = EmailAuthProvider.getCredential(user!!.email!!, oldPass.text.toString())
                    user!!.reauthenticate(authCredential).addOnCompleteListener {
                        if (it.isSuccessful){
                            user!!.updatePassword(newPass.text.toString()).addOnCompleteListener { task ->
                                if (task.isSuccessful){
                                    alertDialog.dismiss()
                                    binding.rProgLayout.visibility=View.GONE
                                    Toast.makeText(activity, "Password Changed Successfully.", Toast.LENGTH_SHORT).show()
                                }else{
                                    alertDialog.dismiss()
                                    binding.rProgLayout.visibility=View.GONE
                                    Toast.makeText(activity, "Password Change Failed.", Toast.LENGTH_SHORT).show()
                                    Log.d("ErrorAuth--",task.exception.toString())

                                }
                            }
                        } else{
                            alertDialog.dismiss()
                            binding.rProgLayout.visibility=View.GONE
                            Toast.makeText(activity, "Incorrect Old Password !", Toast.LENGTH_SHORT).show()
                            Log.d("ErrorAuth--",it.exception.toString())
                        }
                    }
                }
            }
        }


        return binding.root
    }

    private fun loadUsrDetails(databaseUsr: DatabaseReference,userID:String){
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue<UserModel>() != null) {
                    val key = dataSnapshot.key
                    if (key == userID) {
                        binding.usrNameAcc.text=dataSnapshot.child("cname").value.toString()
                        binding.usrPhoneAcc.text=dataSnapshot.child("email").value.toString()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        databaseUsr.addValueEventListener(valueEventListener)
    }

}