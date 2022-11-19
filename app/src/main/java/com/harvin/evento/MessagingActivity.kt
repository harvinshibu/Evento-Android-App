package com.harvin.evento

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.harvin.evento.Adapters.RecyclerMessageAdapter
import com.harvin.evento.Model.*
import com.harvin.evento.databinding.ActivityMessagingBinding

class MessagingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMessagingBinding
    var chatArrayList: ArrayList<ChatModel >? = null
    var messageAdapter: RecyclerMessageAdapter?=null
    private var databaseChat: DatabaseReference? = null
    private var databaseChatUsr: DatabaseReference? = null
    private var databaseCmp: DatabaseReference? = null
    var userID: String? = null
    var cmpID: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMessagingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.msgView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        binding.msgView.layoutManager=linearLayoutManager
        userID= FirebaseAuth.getInstance().currentUser!!.uid
        cmpID=intent.getStringExtra("cmpID")

        binding.btnSend.setOnClickListener {
            if (binding.textSend.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Message is Empty !!",Toast.LENGTH_SHORT).show()
            }else{

                sendMessage(userID!!, cmpID!!,binding.textSend.text.toString())

            }
            binding.textSend.setText("")
        }

        readMessages(userID!!, cmpID!!)
        databaseChatUsr = FirebaseDatabase.getInstance().getReference("users").child(cmpID!!)
        databaseCmp=FirebaseDatabase.getInstance().getReference("company").child(cmpID!!)
        checkCmp(databaseCmp!!,cmpID!!)
        checkUsr(databaseChatUsr!!,cmpID!!)





    }

    /*private fun seenMessage(usersId: String) {
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        seenListener = reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val chats: Chats? = dataSnapshot.getValue(Chats::class.java)
                    if (chats.getReceiver().equals(fuser.getUid()) && chats.getSender()
                            .equals(usersId)
                    ) {
                        val hashMap = HashMap<String, Any>()
                        hashMap["isseen"] = true
                        dataSnapshot.ref.updateChildren(hashMap)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }*/

    private fun checkCmp(databaseCmp: DatabaseReference, userID:String){
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue<CompanyModel>() != null) {
                    val key = dataSnapshot.key
                    if (key == userID) {
                        databaseCmp.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                binding.username.text=snapshot.child("cname").value.toString()
                                binding.profImg.setImageResource(R.mipmap.ic_launcher)
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        databaseCmp.addValueEventListener(valueEventListener)

    }

    private fun checkUsr(databaseUsr: DatabaseReference, userID:String){
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.getValue<UserModel>() != null) {
                    val key = dataSnapshot.key
                    if (key == userID) {
                        databaseUsr.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                binding.username.text=snapshot.child("cname").value.toString()
                                binding.profImg.setImageResource(R.mipmap.ic_launcher)
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        databaseUsr.addValueEventListener(valueEventListener)
    }


    private fun readMessages(myid: String, userid: String) {
        chatArrayList= arrayListOf()
        databaseChat = FirebaseDatabase.getInstance().getReference("Chats")

        val chatValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    chatArrayList!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val chatItemModel = snapshot.getValue<ChatModel>()
                        if (chatItemModel != null) {
                            if((chatItemModel.receiver.toString() == myid && chatItemModel.sender.toString() == userid) || (chatItemModel.receiver.toString() == userid && chatItemModel.sender.toString() == myid))
                            chatArrayList!!.add(chatItemModel)
                        }
                    }
                    Log.d("Error---",chatArrayList.toString())
                    messageAdapter = RecyclerMessageAdapter(applicationContext, chatArrayList!!)
                    binding.msgView.adapter = messageAdapter
                    messageAdapter!!.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        databaseChat!!.addValueEventListener(chatValueEventListener)
    }

    private fun sendMessage(sender: String, receiver: String, message: String) {
        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        val chatModel=ChatModel(sender,receiver,message,false)
        reference.push().setValue(chatModel)

        val chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
            .child(sender)
            .child(receiver)
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    chatRef.child("id").setValue(receiver)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        val chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
            .child(receiver)
            .child(sender)
        chatRefReceiver.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRefReceiver.child("id").setValue(sender)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }
}