package com.ashok.bible.ui.adapter

import android.content.Context
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import com.ashok.bible.R
import com.ashok.bible.data.local.entry.BibleIndexModelEntry
import com.ashok.bible.data.local.entry.BibleModelEntry
import com.ashok.bible.ui.bibleindex.BibleIndexActivity
import com.ashok.bible.ui.model.BibleNumberIndexModel
import com.lakki.kotlinlearning.view.base.RecyclerBaseAdapter


class BibleIndexNumberAdapter constructor(var mContext: BibleIndexActivity?, var list: ArrayList<Int>, var originalList: ArrayList<Int> = list):
    RecyclerBaseAdapter<Int?>(), Filterable {

    override fun getDataAtPosition(position: Int): Int? {
        return list[position]
    }

    override fun getLayoutIdForType(viewType: Int): Int {
        return R.layout.bible_index_number_row
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(list: ArrayList<Int>) {
        this.list = list
        this.originalList = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    list = originalList
                } else {
                    val filteredList: ArrayList<Int> = ArrayList()
                    for (obj in originalList) {
                        if (obj.toString().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(obj)
                        }
                    }
                    list = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = list
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                list = filterResults.values as ArrayList<Int>
                notifyDataSetChanged()
            }
        }

    }
    public fun onClick(view: View, chaptertId:Int){
        mContext?.onclickChapter(chaptertId)
    }

}