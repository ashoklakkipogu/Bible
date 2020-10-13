package com.ashok.bible.data.local.repositary

import android.util.Log
import androidx.lifecycle.LiveData
import com.ashok.bible.data.local.dao.BibleDao
import com.ashok.bible.data.local.dao.BibleIndexDao
import com.ashok.bible.data.local.entry.*
import com.ashok.bible.data.remote.network.ApiDisposable
import com.ashok.bible.data.remote.network.ApiError
import com.ashok.favorite.data.local.dao.FavoriteDao
import com.ashok.favorite.data.local.dao.HighlightDao
import com.ashok.favorite.data.local.dao.NoteDao
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DbRepoImp @Inject constructor(
    private val bibleDao: BibleDao,
    private val bibleIndexDao: BibleIndexDao,
    private val favoriteDao: FavoriteDao,
    private val noteDao: NoteDao,
    private val highlightDao: HighlightDao
) : DbRepository {
    override fun getBible(
        success: (LiveData<List<BibleModelEntry>>) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { bibleDao.getAllBibleContent() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<LiveData<List<BibleModelEntry>>>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun getBibleIndex(
        success: (LiveData<List<BibleIndexModelEntry>>) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { bibleIndexDao.getAllBibleIndexContent() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<LiveData<List<BibleIndexModelEntry>>>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun getBibleByBookId(
        id: Int,
        success: (LiveData<List<BibleModelEntry>>) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { bibleDao.getAllBibleContentByBookId(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<LiveData<List<BibleModelEntry>>>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun getBibleByBookIdAndChapterId(
        bookId: Int,
        chapterID: Int,
        success: (LiveData<List<BibleModelEntry>>) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { bibleDao.getAllBibleContentByBookIdAndChapterId(bookId, chapterID) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<LiveData<List<BibleModelEntry>>>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun getBibleByBookIdAndChapterIdAndVerse(
        bookId: Int,
        chapterID: Int,
        verseId: Int,
        success: (LiveData<List<BibleModelEntry>>) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable {
                bibleDao.getAllBibleContentByBookIdAndChapterIdAndVerse(
                    bookId,
                    chapterID,
                    verseId
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<LiveData<List<BibleModelEntry>>>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun getAllFav(
        success: (LiveData<List<FavoriteModelEntry>>) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { favoriteDao.getAllFavorites() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<LiveData<List<FavoriteModelEntry>>>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun getAllHighlights(
        success: (LiveData<List<HighlightModelEntry>>) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { highlightDao.getAllHighlight() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<LiveData<List<HighlightModelEntry>>>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun getAllNotes(
        success: (LiveData<List<NoteModelEntry>>) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { noteDao.getAllNote() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<LiveData<List<NoteModelEntry>>>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun insertAllFav(
        favList: ArrayList<FavoriteModelEntry>,
        success: (Unit) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { favoriteDao.insertFavorites(favList) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<Unit>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun insertAllNotes(
        noteList: ArrayList<NoteModelEntry>,
        success: (Unit) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { noteDao.insertNote(noteList) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<Unit>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun insertAllHighlight(
        highlights: ArrayList<HighlightModelEntry>,
        success: (Unit) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { highlightDao.insertHighlight(highlights) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<Unit>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun deleteHighlight(
        id: Int,
        success: (Unit) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { highlightDao.deleteHighlightById(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<Unit>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun deleteNote(
        id: Int,
        success: (Unit) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { noteDao.deleteNoteById(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<Unit>(
                    {
                        success(it)
                    },
                    failure
                )
            )    }

    override fun deleteFavorite(
        id: Int,
        success: (Unit) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return Observable
            .fromCallable { favoriteDao.deleteFavoriteById(id) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(
                ApiDisposable<Unit>(
                    {
                        success(it)
                    },
                    failure
                )
            )    }
}