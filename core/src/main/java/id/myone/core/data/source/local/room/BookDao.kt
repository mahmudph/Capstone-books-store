/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.local.room

import androidx.room.*
import id.myone.core.data.source.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BookDao {

    @Query("SELECT * FROM tbl_books")
    fun getBooks(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bulkInsert(listBookEntity: List<BookEntity>)

    @Query("SELECT * FROM tbl_books WHERE id=:id")
    suspend fun getBookById(id: String): BookEntity?


}