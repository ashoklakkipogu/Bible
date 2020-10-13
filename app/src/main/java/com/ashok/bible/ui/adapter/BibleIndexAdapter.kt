package com.ashok.bible.ui.adapter

import android.view.View
import android.widget.Filter
import android.widget.Filterable
import com.ashok.bible.R
import com.ashok.bible.data.local.entry.BibleIndexModelEntry
import com.ashok.bible.ui.bibleindex.BibleIndexActivity
import com.lakki.kotlinlearning.view.base.RecyclerBaseAdapter


class BibleIndexAdapter constructor(
    var mContext: BibleIndexActivity?,
    var list: ArrayList<BibleIndexModelEntry>,
    var originalList: ArrayList<BibleIndexModelEntry> = list
) :
    RecyclerBaseAdapter<BibleIndexModelEntry?>(), Filterable {

    companion object {
        const val SECTION_VIEW = 0
        const val CONTENT_VIEW = 1
    }

    override fun getDataAtPosition(position: Int): BibleIndexModelEntry? {
        return list[position]
    }

    override fun getLayoutIdForType(viewType: Int): Int {
        if (viewType == SECTION_VIEW) {
            return R.layout.bible_index_row_section
        } else {
            return R.layout.bible_index_row
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(list: ArrayList<BibleIndexModelEntry>) {
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
                    val filteredList: ArrayList<BibleIndexModelEntry> = ArrayList()
                    for (obj in originalList) {
                        if (obj.chapter.toLowerCase().contains(charString.toLowerCase())) {
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
                list = filterResults.values as ArrayList<BibleIndexModelEntry>
                notifyDataSetChanged()
            }
        }

    }

    public fun onClick(view: View, obj: BibleIndexModelEntry) {
        mContext?.onclickBook(obj)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 || position == 40) {
            SECTION_VIEW
        } else {
            CONTENT_VIEW
        }
    }

}