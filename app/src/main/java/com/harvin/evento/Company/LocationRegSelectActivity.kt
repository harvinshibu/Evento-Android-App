package com.harvin.evento.Company

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.harvin.evento.Model.CompanyModel
import com.harvin.evento.databinding.ActivityLocationRegSelectBinding
import java.io.IOException
import java.util.*


class LocationRegSelectActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLocationRegSelectBinding
    var databaseCmp: DatabaseReference? = null

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLocationRegSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseCmp = FirebaseDatabase.getInstance().getReference("company")

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext,"AIzaSyDv8lfMsQukTHFgQ4walL_mR6PzxiAP3sI")
        }

        binding.cmpLocReg.isFocusable=false
        binding.cmpLocReg.setOnClickListener {
                binding.cmpLocReg.setText("")
                val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS)
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(this@LocationRegSelectActivity)
                startActivityForResult(intent, 134)
        }

        binding.getCurLoc.setOnClickListener {
            binding.cmpLocReg.setText("")
            getCurrentLoc()
        }

        binding.cmpDetailSub.setOnClickListener {
            if(binding.cmpLocReg.text.toString().isEmpty()){
                binding.locLayout.error="Check Fields & Try Again"
            }
            else{
                val userID= Firebase.auth.currentUser?.uid.toString()
                val companyModel=CompanyModel(userID,intent.getStringExtra("phone").toString(),intent.getStringExtra("cname").toString(),intent.getStringExtra("email").toString(),intent.getStringExtra("yoe").toString(),intent.getStringExtra("cin").toString(),intent.getStringExtra("desc").toString(),intent.getStringExtra("service").toString(),binding.cmpLocReg.text.toString())
                databaseCmp!!.child(userID).setValue(companyModel)
                val i = Intent(applicationContext, CmpHomeActivity::class.java)
                i.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 134) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        binding.cmpLocReg.setText(place.address)
                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {

                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage ?: "")
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getCurrentLoc(){
        if (ActivityCompat.checkSelfPermission(this@LocationRegSelectActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient!!.lastLocation.addOnCompleteListener { task ->
                val location = task.result
                if (location != null) {
                    val geocoder = Geocoder(this@LocationRegSelectActivity, Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        binding.cmpLocReg.setText(addresses[0].getAddressLine(0))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        else {
            ActivityCompat.requestPermissions(this@LocationRegSelectActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 44)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==44){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLoc()
            }
        }
    }
}