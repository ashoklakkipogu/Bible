package com.ashok.bible.ui

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ashok.bible.data.local.entry.*
import com.ashok.bible.data.local.repositary.DbRepository
import com.ashok.bible.data.remote.model.CarouselModel
import com.ashok.bible.data.remote.network.ApiError
import com.ashok.bible.ui.home.HomeFragment
import com.ashok.bible.utils.DialogBuilder
import com.ashok.bible.utils.DialogListenerForLanguage
import com.ashok.bible.utils.SharedPrefUtils
import com.ashok.bible.utils.Utils
import com.ashok.favorite.data.local.dao.FavoriteDao
import com.ashok.favorite.data.local.dao.HighlightDao
import com.ashok.favorite.data.local.dao.NoteDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lakki.kotlinlearning.data.remote.repositary.AppRepository
import com.lakki.kotlinlearning.view.base.BaseViewModel
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val dbRepository: DbRepository
) :
    BaseViewModel() {

    @Inject
    lateinit var highlightDao: HighlightDao
    @Inject
    lateinit var noteDao: NoteDao
    @Inject
    lateinit var favoriteDao: FavoriteDao

    val error: MutableLiveData<ApiError> by lazy { MutableLiveData<ApiError>() }
    // val bibleIndexData: MutableLiveData<List<BibleIndexModelEntry>> by lazy { MutableLiveData<List<BibleIndexModelEntry>>() }
    val insertFav: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    val insertNotes: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    val insertHighlight: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    fun insertFavorites(favList: ArrayList<FavoriteModelEntry>) {
        dbRepository.insertAllFav(
            favList,
            {
                insertFav.value = it
            },
            {
                error.value = it
            }
        ).also { compositeDisposable.dispose() }
    }


    fun insertNotes(noteList: ArrayList<NoteModelEntry>) {
        dbRepository.insertAllNotes(
            noteList,
            {
                insertNotes.value = it
            },
            {
                error.value = it
            }
        ).also { compositeDisposable.dispose() }
    }


    fun insertHighlights(highlight: ArrayList<HighlightModelEntry>) {
        dbRepository.insertAllHighlight(
            highlight,
            {
                insertHighlight.value = it
            },
            {
                error.value = it
            }
        ).also { compositeDisposable.dispose() }
    }

    fun languageChange(activity:MainActivity, pref:SharedPreferences){
        DialogBuilder.showLanguage(
            activity,
            object : DialogListenerForLanguage {
                override fun language(selectedLan: String) {
                    val lan  = SharedPrefUtils.getLanguage(pref)
                    lan?.let {
                        if (selectedLan !=lan ){
                            val ab: AlertDialog.Builder = AlertDialog.Builder(activity)
                            ab.setTitle("Change language")
                            ab.setMessage("Are you sure you want to change the language?")
                            ab.setPositiveButton("Yes") { _, id ->
                                highlightDao.getAllHighlight().observe(activity, Observer {highlighList->
                                    if(highlighList.isNotEmpty()){
                                        SharedPrefUtils.saveHighLights(pref, Gson().toJson(highlighList))
                                    }
                                    favoriteDao.getAllFavorites().observe(activity, Observer {favoriteList ->
                                        if(favoriteList.isNotEmpty()){
                                            SharedPrefUtils.saveFavorite(pref, Gson().toJson(favoriteList))
                                        }
                                        noteDao.getAllNote().observe(activity, Observer {noteList ->
                                            if(noteList.isNotEmpty()){
                                                SharedPrefUtils.saveNote(pref, Gson().toJson(noteList))
                                            }
                                            Utils.deleteDb(activity!!, selectedLan, pref)
                                        })
                                    })

                                })
                            }
                            ab.setNegativeButton(
                                "No"
                            ) { pObjDialog, id -> pObjDialog.dismiss() }
                            ab.show()


                        }
                    }
                }

            })
    }
}
