/**
 * Created by Mahmud on 24/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.myone.core.data.source.local.entity.FavoriteBookEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteBookDao {
    @Query("SELECT * FROM tbl_favorite_book")
    fun getFavoriteBooks(): Flow<List<FavoriteBookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteBookDao: FavoriteBookEntity)

    @Query("DELETE FROM tbl_favorite_book where id=:favoriteBookId")
    suspend fun deleteFavorite(favoriteBookId: String)

    @Query("SELECT * FROM tbl_favorite_book where id=:favoriteBookId")
    suspend fun getFavoriteBookById(favoriteBookId: String): FavoriteBookEntity?
}