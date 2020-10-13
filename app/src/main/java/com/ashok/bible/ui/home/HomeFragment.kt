package com.ashok.bible.ui.home

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashok.bible.R
import com.ashok.bible.common.AppConstants
import com.ashok.bible.data.local.entry.*
import com.ashok.bible.databinding.FragmentHomeBinding
import com.ashok.bible.ui.MainActivity
import com.ashok.bible.ui.adapter.HomeAdapter
import com.ashok.bible.utils.SharedPrefUtils
import com.ashok.bible.utils.TtsManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.firebase.analytics.FirebaseAnalytics
import com.lakki.kotlinlearning.view.base.BaseFragment
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    var bookId = 0
    var chapterId = 0
    var verseId = 0
    lateinit var adapters: HomeAdapter
    var bibleModelEntry: ArrayList<BibleModelEntry> = ArrayList()
    var bibleIndex: List<BibleIndexModelEntry> = ArrayList()
    var position: Int = 0
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var animation: ShimmerFrameLayout
    var bibleID = -1
    var tts: TtsManager? = null
    var lng: Locale = Locale.US
    @Inject
    lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var adRequest: AdRequest

    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }


    override fun init() {
        binding.adView.loadAd(adRequest)
        animation = binding.loadingAnimation
        tts = TtsManager(activity)
        val lang = SharedPrefUtils.getLanguage(pref)
        when (lang){
            AppConstants.TELUGU->{
                lng = Locale(AppConstants.TELUGU_IN)
            }
            AppConstants.TAMIL->{
                lng = Locale(AppConstants.TAMIL_IN)
            }
            AppConstants.ENGLISH->{
                lng = Locale(AppConstants.ENGLISH_IN)
            }
        }
        linearLayoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = linearLayoutManager
        adapters = HomeAdapter(this, tts, lng, pref, analytics, bibleModelEntry)
        binding.recyclerView.adapter = adapters
        arguments?.let {
            bibleID = it.getInt("BibleID", -1)

        }

        with(viewModel) {
            animation.startShimmer()
            animation.visibility = View.VISIBLE
            getBibleIndex(this@HomeFragment)
            getBible(this@HomeFragment)
            getAllFav(this@HomeFragment)
            getAllNotes(this@HomeFragment)
            getAllHighlights(this@HomeFragment)
            bibleData.observe(this@HomeFragment, Observer {
                animation.stopShimmer();
                animation.visibility = View.GONE
                if (it != null) {
                    val data = it as ArrayList<BibleModelEntry>
                    adapters.updateData(data)
                    if(bibleID!=-1){
                        linearLayoutManager.scrollToPositionWithOffset(bibleID - 1, 0)
                    }

                }
            })
            bibleIndexData.observe(this@HomeFragment, Observer {
                if (it != null) {
                    bibleIndex = it
                }
            })
            bibleDataByBookIDAndChapterIdAndVerseID.observe(this@HomeFragment, Observer {
                if (it != null && it.isNotEmpty()) {
                    position = it[0].id
                    linearLayoutManager.scrollToPositionWithOffset(position - 1, 0)
                }
            })
            favData.observe(this@HomeFragment, Observer {
                if (it != null) {
                    adapters.updateDataFavData(it)
                }
            })
            notesData.observe(this@HomeFragment, Observer {
                if (it != null) {
                    adapters.updateDataNotesData(it)
                }
            })
            highlightsData.observe(this@HomeFragment, Observer {
                if (it != null) {
                    adapters.updateDataHighlightsData(it)
                }
            })
            error.observe(this@HomeFragment, Observer {
                animation.stopShimmer();
                animation.visibility = View.GONE
            })
        }


    }

    override fun getViewModel(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    fun updateBibleIndex(chapterId: Int, bookID: Int) {
        (activity as MainActivity).updateToolBar("${bibleIndex[bookID].chapter} ${chapterId}")
    }

    fun updateValue(bookId: Int, chapterId: Int, verseId: Int) {
        this.bookId = bookId
        this.chapterId = chapterId
        this.verseId = verseId
        viewModel.getBibleByBookIdAndChapterIdAndVerseId(this, bookId, chapterId, verseId)

    }

    fun updateSearch(query: String) {
        adapters.filter.filter(query)
    }

    override fun onDestroy() {
        tts?.shutDown()
        super.onDestroy()

    }

    fun getBibleIndexName(index: Int): String {
        return bibleIndex[index].chapter
    }

    fun insertFavorites(favList: ArrayList<FavoriteModelEntry>) {
        viewModel.insertFavorites(favList)
    }

    fun insertNotes(noteList: ArrayList<NoteModelEntry>) {
        viewModel.insertNotes(noteList)
    }

    fun insertHighlight(highlights: ArrayList<HighlightModelEntry>) {
        viewModel.insertHighlights(highlights)
    }
}
