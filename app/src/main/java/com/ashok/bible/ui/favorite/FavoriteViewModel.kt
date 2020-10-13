package com.ashok.bible.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ashok.bible.data.local.entry.FavoriteModelEntry
import com.ashok.bible.data.local.repositary.DbRepository
import com.ashok.bible.data.remote.network.ApiError
import com.lakki.kotlinlearning.view.base.BaseViewModel
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val dbRepository: DbRepository
) :
    BaseViewModel() {

    val error: MutableLiveData<ApiError> by lazy { MutableLiveData<ApiError>() }
    val favData: MutableLiveData<List<FavoriteModelEntry>> by lazy { MutableLiveData<List<FavoriteModelEntry>>() }
    val deleteFavorite: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }

    fun getAllFav(frg: FavoriteFragment) {
        dbRepository.getAllFav(
            { data->
                data.observe(frg, Observer {
                    favData.value = it
                })
            },
            {

            }
        ).also { compositeDisposable.dispose() }
    }
    fun deleteFavoriteById(id: Int) {
        dbRepository.deleteFavorite(
            id,
            {
                deleteFavorite.value = it
            },
            {

            }
        ).also { compositeDisposable.dispose() }
    }
}
