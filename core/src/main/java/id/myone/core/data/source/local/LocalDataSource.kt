/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.local

import id.myone.core.data.source.local.entity.BookEntity
import id.myone.core.data.source.local.entity.FavoriteBookEntity
import id.myone.core.data.source.local.entity.FavoriteEntity
import id.myone.core.data.source.local.room.BookDao
import id.myone.core.data.source.local.room.FavoriteDao
import kotlinx.coroutines.flow.Flow


class LocalDataSource(
    private val bookDao: BookDao,
    private val favoriteBookDao: FavoriteDao
) {
    suspend fun getFavoriteBookById(bookId: String) = favoriteBookDao.getIsInFavoriteBook(bookId)
    fun getFavoriteBooks(): Flow<List<FavoriteBookEntity>> = favoriteBookDao.getFavoriteBooks()
    suspend fun setFavoriteBook(book: FavoriteEntity) = favoriteBookDao.addToFavoriteBooks(book)

    suspend fun deleteFavoriteBook(id: Int) = favoriteBookDao.deleteFavoriteBook(id)
    suspend fun bulkInsertBook(listBook: List<BookEntity>) = bookDao.bulkInsert(listBook)
    fun getBooks() = bookDao.getBooks()
}