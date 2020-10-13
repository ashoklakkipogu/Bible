package com.ashok.bible.di.builder

import com.ashok.bible.ui.MainActivity
import com.ashok.bible.ui.SplashActivity
import com.ashok.bible.ui.bibleindex.BibleIndexActivity
import com.ashok.bible.ui.feedback.FeedbackActivity
import com.ashok.bible.ui.lyrics.LyricDetailsActivity
import com.ashok.bible.ui.lyrics.LyricPostActivity
import com.ashok.bible.ui.lyrics.LyricsCommentsActivity
import com.ashok.bible.ui.notification.NotificationPostActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector()
    abstract fun bibleIndexActivity(): BibleIndexActivity

    @ContributesAndroidInjector()
    abstract fun splashActivity(): SplashActivity

    @ContributesAndroidInjector()
    abstract fun feedbackActivity(): FeedbackActivity

    @ContributesAndroidInjector()
    abstract fun notificationPostActivity(): NotificationPostActivity

    @ContributesAndroidInjector()
    abstract fun lyricDetailsActivity(): LyricDetailsActivity

    @ContributesAndroidInjector()
    abstract fun lyricsCommentsActivity(): LyricsCommentsActivity

    @ContributesAndroidInjector()
    abstract fun lyricPostActivity(): LyricPostActivity

}