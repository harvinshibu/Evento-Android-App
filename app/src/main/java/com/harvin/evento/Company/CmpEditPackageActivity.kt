package com.harvin.evento.Company

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.R
import com.harvin.evento.databinding.ActivityCmpEditPackageBinding

class CmpEditPackageActivity : AppCompatActivity() {
    private  lateinit var binding:ActivityCmpEditPackageBinding
    private var cmpID: String? = null
    private var packID: String? = null
    var databaseCmpPack: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCmpEditPackageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cmpPackName.setText(intent.getStringExtra("title").toString())
        binding.cmpPackDes.setText(intent.getStringExtra("desc").toString())
        binding.cmpPrice.setText(intent.getStringExtra("price").toString())
        binding.cmpPCount.setText(intent.getStringExtra("pcount").toString())
        cmpID=intent.getStringExtra("cmpID").toString()
        packID=intent.getStringExtra("packID").toString()
        databaseCmpPack = FirebaseDatabase.getInstance().getReference("package").child(packID!!)

        binding.delPack.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)
            val alertView: View = LayoutInflater.from(it.context).inflate(R.layout.dialog_delete, null)
            val deButton: Button =alertView.findViewById(R.id.btn_action_delete)
            builder.setView(alertView)
            val alertDialog=builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window?.setGravity(Gravity.CENTER)
            alertDialog.show()
            alertDialog.window?.setLayout(900,800)
            deButton.setOnClickListener {
                databaseCmpPack!!.removeValue()
                Toast.makeText(it.context, "Package Removed", Toast.LENGTH_SHORT).show()
                val i = Intent(applicationContext, CmpHomeActivity::class.java)
                startActivity(i)
                alertDialog.dismiss()

            }
        }

        binding.cmpEditPackage.setOnClickListener {
            if(checkEditText()){
                val packageItemModel= PackageItemModel(cmpID,packID,binding.cmpPackName.text.toString(),binding.cmpPackDes.text.toString(),binding.cmpPrice.text.toString(),binding.cmpPCount.text.toString())
                databaseCmpPack!!.setValue(packageItemModel)
                Toast.makeText(applicationContext, "Package Updated", Toast.LENGTH_LONG).show()
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