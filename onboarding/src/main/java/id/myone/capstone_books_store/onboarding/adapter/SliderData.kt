package id.myone.capstone_books_store.onboarding.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class SliderData(
    val title: String,
    val description: String,
    val image: Int,
): Parcelable
