/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.domain.repository

import id.myone.core.domain.entity.Book
import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow


interface Repository {
    fun getBooksList(): Flow<Result<List<Book>>>
    fun getDetailBook(id: String): Flow<Result<BookDetail>>
    fun searchBooks(query: String, page: String): Flow<Result<List<Book>>>
    suspend fun getBookById(bookId: String): Book?


    fun getFavoriteBookList(): Flow<List<Book>>
    suspend fun insertFavoriteBook(book: Book): Result<String>
    suspend fun deleteFavoriteBook(favoriteBookId: String): Result<String>
    suspend fun getIsInFavoriteBook(bookId: String): Boolean

}