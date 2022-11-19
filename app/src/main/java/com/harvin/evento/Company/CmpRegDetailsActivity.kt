package com.harvin.evento.Company

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harvin.evento.databinding.ActivityCmpRegDetailsBinding

class CmpRegDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCmpRegDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCmpRegDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cmpDetailsReg.setOnClickListener {
            val sb = StringBuilder()
            if(binding.flo.isChecked){
                sb.append("f")
            }
            if (binding.cate.isChecked){
                sb.append("c")
            }
            if (binding.dec.isChecked){
                sb.append("d")
            }
            if(binding.all.isChecked){
                binding.flo.isChecked=true
                binding.cate.isChecked=true
                binding.dec.isChecked=true
                sb.append("a")
            }

            val services=sb.toString()

            if(checkEditText()){
                val i = Intent(applicationContext, LocationRegSelectActivity::class.java)
                i.putExtra("phone", intent.getStringExtra("phone").toString())
                i.putExtra("cname", binding.cmpRegName.text.toString())
                i.putExtra("email", binding.cmpRegMail.text.toString())
                i.putExtra("yoe", binding.cmpRegDoe.text.toString())
                i.putExtra("cin", binding.cmpRegCin.text.toString())
                i.putExtra("desc", binding.cmpRegDes.text.toString())
                i.putExtra("service", services)
                startActivity(i)
            }

        }
    }

    fun checkEditText():Boolean{
        var status=true
        if(binding.cmpRegName.text.toString().isEmpty()){
            binding.cmpNameLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.cmpRegMail.text.toString().isEmpty()){
            binding.cmpMailLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.cmpRegDoe.text.toString().isEmpty()){
            binding.cmpDoeLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.cmpRegCin.text.toString().isEmpty()){
            binding.cmpCinLayout.error= "Check Fields & Try Again"
            status=false
        }
        if(binding.cmpRegDes.text.toString().isEmpty()){
            binding.cmpDesLayout.error= "Check Fields & Try Again"
            status=false
        }
        return status
    }

}