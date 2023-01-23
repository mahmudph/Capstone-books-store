/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.myone.core.data.source.local.entity.BookEntity
import id.myone.core.data.source.local.entity.FavoriteBookEntity

@Database(
    entities = [
        BookEntity::class,
        FavoriteBookEntity::class
    ],
    version = 1,
    exportSchema = false
)

abstract class BookDatabase : RoomDatabase() {
    abstract fun booksDao(): BookDao
    abstract fun favoriteBookDao(): FavoriteBookDao
}