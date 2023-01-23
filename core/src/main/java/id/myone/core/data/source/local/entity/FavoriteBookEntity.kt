package id.myone.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tbl_favorite_book")
data class FavoriteBookEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subtitle") val subtitle: String,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "url") val url: String,
)
