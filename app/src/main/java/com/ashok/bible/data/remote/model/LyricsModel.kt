package com.ashok.bible.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LyricsModel:BaseModel(), Serializable {
    @SerializedName("title") var title: String = ""
    @SerializedName("desc") var desc: String = ""
    @SerializedName("youtubeId") var youtubeId: String = ""
    @SerializedName("language") var language: String = ""
    @SerializedName("content") var content: String = ""
    @SerializedName("createdDate") var createdDate: String = ""
    @SerializedName("singers") var singers: String = ""
    @SerializedName("content_en") var contentEn: String = ""
    @SerializedName("title_en") var titleEn: String = ""
    var isSecondLan: Boolean = false
}