package com.harvin.evento.Users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.harvin.evento.Model.BookingModel
import com.harvin.evento.R
import com.harvin.evento.databinding.ActivityUserBookConfirmBinding

class UserBookConfirmActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserBookConfirmBinding
    var databaseBook: DatabaseReference? = null
    var userID:String?=null
    val bookID:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserBookConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pkgNameConfirmTV.text=intent.getStringExtra("pkgName")
        binding.pkgPriceConfirmTV.text="\u20B9"+intent.getStringExtra("pkgPrice")
        binding.pkgDescConfirmTV.text=intent.getStringExtra("pkgDesc")
        binding.nPplConfirmTV.text=intent.getStringExtra("pkgPpl")
        var title=intent.getStringExtra("pkgName")+" - "+intent.getStringExtra("pkgPpl")+" ppl"

        databaseBook= FirebaseDatabase.getInstance().getReference("book")

        binding.usrConfirmBook.setOnClickListener {
            userID= FirebaseAuth.getInstance().currentUser!!.uid
            val bookID= databaseBook!!.push().key.toString()
            val bookingModel= BookingModel(bookID,intent.getStringExtra("packID"),intent.getStringExtra("cmpID"),userID,title,intent.getStringExtra("pkgPrice"),intent.getStringExtra("pkgDesc"),"wait")
            databaseBook!!.child(bookID).setValue(bookingModel)
            Toast.makeText(applicationContext,"Request Sent! Waiting for Approval.", Toast.LENGTH_LONG).show()
            val i=Intent(applicationContext,UserHomeActivity::class.java)
            i.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

    }
}