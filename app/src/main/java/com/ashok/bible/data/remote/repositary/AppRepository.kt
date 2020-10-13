package com.lakki.kotlinlearning.data.remote.repositary

import com.ashok.bible.data.remote.model.BaseModel
import com.ashok.bible.data.remote.model.CarouselModel
import com.ashok.bible.data.remote.model.LyricsModel
import io.reactivex.disposables.Disposable
import com.ashok.bible.data.remote.network.ApiError
import com.ashok.bible.ui.model.NotificationMsgModel
import okhttp3.MultipartBody

interface AppRepository {
    fun getCarousel(
        success: (List<CarouselModel>) -> Unit,
        failure: (ApiError) -> Unit = {},
        terminate: () -> Unit = {}
    ): Disposable

    fun getLyrics(
        success: (List<LyricsModel>) -> Unit,
        failure: (ApiError) -> Unit = {},
        terminate: () -> Unit = {}
    ): Disposable

    fun sendNotificationMsg(
        notificationMsgModel: NotificationMsgModel,
        success: (BaseModel) -> Unit,
        failure: (ApiError) -> Unit = {},
        terminate: () -> Unit = {}
    ): Disposable
    fun createLyric(
        obj: LyricsModel,
        success: (BaseModel) -> Unit,
        failure: (ApiError) -> Unit = {},
        terminate: () -> Unit = {}
    ): Disposable

}