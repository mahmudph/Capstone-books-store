package id.myone.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BookFavoriteEntity(
    @Embedded val book: BookEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "book_id"
    )
    val bookFavorite: FavoriteEntity
)
