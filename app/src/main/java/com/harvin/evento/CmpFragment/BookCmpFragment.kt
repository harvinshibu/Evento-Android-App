package com.harvin.evento.CmpFragment

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
import com.harvin.evento.Adapters.RecyclerCmpBookAdapter
import com.harvin.evento.Adapters.RecyclerUsrBookAdapter
import com.harvin.evento.Model.BookingModel
import com.harvin.evento.R
import com.harvin.evento.databinding.FragmentCmpBookBinding
import com.harvin.evento.databinding.FragmentUserBookBinding
import java.lang.NullPointerException

class BookCmpFragment : Fragment() {
    private lateinit var binding: FragmentCmpBookBinding
    private var databaseBook: DatabaseReference? = null
    var userID:String?=null
    var bookArrayList: ArrayList<BookingModel>? = null
    var bookAdapter: RecyclerCmpBookAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentCmpBookBinding.inflate(inflater,container,false)

        binding.cmpBookRecycler.setHasFixedSize(true)
        binding.cmpBookRecycler.layoutManager= LinearLayoutManager(context)
        bookArrayList= arrayListOf()
        userID= Firebase.auth.currentUser?.uid.toString()

        databaseBook = FirebaseDatabase.getInstance().getReference("book")
        val query = databaseBook!!.orderByChild("cmpID").equalTo(userID)

        val pkgValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try{
                    if (dataSnapshot.exists()) {
                        bookArrayList!!.clear()
                        for (snapshot in dataSnapshot.children) {
                            var bookItemModel = snapshot.getValue<BookingModel>()
                            bookArrayList!!.add(bookItemModel!!)
                        }
                        bookAdapter = RecyclerCmpBookAdapter(context!!, bookArrayList!!)
                        binding.cmpBookRecycler.adapter = bookAdapter
                    }
                }catch (e:NullPointerException){}
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        query.addValueEventListener(pkgValueEventListener)



        return binding.root
    }

}