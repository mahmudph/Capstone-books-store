package id.myone.core.data.source.remote.response


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class ListBooksResponse(
    @SerializedName("books")
    val books: List<BookModel>,
    @SerializedName("error")
    val error: String,
    @SerializedName("total")
    val total: String
)