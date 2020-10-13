package com.ashok.bible.ui.adapter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.ashok.bible.R
import com.ashok.bible.data.local.entry.FavoriteModelEntry
import com.ashok.bible.data.local.entry.HighlightModelEntry
import com.ashok.bible.data.remote.model.LyricsModel
import com.ashok.bible.databinding.LyricRowBinding
import com.ashok.bible.ui.favorite.FavoriteFragment
import com.ashok.bible.ui.feedback.FeedbackActivity
import com.ashok.bible.ui.lyrics.*
import com.ashok.bible.utils.RandomColors
import com.lakki.kotlinlearning.view.base.RecyclerBaseAdapter
import java.io.Serializable


class LyricsAdapter constructor(var mContext: Fragment?, var list: List<LyricsModel>, var isSecondLan:Boolean, val emptyView: TextView, var originalList: List<LyricsModel> = list):
    RecyclerBaseAdapter<LyricsModel?>(), Filterable {

    override fun getDataAtPosition(position: Int): LyricsModel? {
        val obj = list[position];
        obj.isSecondLan = isSecondLan
        return obj
    }

    override fun getLayoutIdForType(viewType: Int): Int {
        return R.layout.lyric_row
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(list: List<LyricsModel>) {
        this.list = list
        this.originalList = list
        if (list.isEmpty()){
            emptyView.visibility = View.VISIBLE
        }else{
            emptyView.visibility = View.GONE
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val binding = holder.binding as LyricRowBinding
        val obj = list[position];
        if (obj.isSecondLan){
            binding.icon.letter = list[position].titleEn
        }else{
            binding.icon.letter = list[position].title
        }
        binding.icon.shapeColor = RandomColors().color
    }
    fun onClick(view: View, obj:LyricsModel){
        val index = list.indexOf(obj)
        if (mContext is LyricsFirstLangFragment){
            (mContext as LyricsFirstLangFragment).onClickDraggableView(obj, list, isSecondLan, index)
        }else if (mContext is LyricsSecondLangFragment){
            (mContext as LyricsSecondLangFragment).onClickDraggableView(obj, list, isSecondLan, index)
        }
        /*val intent = Intent(mContext!!.context, LyricDetailsActivity::class.java)
        val bundle = Bundle()
        //bundle.putSerializable("lyric", obj)
        bundle.putSerializable("lyrics", list as Serializable)
        bundle.putBoolean("isSecondLan", isSecondLan)
        val index = list.indexOf(obj)
        bundle.putInt("pos", index)
        intent.putExtras(bundle)
        mContext!!.startActivity(intent)*/

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                list = if (charString.isEmpty()) {
                    originalList
                } else {
                    val filteredList: ArrayList<LyricsModel> = ArrayList()
                    for (obj in originalList) {
                        if (obj.title.toLowerCase().contains(charString.toLowerCase()) || obj.titleEn.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(obj)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = list
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                list = filterResults.values as List<LyricsModel>
                notifyDataSetChanged()
            }
        }

    }
}