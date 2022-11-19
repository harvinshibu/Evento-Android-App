package com.harvin.evento.UserFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.harvin.evento.Adapters.RecyclerCmpHomeAdapter
import com.harvin.evento.Adapters.RecyclerUsrBookAdapter
import com.harvin.evento.Model.BookingModel
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.R
import com.harvin.evento.databinding.FragmentUserBookBinding
import java.lang.NullPointerException

class UserBookFragment : Fragment() {
    private lateinit var binding:FragmentUserBookBinding
    private var databaseBook: DatabaseReference? = null
    var userID:String?=null
    var bookArrayList: ArrayList<BookingModel>? = null
    var bookAdapter: RecyclerUsrBookAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentUserBookBinding.inflate(inflater,container,false)

        binding.usrBookRecycler.setHasFixedSize(true)
        binding.usrBookRecycler.layoutManager= LinearLayoutManager(context)
        bookArrayList= arrayListOf()
        userID= Firebase.auth.currentUser?.uid.toString()

        databaseBook = FirebaseDatabase.getInstance().getReference("book")
        val query = databaseBook!!.orderByChild("userID").equalTo(userID)

        val pkgValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        bookArrayList!!.clear()
                        for (snapshot in dataSnapshot.children) {
                            var bookItemModel = snapshot.getValue<BookingModel>()
                            bookArrayList!!.add(bookItemModel!!)

                        }
                            bookAdapter = RecyclerUsrBookAdapter(context!!, bookArrayList!!)
                            binding.usrBookRecycler.adapter = bookAdapter

                    }
                }catch (e: NullPointerException){

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        query.addValueEventListener(pkgValueEventListener)

        return binding.root
    }

}