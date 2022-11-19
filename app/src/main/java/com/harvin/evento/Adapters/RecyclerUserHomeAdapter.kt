package com.harvin.evento.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harvin.evento.Company.CmpEditPackageActivity
import com.harvin.evento.Model.CompanyModel
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.R
import com.harvin.evento.Users.UserCmpDetailsActivity

class RecyclerUserHomeAdapter (var mContext: Context, private val mList: List<CompanyModel>) : RecyclerView.Adapter<RecyclerUserHomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerUserHomeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.usr_home_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val PackageItem = mList[position]
        holder.titleTV.text = PackageItem.cname
        holder.emailTV.text = PackageItem.email
        holder.locTV.text = PackageItem.location
        holder.itemLayout.setOnClickListener {
            val i=Intent(mContext,UserCmpDetailsActivity::class.java)
            i.putExtra("cname",PackageItem.cname)
            i.putExtra("cmpID",PackageItem.cmpID)
            i.putExtra("phone",PackageItem.phone)
            i.putExtra("email",PackageItem.email)
            i.putExtra("yoe",PackageItem.yoe)
            i.putExtra("cin",PackageItem.cin)
            i.putExtra("desc",PackageItem.desc)
            i.putExtra("service",PackageItem.service)
            i.putExtra("location",PackageItem.location)
            mContext.startActivity(i)
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val emailTV: TextView = itemView.findViewById(R.id.emailTV)
        val locTV: TextView = itemView.findViewById(R.id.locTV)
        val itemLayout:LinearLayout=itemView.findViewById(R.id.itemLayout)

    }


}