package com.harvin.evento.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harvin.evento.Company.CmpEditPackageActivity
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.R

class RecyclerCmpHomeAdapter (var mContext: Context, private val mList: List<PackageItemModel>) : RecyclerView.Adapter<RecyclerCmpHomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerCmpHomeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.package_item_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerCmpHomeAdapter.ViewHolder, position: Int) {
        val PackageItem = mList[position]
        holder.titleTV.text = PackageItem.title+" - "+PackageItem.pcount+" ppl"
        holder.priceItemTV.text = "\u20B9"+PackageItem.price
        holder.descItemTV.text = PackageItem.desc
        holder.editItemButton.setOnClickListener {
            val i=Intent(mContext,CmpEditPackageActivity::class.java)
            i.putExtra("title",PackageItem.title)
            i.putExtra("desc",PackageItem.desc)
            i.putExtra("price",PackageItem.price)
            i.putExtra("pcount",PackageItem.pcount)
            i.putExtra("cmpID",PackageItem.cmpID)
            i.putExtra("packID",PackageItem.packID)
            mContext.startActivity(i)
        }


    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val priceItemTV: TextView = itemView.findViewById(R.id.priceItemTV)
        val descItemTV: TextView = itemView.findViewById(R.id.descItemTV)
        val editItemButton: TextView = itemView.findViewById(R.id.editItemButton)

    }


}