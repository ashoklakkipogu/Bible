package com.ashok.bible.ui.home

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ashok.bible.data.local.entry.*
import com.ashok.bible.data.local.repositary.DbRepository
import com.ashok.bible.data.remote.model.CarouselModel
import com.ashok.bible.data.remote.network.ApiError
import com.ashok.bible.ui.MainActivity
import com.ashok.bible.ui.bibleindex.BibleIndexActivity
import com.ashok.bible.ui.favorite.FavoriteFragment
import com.ashok.bible.ui.highlights.HighlightsFragment
import com.ashok.bible.ui.notes.NotesFragment
import com.lakki.kotlinlearning.data.remote.repositary.AppRepository
import com.lakki.kotlinlearning.view.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val dbRepository: DbRepository
) : BaseViewModel() {

    val error: MutableLiveData<ApiError> by lazy { MutableLiveData<ApiError>() }
    val bibleData: MutableLiveData<List<BibleModelEntry>> by lazy { MutableLiveData<List<BibleModelEntry>>() }
    val bibleIndexData: MutableLiveData<List<BibleIndexModelEntry>> by lazy { MutableLiveData<List<BibleIndexModelEntry>>() }
    val bibleDataByBookIDAndChapterIdAndVerseID: MutableLiveData<List<BibleModelEntry>> by lazy { MutableLiveData<List<BibleModelEntry>>() }
    val insertFav: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    val insertNotes: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    val favData: MutableLiveData<List<FavoriteModelEntry>> by lazy { MutableLiveData<List<FavoriteModelEntry>>() }
    val notesData: MutableLiveData<List<NoteModelEntry>> by lazy { MutableLiveData<List<NoteModelEntry>>() }
    val highlightsData: MutableLiveData<List<HighlightModelEntry>> by lazy { MutableLiveData<List<HighlightModelEntry>>() }
    val insertHighlight: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    fun getBibleIndex(activity: HomeFragment) {
        dbRepository.getBibleIndex(
            { data ->
                data.observe(activity, Observer {
                    bibleIndexData.value = it
                })
            },
            {
                error.value = it
            }
        ).also { compositeDisposable.dispose() }
    }

    fun getBible(activity: HomeFragment) {
        dbRepository.getBible(
            { data ->
                data.observe(activity, Observer {
                    bibleData.value = it
                })
            },
            {
                error.value = it
            }
        ).also { compositeDisposable.dispose() }
    }

    fun getBibleByBookIdAndChapterIdAndVerseId(
        activity: HomeFragment,
        bookId: Int,
        chapterID: Int,
        verseId: Int
    ) {
        dbRepository.getBibleByBookIdAndChapterIdAndVerse(
            bookId,
            chapterID,
            verseId,
            { data ->
                data.observe(activity, Observer {
                    bibleDataByBookIDAndChapterIdAndVerseID.value = it
                })
            },
            {
                error.value = it
            }
        ).also { compositeDisposable.dispose() }
    }

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

    fun getAllFav(frg: HomeFragment) {
        dbRepository.getAllFav(
            { data ->
                data.observe(frg, Observer {
                    favData.value = it
                })
            },
            {

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

    fun getAllNotes(frg: HomeFragment) {
        dbRepository.getAllNotes(
            { data ->
                data.observe(frg, Observer {
                    notesData.value = it
                })
            },
            {

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

    fun getAllHighlights(frg: HomeFragment) {
        dbRepository.getAllHighlights(
            { data ->
                data.observe(frg, Observer {
                    highlightsData.value = it
                })
            },
            {

            }
        ).also { compositeDisposable.dispose() }
    }
}