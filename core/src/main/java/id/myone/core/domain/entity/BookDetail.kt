package id.myone.core.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookDetail(
    val authors: String,
    val desc: String,
    val error: String,
    val image: String,
    val isbn10: String,
    val isbn13: String,
    val pages: String,
    val pdf: Pdf?,
    val price: String,
    val publisher: String,
    val rating: String,
    val subtitle: String,
    val title: String,
    val url: String,
    val year: String
): Parcelable
