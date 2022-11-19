package com.harvin.evento.CmpFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.harvin.evento.Adapters.RecyclerCmpHomeAdapter
import com.harvin.evento.Company.CmpAddPackageActivity
import com.harvin.evento.Company.CmpEditProfileActivity
import com.harvin.evento.Model.CompanyModel
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.databinding.FragmentHomeCmpBinding

class HomeCmpFragment : Fragment() {

    private lateinit var binding: FragmentHomeCmpBinding
    private var databaseCmp: DatabaseReference? = null
    private var databasePkg: DatabaseReference? = null
    var userID:String?=null
    var packageArrayList: ArrayList<PackageItemModel>? = null
    var homeAdapter:RecyclerCmpHomeAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=FragmentHomeCmpBinding.inflate(inflater,container,false)

        binding.cmpHomeRecycler.setHasFixedSize(true)
        binding.cmpHomeRecycler.layoutManager=LinearLayoutManager(context)
        packageArrayList= arrayListOf()
        userID= Firebase.auth.currentUser?.uid.toString()
        databaseCmp = FirebaseDatabase.getInstance().getReference("company").child(userID!!)
        databasePkg = FirebaseDatabase.getInstance().getReference("package")
        val query = databasePkg!!.orderByChild("cmpID").equalTo(userID)

        binding.addPackages.setOnClickListener {
            startActivity(Intent(activity,CmpAddPackageActivity::class.java))
        }

        loadCmpDetails(databaseCmp!!)

        val pkgValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try{
                    if (dataSnapshot.exists()) {
                        packageArrayList!!.clear()
                        for (snapshot in dataSnapshot.children) {
                            val packageItemModel = snapshot.getValue<PackageItemModel>()
                            if (packageItemModel != null) {
                                packageArrayList!!.add(packageItemModel)
                            }
                        }
                        homeAdapter = RecyclerCmpHomeAdapter(context!!, packageArrayList!!)
                        binding.cmpHomeRecycler.adapter = homeAdapter
                    }
                }catch (e:NullPointerException){}

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        query.addValueEventListener(pkgValueEventListener)

        return binding.root
    }

    private fun loadCmpDetails(databaseCmp: DatabaseReference){
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue<CompanyModel>() != null) {
                    val key = dataSnapshot.key
                    if (key == userID) {
                        binding.cmpNameHomeTV.text=dataSnapshot.child("cname").value.toString()
                        binding.cmpMailHomeTV.text=dataSnapshot.child("email").value.toString()
                        binding.locHomeTV.text=dataSnapshot.child("location").value.toString()+", "+dataSnapshot.child("phone").value.toString()
                        binding.yoeHomeTV.text=dataSnapshot.child("yoe").value.toString()
                        binding.cinHomeTV.text=dataSnapshot.child("cin").value.toString()
                        binding.descHomeTV.text=dataSnapshot.child("desc").value.toString()
                        binding.serviceHomeTV.text=dataSnapshot.child("service").value.toString()

                        val sb = StringBuilder()
                        if(binding.serviceHomeTV.text.toString().contains("f")){
                            sb.append("Florist  ")
                        }
                        if(binding.serviceHomeTV.text.toString().contains("d")){
                            sb.append("Decorator  ")
                        }
                        if(binding.serviceHomeTV.text.toString().contains("c")){
                            sb.append("Caterers  ")
                        }
                        if(binding.serviceHomeTV.text.toString().contains("a")){
                            sb.append("All.")
                        }
                        binding.serviceHomeTV.text=sb.toString()

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        databaseCmp.addValueEventListener(valueEventListener)
    }


}
