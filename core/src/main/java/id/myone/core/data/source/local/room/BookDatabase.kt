/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import id.myone.core.data.source.local.entity.BookEntity

@Database(
    entities = [
        BookEntity::class,
    ],
    version = 1,
    exportSchema = false
)

abstract class BookDatabase : RoomDatabase() {
    abstract fun booksDao(): BookDao
}