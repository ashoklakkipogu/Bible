package com.ashok.bible.ui.favorite

import android.app.AlertDialog
import android.view.View
import androidx.lifecycle.Observer
import com.ashok.bible.R
import com.ashok.bible.data.local.entry.BibleIndexModelEntry
import com.ashok.bible.data.local.entry.FavoriteModelEntry
import com.ashok.bible.data.local.entry.HighlightModelEntry
import com.ashok.bible.databinding.FragmentFavoriteBinding
import com.ashok.bible.ui.MainActivity
import com.ashok.bible.ui.adapter.BibleIndexAdapter
import com.ashok.bible.ui.adapter.BibleIndexNumberAdapter
import com.ashok.bible.ui.adapter.FavoriteAdapter
import com.ashok.bible.utils.Utils
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdRequest
import com.lakki.kotlinlearning.view.base.BaseFragment
import javax.inject.Inject

class FavoriteFragment : BaseFragment<FavoriteViewModel, FragmentFavoriteBinding>() {
    var modelList: List<FavoriteModelEntry> = ArrayList()
    lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var animation: ShimmerFrameLayout


    @Inject
    lateinit var adRequest: AdRequest

    override fun getLayoutRes(): Int {
        return R.layout.fragment_favorite
    }

    override fun init() {
        binding.adView.loadAd(adRequest)
        animation = binding.loadingAnimation
        Utils.verticalRecyclerView(binding.recyclerView, activity)
        favoriteAdapter = FavoriteAdapter(this, modelList);
        binding.recyclerView.adapter = favoriteAdapter

        with(viewModel){
            animation.startShimmer()
            animation.visibility = View.VISIBLE
            getAllFav(this@FavoriteFragment)
            favData.observe(this@FavoriteFragment, Observer {
                animation.stopShimmer();
                animation.visibility = View.GONE
                if (it != null) {
                    val data = it.sortedBy {sort-> sort.createdDate }.reversed()
                    favoriteAdapter.updateData(data)
                }
            })
            deleteFavorite.observe(this@FavoriteFragment, Observer {
                animation.startShimmer()
                animation.visibility = View.VISIBLE
                getAllFav(this@FavoriteFragment)
            })
            error.observe(this@FavoriteFragment, Observer {
                animation.stopShimmer();
                animation.visibility = View.GONE
            })
        }
    }

    override fun getViewModel(): Class<FavoriteViewModel> {
        return FavoriteViewModel::class.java
    }
    fun onClickItem(list: FavoriteModelEntry) {
        (activity as MainActivity).onClickItem(list.bibleId)
    }

    fun deleteRow(entry: FavoriteModelEntry) {
        val ab: AlertDialog.Builder = AlertDialog.Builder(activity)
        ab.setTitle("Delete")
        ab.setMessage("Are you sure to delete this item?")
        ab.setPositiveButton("Yes") { _, id ->
            viewModel.deleteFavoriteById(entry.id)
        }
        ab.setNegativeButton(
            "No"
        ) { pObjDialog, id -> pObjDialog.dismiss() }
        ab.show()

    }
}