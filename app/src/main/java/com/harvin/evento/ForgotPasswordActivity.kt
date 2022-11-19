package com.harvin.evento

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.harvin.evento.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding:ActivityForgotPasswordBinding
    var fAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fAuth = FirebaseAuth.getInstance()
        binding.forgotPass.setOnClickListener {
            binding.rProgLayout.visibility= View.VISIBLE
            fAuth!!.sendPasswordResetEmail(binding.forgotMail.text.toString().trim()).addOnCompleteListener {
                if (it.isSuccessful){
                    binding.rProgLayout.visibility= View.GONE
                    Toast.makeText(this@ForgotPasswordActivity, "Reset Link Sent To Email.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                }
                else{
                    binding.rProgLayout.visibility= View.GONE
                    Toast.makeText(this@ForgotPasswordActivity, "Error ! Reset Link Not Sent", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}