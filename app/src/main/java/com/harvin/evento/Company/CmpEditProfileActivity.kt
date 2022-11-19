package com.harvin.evento.Company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.harvin.evento.Model.CompanyModel
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.databinding.ActivityCmpEditProfileActivtyBinding

class CmpEditProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityCmpEditProfileActivtyBinding
    private var databaseCmp: DatabaseReference? = null
    var userID:String?=null
    var service:String?=null
    var phone:String?=null
    var location:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCmpEditProfileActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        userID= Firebase.auth.currentUser?.uid.toString()
        databaseCmp = FirebaseDatabase.getInstance().getReference("company").child(userID!!)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue<CompanyModel>() != null) {
                    val key = dataSnapshot.key
                    if (key == userID) {
                        binding.cmpEditName.setText(dataSnapshot.child("cname").value.toString())
                        binding.cmpEditMail.setText(dataSnapshot.child("email").value.toString())
                        binding.cmpEditDoe.setText(dataSnapshot.child("yoe").value.toString())
                        binding.cmpEditCin.setText(dataSnapshot.child("cin").value.toString())
                        binding.cmpEditDes.setText(dataSnapshot.child("desc").value.toString())
                        phone=dataSnapshot.child("phone").value.toString()
                        location=dataSnapshot.child("location").value.toString()

                        service=dataSnapshot.child("service").value.toString()

                        if(service!!.contains("f")){
                            binding.flo.isChecked=true
                        }
                        if(service!!.contains("d")){
                            binding.dec.isChecked=true
                        }
                        if(service!!.contains("c")){
                            binding.cate.isChecked=true
                        }
                        if(service!!.contains("a")){
                            binding.all.isChecked=true
                        }

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        databaseCmp!!.addValueEventListener(valueEventListener)

        binding.all.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.all.isChecked){
                binding.flo.isChecked=true
                binding.cate.isChecked=true
                binding.dec.isChecked=true
            }else{
                binding.flo.isChecked=false
                binding.cate.isChecked=false
                binding.dec.isChecked=false
            }
        }








        binding.cmpDetailsEdit.setOnClickListener {


            val sb = StringBuilder()
            if(binding.flo.isChecked){
                sb.append("f")
            }
            if (binding.cate.isChecked){
                sb.append("c")
            }
            if (binding.dec.isChecked){
                sb.append("d")
            }
            if(binding.all.isChecked){
                sb.append("a")
            }
            val services=sb.toString()
            val i=Intent(applicationContext,CmpEditProfileLocActivity::class.java)
            i.putExtra("cname", binding.cmpEditName.text.toString())
            i.putExtra("email", binding.cmpEditMail.text.toString())
            i.putExtra("yoe", binding.cmpEditDoe.text.toString())
            i.putExtra("cin", binding.cmpEditCin.text.toString())
            i.putExtra("desc", binding.cmpEditDes.text.toString())
            i.putExtra("phone", phone)
            i.putExtra("location", location)
            i.putExtra("service", services)
            startActivity(i)
        }
    }
}