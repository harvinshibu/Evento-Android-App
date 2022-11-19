package com.harvin.evento.CmpFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.harvin.evento.Adapters.RecyclerCmpChatListAdapter
import com.harvin.evento.Adapters.RecyclerUserChatListAdapter
import com.harvin.evento.Model.ChatListModel
import com.harvin.evento.Model.CompanyModel
import com.harvin.evento.Model.UserModel
import com.harvin.evento.R
import com.harvin.evento.databinding.FragmentChatCmpBinding
import com.harvin.evento.databinding.FragmentUserChatBinding


class ChatCmpFragment : Fragment() {
    private var userAdapter: RecyclerUserChatListAdapter? = null
    private var mUsers: ArrayList<UserModel>? = null
    private lateinit var binding: FragmentChatCmpBinding
    var userId: String? = null
    var reference: DatabaseReference? = null

    private var userList: ArrayList<ChatListModel>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentChatCmpBinding.inflate(inflater,container,false)

        binding.cmpChatRecycler.setHasFixedSize(true)
        binding.cmpChatRecycler.layoutManager = LinearLayoutManager(context)

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
        reference = FirebaseDatabase.getInstance().getReference("users")
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try{
                    mUsers!!.clear()
                    for (snapshot1 in snapshot.children) {
                        val cmpItemModel = snapshot1.getValue<UserModel>()
                        for (chatList in userList!!) {
                            if (cmpItemModel!!.userID.equals(chatList.id)) {
                                mUsers!!.add(cmpItemModel)
                            }
                        }
                    }
                    Log.d("Error---",mUsers.toString())
                    userAdapter = RecyclerUserChatListAdapter(context!!, mUsers!!)
                    binding.cmpChatRecycler.setAdapter(userAdapter)
                }catch (e:NullPointerException){ }

            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}