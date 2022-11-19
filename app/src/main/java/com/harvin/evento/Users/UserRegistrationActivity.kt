package com.harvin.evento.Users

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.harvin.evento.Company.CmpRegistrationActivity
import com.harvin.evento.MainActivity
import com.harvin.evento.Model.UserModel
import com.harvin.evento.databinding.ActivityUserRegistrationBinding

class UserRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserRegistrationBinding
    private val mAuth: FirebaseAuth=FirebaseAuth.getInstance()
    var databaseUsr: DatabaseReference=FirebaseDatabase.getInstance().getReference("users")
    var userID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.usrSignup.setOnClickListener {
            var name=binding.usrSignName.text.toString()
            var email=binding.usrSignMail.text.toString()
            var pass=binding.usrSignPass.text.toString()
            if(checkEditText()){
                binding.rProgLayout.visibility=View.VISIBLE
                mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            binding.rProgLayout.visibility=View.GONE
                            userID = mAuth.currentUser!!.uid
                            val userModel=UserModel(userID,name,email)
                            databaseUsr.child(userID!!).setValue(userModel)
                            val i=Intent(this@UserRegistrationActivity,UserHomeActivity::class.java)
                            i.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                        } else {
                            binding.rProgLayout.visibility=View.GONE
                            Toast.makeText(this@UserRegistrationActivity, "Error !" + task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    fun onCmpSignUp(view: View) {
        startActivity(Intent(this@UserRegistrationActivity, CmpRegistrationActivity::class.java))
    }
    fun onUsrLoginRegClick(view: View) {
        startActivity(Intent(this@UserRegistrationActivity, MainActivity::class.java))
    }

    fun checkEditText():Boolean{
        var status=true
        if(binding.usrSignName.text.toString().isEmpty()){
            binding.nameLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.usrSignMail.text.toString().isEmpty()){
            binding.usrMailLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.usrSignPass.text.toString().isEmpty()){
            binding.usrPassLayout.error= "Check Fields & Try Again"
            status=false
        }
        return status
    }
}