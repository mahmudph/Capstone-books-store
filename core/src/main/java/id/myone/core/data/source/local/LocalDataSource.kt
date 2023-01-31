/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.local

import id.myone.core.data.source.local.entity.BookEntity
import id.myone.core.data.source.local.entity.FavoriteBookEntity
import id.myone.core.data.source.local.room.BookDao
import id.myone.core.data.source.local.room.FavoriteBookDao
import kotlinx.coroutines.flow.Flow


class LocalDataSource(
    private val bookDao: BookDao,
    private val favoriteBook: FavoriteBookDao
) {

    /// books
    fun getBooks() = bookDao.getBooks()
    suspend fun getBookById(bookId: String) = bookDao.getBookById(bookId)
    suspend fun bulkInsertBook(listBook: List<BookEntity>) = bookDao.bulkInsert(listBook)


    /// favorite
    fun getFavoriteBooks(): Flow<List<FavoriteBookEntity>> = favoriteBook.getFavoriteBooks()
    suspend fun insertFavoriteBook(favoriteBookEntity: FavoriteBookEntity) =
        favoriteBook.insertFavorite(
            favoriteBookEntity
        )

    suspend fun deleteFavoriteBook(favoriteBookId: String) =
        favoriteBook.deleteFavorite(favoriteBookId)

    suspend fun getFavoriteBookById(favoriteBookId: String) =
        favoriteBook.getFavoriteBookById(favoriteBookId)
}