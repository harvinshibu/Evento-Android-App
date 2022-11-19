package com.harvin.evento.UserFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.harvin.evento.Adapters.RecyclerUserHomeAdapter
import com.harvin.evento.Model.CompanyModel
import com.harvin.evento.R
import com.harvin.evento.databinding.FragmentUserHomeBinding
import java.util.*
import kotlin.collections.ArrayList

class UserHomeFragment : Fragment() {
    private lateinit var binding: FragmentUserHomeBinding
    private var databaseCmp: DatabaseReference? = null
    var userID:String?=null
    var cmpArrayList: ArrayList<CompanyModel>? = null
    var homeAdapter: RecyclerUserHomeAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentUserHomeBinding.inflate(inflater,container,false)

        binding.usrHomeRecycler.setHasFixedSize(true)
        binding.usrHomeRecycler.layoutManager=LinearLayoutManager(context)
        cmpArrayList= arrayListOf()
        databaseCmp = FirebaseDatabase.getInstance().getReference("company")
        loadDataFromDatabase()
        Log.d("Errorr",cmpArrayList.toString())

        binding.filter.setOnClickListener {
            val builder = AlertDialog.Builder(this.requireContext())
            val alertView: View = layoutInflater.inflate(R.layout.filter_company, null)
            builder.setView(alertView)
            val cancel: AppCompatButton =alertView.findViewById(R.id.btn_filter_cancel)
            val filterCmp: AppCompatButton =alertView.findViewById(R.id.btn_filter)
            val flo: CheckBox =alertView.findViewById(R.id.floFilt)
            val cate: CheckBox =alertView.findViewById(R.id.cateFilt)
            val none: CheckBox =alertView.findViewById(R.id.noneFilt)
            val all: CheckBox =alertView.findViewById(R.id.allFilt)
            val dec: CheckBox =alertView.findViewById(R.id.decFilt)
            val alertDialog=builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window?.setGravity(Gravity.CENTER)
            alertDialog.show()
            all.setOnCheckedChangeListener { buttonView, isChecked ->
                if (all.isChecked){
                    flo.isChecked=true
                    cate.isChecked=true
                    dec.isChecked=true
                    none.isChecked=false
                }
                else{
                    flo.isChecked=false
                    cate.isChecked=false
                    dec.isChecked=false
                    none.isChecked=false
                }
            }
            none.setOnCheckedChangeListener { buttonView, isChecked ->
                if (none.isChecked){
                    flo.isChecked=false
                    cate.isChecked=false
                    dec.isChecked=false
                    all.isChecked=false
                }
            }
            cancel.setOnClickListener {
                alertDialog.dismiss()
            }
            filterCmp.setOnClickListener {
                val sb = StringBuilder()
                if(flo.isChecked){
                    sb.append("f")
                }
                if (cate.isChecked){
                    sb.append("c")
                }
                if (dec.isChecked){
                    sb.append("d")
                }
                if(all.isChecked){
                    sb.append("a")
                }
                if(none.isChecked){
                    loadDataFromDatabase()
                }
                val services=sb.toString()
                filter(services)
                alertDialog.dismiss()
            }
        }

        if(binding.searchHome !=null){
            binding.searchHome.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }
                override fun afterTextChanged(s: Editable) {
                    search(s.toString())
                }
            })
        }

        return binding.root
    }

    private fun search(string: String) {
        val searchList: ArrayList<CompanyModel> = arrayListOf()
        for (objectList in cmpArrayList!!) {
            if (objectList.cname.toString().lowercase(Locale.getDefault()).contains(string.lowercase(Locale.getDefault()))) {
                searchList.add(objectList)
            }
        }
        homeAdapter = RecyclerUserHomeAdapter(requireContext(), searchList)
        binding.usrHomeRecycler.adapter = homeAdapter
    }

    private fun filter(string: String) {
        val filterList: ArrayList<CompanyModel> = arrayListOf()
        for (objectList in cmpArrayList!!) {
            if (objectList.service.toString().lowercase(Locale.getDefault()).contains(string.lowercase(Locale.getDefault()))) {
                filterList.add(objectList)
            }
        }
        homeAdapter = RecyclerUserHomeAdapter(requireContext(), filterList)
        binding.usrHomeRecycler.adapter = homeAdapter
    }

    private fun loadDataFromDatabase(){
        if (cmpArrayList!!.isNotEmpty())
            cmpArrayList!!.clear()
        databaseCmp!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try{
                    if (snapshot.exists()){
                        for (postSnapshot in snapshot.children) {
                            val companyModel= postSnapshot.getValue<CompanyModel>()
                            cmpArrayList!!.add(companyModel!!)
                        }
                        homeAdapter = RecyclerUserHomeAdapter(context!!, cmpArrayList!!)
                        binding.usrHomeRecycler.setAdapter(homeAdapter)
                    }
                }catch (e:NullPointerException){}

            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }
}