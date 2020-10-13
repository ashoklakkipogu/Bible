package com.ashok.bible.ui.lyrics

import com.ashok.bible.R
import com.ashok.bible.data.remote.model.LyricsModel
import com.ashok.bible.databinding.FragmentLyricsFirstLanBinding
import com.ashok.bible.ui.adapter.LyricsAdapter
import com.ashok.bible.utils.Utils
import com.lakki.kotlinlearning.view.base.BaseFragment


class LyricsFirstLangFragment : BaseFragment<LyricsViewModel, FragmentLyricsFirstLanBinding>() {

    lateinit var adapter: LyricsAdapter

    override fun getLayoutRes(): Int {
        return R.layout.fragment_lyrics_first_lan
    }

    override fun init() {
        binding.handlers = this
        val bundle = arguments
        val lyrics = bundle?.getSerializable("lyrics") as List<LyricsModel>
        val isSecondLan = bundle?.getBoolean("isSecondLan")
        Utils.verticalRecyclerView(binding.recyclerView, activity)
        adapter = LyricsAdapter(this, lyrics, isSecondLan, binding.emptyView)
        binding.recyclerView.adapter = adapter

    }


    override fun getViewModel(): Class<LyricsViewModel> {
        return LyricsViewModel::class.java
    }

    fun updateSearch(query: String) {
        adapter.filter.filter(query)
    }
    fun onClickDraggableView(obj:LyricsModel, list:List<LyricsModel>, isSecondLan:Boolean, index:Int){
        val parentFrag: LyricsFragment = this.parentFragment as LyricsFragment
        parentFrag.onClickDraggableView(obj, list, isSecondLan, index)
    }
}
