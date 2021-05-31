package com.benone.mvvm2.utils

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.benone.mvvm2.R
import com.benone.mvvm2.WanApplication

class ToastUtils {

    companion object{

        private var toast: Toast? = null

        fun show(content:String){
            val inflater = LayoutInflater.from(WanApplication.instance)
            val view = inflater.inflate(R.layout.toast_layout,null)
            val text= view.findViewById<TextView>(R.id.toast_text)
            text.text = content
            toast = Toast(WanApplication.instance)
            toast!!.setGravity(Gravity.CENTER,0,0)
            toast!!.duration = Toast.LENGTH_SHORT
            toast!!.view = view
            toast!!.show()
        }
    }
}