package com.harvin.evento.Company

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.databinding.ActivityCmpAddPackageBinding

class CmpAddPackageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCmpAddPackageBinding

    var databaseCmpPack: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCmpAddPackageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseCmpPack = FirebaseDatabase.getInstance().getReference("package")

        binding.cmpAddPackage.setOnClickListener {
            if (checkEditText()){
                val packID= databaseCmpPack!!.push().key.toString()
                val userID= Firebase.auth.currentUser?.uid.toString()
                val packageItemModel=PackageItemModel(userID,packID,binding.cmpPackName.text.toString(),binding.cmpPackDes.text.toString(),binding.cmpPrice.text.toString(),binding.cmpPCount.text.toString())
                databaseCmpPack!!.child(packID).setValue(packageItemModel)
                val i = Intent(applicationContext, CmpHomeActivity::class.java)
                startActivity(i)
            }
        }


    }

    fun checkEditText():Boolean{
        var status=true
        if(binding.cmpPackName.text.toString().isEmpty()){
            binding.cmpNameLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.cmpPCount.text.toString().isEmpty()){
            binding.cmpDoeLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.cmpPrice.text.toString().isEmpty()){
            binding.cmpCinLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.cmpPackDes.text.toString().isEmpty()){
            binding.cmpDesLayout.error= "Check Fields & Try Again"
            status=false
        }
        return status
    }
}