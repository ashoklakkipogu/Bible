package com.ashok.favorite.data.local.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import com.ashok.bible.data.local.entry.HighlightModelEntry
import com.ashok.bible.data.local.entry.NoteModelEntry
import io.reactivex.Observable

@Dao
interface HighlightDao {
    @Query("SELECT * FROM highlights")
    fun getAllHighlight(): LiveData<List<HighlightModelEntry>>

    @Query("SELECT * FROM highlights WHERE Book =:bookId")
    fun getAllHighlightByBookId(bookId: Int): LiveData<List<HighlightModelEntry>>


    @Query("SELECT * FROM highlights WHERE Book =:bookId AND Chapter =:chapterId")
    fun getAllHighlightBookIdAndChapterId(bookId: Int, chapterId: Int): LiveData<List<HighlightModelEntry>>

    @Query("SELECT * FROM highlights WHERE Book =:bookId AND Chapter =:chapterId AND Versecount =:verseId")
    fun getAllHighlightByBookIdAndChapterIdAndVerse(bookId: Int, chapterId: Int, verseId: Int): LiveData<List<HighlightModelEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHighlight(highlight: List<HighlightModelEntry>)

    @Delete
    fun deleteHighlight(highlight: HighlightModelEntry)

    @Query("DELETE FROM highlights WHERE id = :id")
    fun deleteHighlightById(id: Int)

}