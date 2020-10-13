package com.ashok.bible.di.module

import android.app.Application
import androidx.room.Room
import com.ashok.bible.data.local.BibleDatabase
import com.ashok.bible.data.local.dao.BibleDao
import com.ashok.bible.data.local.dao.BibleIndexDao
import com.ashok.bible.data.local.repositary.DbRepoImp
import com.ashok.bible.data.local.repositary.DbRepository
import com.ashok.favorite.data.local.dao.FavoriteDao
import com.ashok.favorite.data.local.dao.HighlightDao
import com.ashok.favorite.data.local.dao.NoteDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomDataBase(application:Application): BibleDatabase=
        Room.databaseBuilder(application, BibleDatabase::class.java, BibleDatabase.DATABASE).build()

    @Provides
    @Singleton
    fun provideNotificationDao(bibleDatabase: BibleDatabase):BibleDao =
        bibleDatabase.bibleDao()

    @Provides
    @Singleton
    fun provideBibleIndexDao(bibleDatabase: BibleDatabase):BibleIndexDao =
        bibleDatabase.bibleIndexDao()

    @Provides
    @Singleton
    fun provideFavoriteDao(bibleDatabase: BibleDatabase):FavoriteDao =
        bibleDatabase.favoriteDao()

    @Provides
    @Singleton
    fun provideNoteDao(bibleDatabase: BibleDatabase):NoteDao =
        bibleDatabase.noteDao()

    @Provides
    @Singleton
    fun provideHighlightDao(bibleDatabase: BibleDatabase):HighlightDao =
        bibleDatabase.highlightDao()


    @Provides
    fun provideDbRepository(bibleDao: BibleDao, bibleIndexDao: BibleIndexDao, favoriteDao: FavoriteDao, noteDao: NoteDao, highlightDao: HighlightDao): DbRepository {
        return DbRepoImp(bibleDao, bibleIndexDao, favoriteDao, noteDao, highlightDao)
    }


}