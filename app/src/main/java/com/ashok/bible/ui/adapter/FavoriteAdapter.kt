package com.ashok.bible.ui.adapter

import android.view.View
import com.ashok.bible.R
import com.ashok.bible.data.local.entry.FavoriteModelEntry
import com.ashok.bible.data.local.entry.HighlightModelEntry
import com.ashok.bible.ui.favorite.FavoriteFragment
import com.lakki.kotlinlearning.view.base.RecyclerBaseAdapter


class FavoriteAdapter constructor(var mContext: FavoriteFragment?, var list: List<FavoriteModelEntry>, var originalList: List<FavoriteModelEntry> = list):
    RecyclerBaseAdapter<FavoriteModelEntry?>() {

    override fun getDataAtPosition(position: Int): FavoriteModelEntry? {
        return list[position]
    }

    override fun getLayoutIdForType(viewType: Int): Int {
        return R.layout.favorite_row
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(list: List<FavoriteModelEntry>) {
        this.list = list
        this.originalList = list
        notifyDataSetChanged()
    }

    fun onClick(view: View, obj:FavoriteModelEntry){
        mContext?.onClickItem(obj)
        //mContext?.onclickChapter(chaptertId)
    }

    fun onClickDelete(view:View, entry: FavoriteModelEntry){
        mContext?.deleteRow(entry)
    }

}