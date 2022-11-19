package com.harvin.evento.Users

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.harvin.evento.Adapters.RecyclerUsrCmpPkgAdapter
import com.harvin.evento.MessagingActivity
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.databinding.ActivityUserCmpDetailsBinding

class UserCmpDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUserCmpDetailsBinding
    var cmpID:String?=null
    var phone:String?=null
    private var databasePkg: DatabaseReference? = null
    var packageArrayList: ArrayList<PackageItemModel>? = null
    var homeAdapter: RecyclerUsrCmpPkgAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserCmpDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(FirebaseAuth.getInstance().currentUser != null){
            binding.phoneChat.visibility= View.VISIBLE
        }
        else{

            binding.phoneChat.visibility= View.GONE
        }

        cmpID=intent.getStringExtra("cmpID")
        phone=intent.getStringExtra("phone")
        binding.cmpNameHomeTV.text=intent.getStringExtra("cname")
        binding.locHomeTV.text=intent.getStringExtra("location")
        binding.cmpMailHomeTV.text=intent.getStringExtra("email")
        binding.yoeHomeTV.text=intent.getStringExtra("yoe")
        var service=intent.getStringExtra("service")

        val sb = StringBuilder()
        if(service!!.contains("f")){
            sb.append("Florist\t")
        }
        if(service!!.contains("d")){
            sb.append("Decorator\t")
        }
        if(service!!.contains("c")){
            sb.append("Caterers\t")
        }
        if(service!!.contains("a")){
            sb.append("All.")
        }
        binding.serviceHomeTV.text=sb.toString()

        binding.descHomeTV.text=intent.getStringExtra("desc")
        binding.usrCall.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phone"))
            startActivity(i)
        }

        binding.usrChat.setOnClickListener {
            val intent = Intent(this@UserCmpDetailsActivity, MessagingActivity::class.java)
            intent.putExtra("cmpID", cmpID)
            startActivity(intent)
        }
        binding.cmpHomeRecycler.setHasFixedSize(true)
        binding.cmpHomeRecycler.layoutManager= LinearLayoutManager(this)
        packageArrayList= arrayListOf()
        databasePkg = FirebaseDatabase.getInstance().getReference("package")
        val query = databasePkg!!.orderByChild("cmpID").equalTo(cmpID)

        val pkgValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    packageArrayList!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val packageItemModel = snapshot.getValue<PackageItemModel>()
                        if (packageItemModel != null) {
                            packageArrayList!!.add(packageItemModel)
                        }
                    }
                    homeAdapter = RecyclerUsrCmpPkgAdapter(applicationContext, packageArrayList!!)
                    binding.cmpHomeRecycler.adapter = homeAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        query.addValueEventListener(pkgValueEventListener)
    }
}