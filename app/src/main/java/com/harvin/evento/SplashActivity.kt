package com.harvin.evento

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.harvin.evento.Company.CmpHomeActivity
import com.harvin.evento.Model.CompanyModel
import com.harvin.evento.Model.UserModel
import com.harvin.evento.Users.UserHomeActivity

class SplashActivity : AppCompatActivity() {
    private var databaseCmp: DatabaseReference? = null
    private var databaseUsr: DatabaseReference? = null
    var userID:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userID= Firebase.auth.currentUser?.uid.toString()
        databaseCmp = FirebaseDatabase.getInstance().getReference("company").child(userID!!)
        databaseUsr = FirebaseDatabase.getInstance().getReference("users").child(userID!!)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            if(FirebaseAuth.getInstance().currentUser != null){
                checkCmp(databaseCmp!!, userID!!)
                checkUsr(databaseUsr!!, userID!!)
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }

        }, 1800)
    }

    private fun checkCmp(databaseCmp: DatabaseReference, userID:String){
        val i = Intent(this, CmpHomeActivity::class.java)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue<CompanyModel>() != null) {
                    val key = dataSnapshot.key
                    if (key == userID) {
                        startActivity(i)
                        finish()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        databaseCmp.addValueEventListener(valueEventListener)

    }

    private fun checkUsr(databaseUsr: DatabaseReference, userID:String){
        val i = Intent(this, UserHomeActivity::class.java)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue<UserModel>() != null) {
                    val key = dataSnapshot.key
                    if (key == userID) {
                        startActivity(i)
                        finish()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        databaseUsr.addValueEventListener(valueEventListener)
    }
}