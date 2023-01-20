/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.local

import id.myone.core.data.source.local.entity.BookEntity
import id.myone.core.data.source.local.room.BookDao
import kotlinx.coroutines.flow.Flow


class LocalDataSource(
    private val bookDao: BookDao,
) {

    /// books
    fun getBooks() = bookDao.getBooks()
    suspend fun getBookById(bookId: String) = bookDao.getBookById(bookId)

    suspend fun updateBook(book: BookEntity) = bookDao.updateBook(book)

    suspend fun insertBook(book: BookEntity) = bookDao.insertBook(book)
    suspend fun bulkInsertBook(listBook: List<BookEntity>) = bookDao.bulkInsert(listBook)

    /// favorite
    fun getFavoriteBooks(): Flow<List<BookEntity>> = bookDao.getFavoriteBooks()


}