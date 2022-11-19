package com.harvin.evento.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.harvin.evento.Model.ChatModel
import com.harvin.evento.R

class RecyclerMessageAdapter (var mContext: Context, private val chatList: List<ChatModel>) : RecyclerView.Adapter<RecyclerMessageAdapter.ViewHolder>() {
    val MSG_TYPE_LEFT = 0
    val MSG_TYPE_RIGHT = 1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        if(viewType==MSG_TYPE_RIGHT){
        val view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false)
        return ViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item_left, parent, false)
            return ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatItem = chatList[position]
        holder.show_message.text=chatItem.msg.toString()
        holder.profile_image.setImageResource(R.mipmap.ic_launcher)
        if (position == chatList.size - 1) {
            if (chatItem.isseen == true) {
                holder.txt_seen.text = "Seen"
            } else {
                holder.txt_seen.text = "Delivered"
            }
        } else {
            holder.txt_seen.visibility = View.GONE
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txt_seen: TextView = itemView.findViewById(R.id.txt_seen)
        val show_message: TextView = itemView.findViewById(R.id.show_message)
        val profile_image: ImageView = itemView.findViewById(R.id.profile_image)

    }

    override fun getItemViewType(position: Int): Int {
        if(chatList[position].sender!!.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {
            return MSG_TYPE_RIGHT
        } else {
            return MSG_TYPE_LEFT
        }
    }
}