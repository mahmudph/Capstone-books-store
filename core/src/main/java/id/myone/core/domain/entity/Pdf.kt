/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Pdf(
    val chapter2: String,
    val chapter5: String
): Parcelable