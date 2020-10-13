package com.ashok.bible.databinding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ashok.bible.utils.Utils


class DataBindingAdapter {
    companion object {

        @BindingAdapter("createdData")
        @JvmStatic
        fun setCreatedData(view: TextView, resource: String) {
            if (resource.isNotEmpty()){
                view.text = Utils.getUtcToDDMMYYYYHHMMA(resource)
            }else{
                view.text = ""
            }
        }
    }

}