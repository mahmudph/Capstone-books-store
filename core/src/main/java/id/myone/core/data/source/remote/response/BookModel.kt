package id.myone.core.data.source.remote.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BookModel(
    @SerializedName("image")
    val image: String,
    @SerializedName("isbn13")
    val isbn13: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)