package com.ashok.bible.data.remote.repositary

import com.ashok.bible.data.remote.ApiService
import com.ashok.bible.data.remote.NotificationMessagingService
import com.ashok.bible.data.remote.model.BaseModel
import com.ashok.bible.data.remote.model.CarouselModel
import com.ashok.bible.data.remote.model.LyricsModel
import com.lakki.kotlinlearning.data.remote.repositary.AppRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.ashok.bible.data.remote.network.ApiDisposable
import com.ashok.bible.data.remote.network.ApiError
import com.ashok.bible.ui.model.NotificationMsgModel
import com.ashok.bible.utils.MappedFireBaseData
import okhttp3.MultipartBody
import javax.inject.Inject

class AppRepoImp @Inject constructor(
    val apiService: ApiService,
    val notificationService: NotificationMessagingService
) :
    AppRepository {
    override fun getCarousel(
        success: (List<CarouselModel>) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return apiService
            .getCarousel()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate(terminate)
            .subscribeWith(
                ApiDisposable<List<CarouselModel>>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun getLyrics(
        success: (List<LyricsModel>) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return apiService
            .getLyrics()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate(terminate)
            .subscribeWith(
                ApiDisposable<Map<String, LyricsModel>>(
                    {
                        val data = MappedFireBaseData<LyricsModel>().getFireBaseMapData(it)
                        success(data)
                    },
                    failure
                )
            )
    }

    override fun sendNotificationMsg(
        notificationMsgModel: NotificationMsgModel,
        success: (BaseModel) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return notificationService
            .sendMessage(notificationMsgModel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate(terminate)
            .subscribeWith(
                ApiDisposable<BaseModel>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }

    override fun createLyric(
        obj: LyricsModel,
        success: (BaseModel) -> Unit,
        failure: (ApiError) -> Unit,
        terminate: () -> Unit
    ): Disposable {
        return apiService
            .createLyric(obj)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate(terminate)
            .subscribeWith(
                ApiDisposable<BaseModel>(
                    {
                        success(it)
                    },
                    failure
                )
            )
    }
}