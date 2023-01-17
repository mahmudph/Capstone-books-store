/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class FavoriteBookEntity(
    @Embedded val favorite: FavoriteEntity,

    @Relation(
        parentColumn = "book_id",
        entityColumn = "id",
    )
    val book: BookEntity
)