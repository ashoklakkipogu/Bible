package com.ashok.bible.ui.lyrics

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.ashok.bible.R
import com.ashok.bible.common.AppConstants
import com.ashok.bible.data.remote.model.LyricsModel
import com.ashok.bible.databinding.FragmentLyricsBinding
import com.ashok.bible.ui.MainActivity
import com.ashok.bible.ui.One
import com.ashok.bible.ui.Two
import com.ashok.bible.ui.adapter.ViewPagerAdapter
import com.ashok.bible.utils.DialogBuilder
import com.ashok.bible.utils.DialogListenerForLyricFilter
import com.github.pedrovgs.DraggableListener
import com.github.pedrovgs.DraggablePanel
import com.lakki.kotlinlearning.view.base.BaseFragment
import java.io.Serializable


class LyricsFragment : BaseFragment<LyricsViewModel, FragmentLyricsBinding>() {

    var modelList: List<LyricsModel> = ArrayList()
    var fragment1 = LyricsFirstLangFragment()
    var fragment2 = LyricsSecondLangFragment()
    var filterValue = AppConstants.FILTER_ALL
    lateinit var draggablePanel: DraggablePanel
    private var videoFragment = One()
    private var lyricFragment = Two()
    public var isOpenDraggablePanel = false
    private lateinit var mainActivity: MainActivity
    override fun getLayoutRes(): Int {
        return R.layout.fragment_lyrics
    }

    override fun init() {
        binding.handlers = this
        mainActivity = (activity as MainActivity)

        val tab = binding.tabLayout
        val viewPager = binding.viewPager
        draggablePanel = binding.draggablePanel

        with(viewModel) {
            showDialog()
            binding.emptyView.visibility=View.VISIBLE
            getLyrics()
            lyricData.observe(this@LyricsFragment, Observer {
                dismissDialog()
                binding.emptyView.visibility=View.GONE
                if (it != null) {
                    val data = it.sortedBy { sort -> sort.title }
                    modelList = data
                    setupViewPager(viewPager, data)
                    tab.setupWithViewPager(viewPager)
                }
            })
            error.observe(this@LyricsFragment, Observer {
                dismissDialog()
                binding.emptyView.visibility=View.GONE
                if (it != null) {
                    showSnackbar(it.getErrorMessage())
                }
            })
        }

        draggableView()

    }

    private fun setupViewPager(viewPager: ViewPager, lyricsModel: List<LyricsModel>) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        val bundle1 = Bundle()
        bundle1.putSerializable("lyrics", lyricsModel as Serializable)
        bundle1.putBoolean("isSecondLan", false)
        fragment1.arguments = bundle1
        val bundle2 = Bundle()

        bundle2.putSerializable("lyrics", lyricsModel as Serializable)
        bundle2.putBoolean("isSecondLan", true)
        fragment2.arguments = bundle2
        adapter.addFragment(fragment1, "All LANGUAGES")
        adapter.addFragment(fragment2, "ENGLISH")
        //fragment_lyrics_first_lan
        viewPager.adapter = adapter
    }

    override fun getViewModel(): Class<LyricsViewModel> {
        return LyricsViewModel::class.java
    }

    fun updateSearch(query: String) {
        fragment1.updateSearch(query)
        fragment2.updateSearch(query)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.fab -> {
                DialogBuilder.showFilter(
                    activity!!,
                    filterValue,
                    object : DialogListenerForLyricFilter {
                        override fun language(filterValue: String) {
                            this@LyricsFragment.filterValue = filterValue
                            if (filterValue != AppConstants.FILTER_ALL) {
                                if (modelList.isNotEmpty()) {
                                    val filteredMap = modelList.filter {
                                        it.language == filterValue
                                    }
                                    fragment1.adapter.updateData(filteredMap)
                                    fragment2.adapter.updateData(filteredMap)
                                    binding.tabLayout.getTabAt(0)?.text = filterValue

                                }
                            } else {
                                fragment1.adapter.updateData(modelList)
                                fragment2.adapter.updateData(modelList)
                                binding.tabLayout.getTabAt(0)?.text = "All LANGUAGES"

                            }

                            //showSnackbar(filterValue)
                        }
                    }
                )
            }
        }
    }

    private fun draggableView() {
        draggablePanel.setFragmentManager(activity?.supportFragmentManager)
        draggablePanel.setTopFragment(videoFragment)
        draggablePanel.setBottomFragment(lyricFragment)
        //val height = (200 / Resources.getSystem().getDisplayMetrics().density).toInt();

        //draggablePanel.setTopViewHeight(600)
        draggablePanel.isClickToMaximizeEnabled = true;
        val handler = Handler()
        handler.postDelayed({ draggablePanel.closeToLeft() }, 100)
        draggablePanel.setDraggableListener(object : DraggableListener {
            override fun onMaximized() {
                closeSearch()
                mainActivity.actionSearch?.isVisible = false
            }

            override fun onMinimized() {
                closeSearch()
                mainActivity.actionSearch?.isVisible = true
            }

            override fun onClosedToLeft() {
                closeSearch()
                mainActivity.actionSearch?.isVisible = true
                isOpenDraggablePanel = false
                videoFragment?.pauseVideo()
            }

            override fun onClosedToRight() {
                closeSearch()
                mainActivity.actionSearch?.isVisible = true
                isOpenDraggablePanel = false
                videoFragment?.pauseVideo()
            }

            fun onTouchListener() {

            }

            fun clickedToMinimize() {}
            fun smoothSlide() {}
            fun clickedToMaximize() {}
        })
        draggablePanel.initializeView()

    }

    fun onClickDraggableView(
        obj: LyricsModel,
        list: List<LyricsModel>,
        isSecondLan: Boolean,
        index: Int
    ) {
        closeSearch()
        mainActivity.actionSearch?.isVisible = false
        isOpenDraggablePanel = true
        videoFragment?.setVideo(obj.youtubeId)
        lyricFragment?.setLyric(this, list, isSecondLan, index)
        draggablePanel.maximize()

    }

    private fun closeSearch() {
        if (mainActivity.searchView.isSearchOpen)
            mainActivity.searchView.closeSearch()
    }

    fun onBackPressed() {
        if (isOpenDraggablePanel) {
            (activity as MainActivity).actionSearch?.isVisible = true
            draggablePanel.closeToLeft()
        }
    }
}
