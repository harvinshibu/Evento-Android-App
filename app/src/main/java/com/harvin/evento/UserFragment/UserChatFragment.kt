package com.harvin.evento.UserFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.harvin.evento.Adapters.RecyclerCmpChatListAdapter
import com.harvin.evento.Model.ChatListModel
import com.harvin.evento.Model.CompanyModel
import com.harvin.evento.databinding.FragmentUserChatBinding


class UserChatFragment : Fragment() {
    private var userAdapter: RecyclerCmpChatListAdapter? = null
    private var mUsers: ArrayList<CompanyModel>? = null
    private lateinit var binding:FragmentUserChatBinding
    var userId: String? = null
    var reference: DatabaseReference? = null

    private var userList: ArrayList<ChatListModel>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentUserChatBinding.inflate(inflater,container,false)

        binding.usrChatRecycler.setHasFixedSize(true)
        binding.usrChatRecycler.layoutManager = LinearLayoutManager(context)

        userId = FirebaseAuth.getInstance().currentUser!!.uid
        userList = arrayListOf()


        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(userId!!)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList!!.clear()
                for (snapshot1 in snapshot.children) {
                    val chatlist = snapshot1.getValue<ChatListModel>()
                    userList!!.add(chatlist!!)
                }

                Log.d("ErrorList---",userList.toString())
                chatList()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return binding.root
    }

    private fun chatList() {
        mUsers = arrayListOf()
        reference = FirebaseDatabase.getInstance().getReference("company")
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try{
                    mUsers!!.clear()
                    for (snapshot1 in snapshot.children) {
                        val cmpItemModel = snapshot1.getValue<CompanyModel>()
                        for (chatList in userList!!) {
                            if (cmpItemModel!!.cmpID.equals(chatList.id)) {
                                mUsers!!.add(cmpItemModel)
                            }
                        }
                    }
                    Log.d("Error---",mUsers.toString())
                    userAdapter = RecyclerCmpChatListAdapter(context!!, mUsers!!)
                    binding.usrChatRecycler.setAdapter(userAdapter)
                }catch (e:NullPointerException){ }

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}