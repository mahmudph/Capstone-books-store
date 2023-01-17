/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.local.room

import androidx.room.*
import id.myone.core.data.source.local.entity.FavoriteBookEntity
import id.myone.core.data.source.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDao {

    @Transaction
    @Query("SELECT * FROM tbl_favorite")
    fun getFavoriteBooks(): Flow<List<FavoriteBookEntity>>

    @Query("DELETE FROM tbl_favorite WHERE id=:id")
    suspend fun deleteFavoriteBook(id: Int)

    @Transaction
    @Query("SELECT * FROM tbl_favorite WHERE book_id=:bookId")
    suspend fun getIsInFavoriteBook(bookId: String): FavoriteBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavoriteBooks(bookFavoriteEntity: FavoriteEntity)
}