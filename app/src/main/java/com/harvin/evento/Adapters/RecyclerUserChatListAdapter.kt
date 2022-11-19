package com.harvin.evento.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harvin.evento.Company.CmpEditPackageActivity
import com.harvin.evento.MessagingActivity
import com.harvin.evento.Model.CompanyModel
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.Model.UserModel
import com.harvin.evento.R

class RecyclerUserChatListAdapter (var mContext: Context, private val mList: List<UserModel>) : RecyclerView.Adapter<RecyclerUserChatListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val PackageItem = mList[position]
        holder.titleTV.text=PackageItem.cname
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MessagingActivity::class.java)
            intent.putExtra("cmpID", PackageItem.userID.toString())
            it.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleTV: TextView = itemView.findViewById(R.id.username1)

    }


}