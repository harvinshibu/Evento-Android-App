package com.harvin.evento.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.harvin.evento.Company.CmpEditPackageActivity
import com.harvin.evento.MessagingActivity
import com.harvin.evento.Model.BookingModel
import com.harvin.evento.Model.PackageItemModel
import com.harvin.evento.R

class RecyclerCmpBookAdapter (var mContext: Context, private val mList: List<BookingModel>) : RecyclerView.Adapter<RecyclerCmpBookAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerCmpBookAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_cmp_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerCmpBookAdapter.ViewHolder, position: Int) {
        val PackageItem = mList[position]
        holder.titleTV.text = PackageItem.pkgName
        holder.priceTV.text = "\u20B9"+PackageItem.pkgPrice
        holder.packDes.text = PackageItem.pkgDesc
        if (PackageItem.status.equals("wait")){
            holder.cmpApprove.text ="Pending! Waiting for approval."
            holder.cmpApprove.setTextColor(ContextCompat.getColor(mContext, R.color.red))
        }
        else if(PackageItem.status.equals("approve")){
            holder.cmpApprove.text ="Approved."
            holder.approve.visibility=View.GONE
            holder.cmpApprove.setTextColor(ContextCompat.getColor(mContext, R.color.green))
        }
        else{
            holder.cmpApprove.text ="Declined."
            holder.cmpApprove.setTextColor(ContextCompat.getColor(mContext, R.color.red))
            holder.approve.visibility=View.GONE
            holder.decline.visibility=View.GONE
        }
        holder.approve.setOnClickListener {
            var bookID: String? = PackageItem.bookID.toString()
            var databaseCmpBook: DatabaseReference? = FirebaseDatabase.getInstance().getReference("book").child(bookID!!)
            val bookingModel= BookingModel(bookID,PackageItem.packID,PackageItem.cmpID,PackageItem.userID,PackageItem.pkgName,PackageItem.pkgPrice,PackageItem.pkgDesc,"approve")
            databaseCmpBook!!.setValue(bookingModel)
        }
        holder.decline.setOnClickListener {
            var bookID: String? = PackageItem.bookID.toString()
            var databaseCmpBook: DatabaseReference? = FirebaseDatabase.getInstance().getReference("book").child(bookID!!)
            val bookingModel= BookingModel(bookID,PackageItem.packID,PackageItem.cmpID,PackageItem.userID,PackageItem.pkgName,PackageItem.pkgPrice,PackageItem.pkgDesc,"decline")
            databaseCmpBook!!.setValue(bookingModel)
        }
        holder.chatCmp.setOnClickListener {
            val intent = Intent(it.context, MessagingActivity::class.java)
            intent.putExtra("cmpID", PackageItem.userID.toString())
            it.context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        val priceTV: TextView = itemView.findViewById(R.id.priceTV)
        val packDes: TextView = itemView.findViewById(R.id.packDes)
        val cmpApprove: TextView = itemView.findViewById(R.id.cmp_approve)
        val approve: ImageButton = itemView.findViewById(R.id.approve)
        val decline: ImageButton = itemView.findViewById(R.id.decline)
        val chatCmp: ImageButton = itemView.findViewById(R.id.chat_cmp)

    }


}