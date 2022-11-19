package com.harvin.evento.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.harvin.evento.Model.BookingModel
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.R
import com.harvin.evento.Users.UserBookConfirmActivity
import com.harvin.evento.Users.UserCmpDetailsActivity

class RecyclerUsrCmpPkgAdapter (var mContext: Context,private val mList: List<PackageItemModel>) : RecyclerView.Adapter<RecyclerUsrCmpPkgAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerUsrCmpPkgAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.package_usr_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val PackageItem = mList[position]
        holder.titleTV.text = PackageItem.title+" - "+PackageItem.pcount+" ppl"
        holder.priceItemTV.text = "\u20B9"+PackageItem.price
        holder.descItemTV.text = PackageItem.desc
        if(FirebaseAuth.getInstance().currentUser != null){
            holder.editItemButton.visibility= View.VISIBLE
        }
        else{

            holder.editItemButton.visibility= View.GONE
        }
        holder.editItemButton.setOnClickListener {
            val i= Intent(it.context, UserBookConfirmActivity::class.java)
            i.putExtra("packID",PackageItem.packID)
            i.putExtra("cmpID",PackageItem.cmpID)
            i.putExtra("pkgName",PackageItem.title)
            i.putExtra("pkgPrice",PackageItem.price)
            i.putExtra("pkgPpl",PackageItem.pcount)
            i.putExtra("pkgDesc",PackageItem.desc)
            it.context.startActivity(i)
        }
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val priceItemTV: TextView = itemView.findViewById(R.id.priceItemTV)
        val descItemTV: TextView = itemView.findViewById(R.id.descItemTV)
        val editItemButton: TextView = itemView.findViewById(R.id.editItemButton)
    }

}