package com.ashok.bible.data.remote

import io.reactivex.Observable
import retrofit2.http.*
import java.util.*
import retrofit2.http.POST
import android.R.attr.data
import com.ashok.bible.data.remote.model.BaseModel
import com.ashok.bible.data.remote.model.CarouselModel
import com.ashok.bible.data.remote.model.LyricsModel
import com.ashok.bible.ui.model.NotificationMsgModel
import okhttp3.MultipartBody


interface ApiService {
    @GET("carousel")
    fun getCarousel(): Observable<List<CarouselModel>>

    @GET("lyrics.json")
    fun getLyrics(): Observable<Map<String, LyricsModel>>

    @POST("lyrics.json")
    fun createLyric(@Body lyricsModel: LyricsModel): Observable<BaseModel>
}