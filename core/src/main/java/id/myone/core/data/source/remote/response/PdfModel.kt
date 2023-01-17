package id.myone.core.data.source.remote.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class PdfModel(
    @SerializedName("Chapter 2")
    val chapter2: String,
    @SerializedName("Chapter 5")
    val chapter5: String
)