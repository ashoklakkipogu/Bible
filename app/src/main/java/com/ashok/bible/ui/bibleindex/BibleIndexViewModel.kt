package com.ashok.bible.ui.bibleindex

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ashok.bible.data.local.entry.BibleIndexModelEntry
import com.ashok.bible.data.local.entry.BibleModelEntry
import com.ashok.bible.data.local.repositary.DbRepository
import com.ashok.bible.data.remote.model.CarouselModel
import com.ashok.bible.data.remote.network.ApiError
import com.ashok.bible.ui.home.HomeFragment
import com.ashok.bible.ui.model.BibleNumberIndexModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lakki.kotlinlearning.data.remote.repositary.AppRepository
import com.lakki.kotlinlearning.view.base.BaseViewModel
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class BibleIndexViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val dbRepository: DbRepository
) :
    BaseViewModel() {

    val error: MutableLiveData<ApiError> by lazy { MutableLiveData<ApiError>() }
    val bibleIndexData: MutableLiveData<List<BibleIndexModelEntry>> by lazy { MutableLiveData<List<BibleIndexModelEntry>>() }
    val bibleChapters: MutableLiveData<List<Int>> by lazy { MutableLiveData<List<Int>>() }
    val bibleVerseCount: MutableLiveData<List<Int>> by lazy { MutableLiveData<List<Int>>() }
    fun getBibleIndex(activity: BibleIndexActivity) {
        dbRepository.getBibleIndex(
            { data->
                data.observe(activity, Observer {
                    bibleIndexData.value = it
                })
            },
            {

            }
        ).also { compositeDisposable.dispose() }
    }
    fun getBibleByBookId(activity: BibleIndexActivity, bookId:Int) {
        dbRepository.getBibleByBookId(
            bookId,
            { data->
                data.observe(activity, Observer {
                    val set = mutableSetOf<Int>()
                    for (obj in it){
                        set.add(obj.Chapter)
                    }
                    bibleChapters.value = set.toMutableList()

                })
            },
            {

            }
        ).also { compositeDisposable.dispose() }
    }
    fun getBibleByBookIdAndChapterId(activity: BibleIndexActivity, bookId:Int,chapterID:Int) {
        dbRepository.getBibleByBookIdAndChapterId(
            bookId,
            chapterID,
            { data->
                data.observe(activity, Observer {
                    val set = mutableSetOf<Int>()
                    for (obj in it){
                        set.add(obj.Versecount)
                    }
                    bibleVerseCount.value = set.toList()
                })
            },
            {

            }
        ).also { compositeDisposable.dispose() }
    }
}
