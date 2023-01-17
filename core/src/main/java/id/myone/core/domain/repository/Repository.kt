/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.domain.repository

import id.myone.core.data.source.local.entity.FavoriteEntity
import id.myone.core.domain.entity.Book
import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow


interface Repository {
    fun getBooksList(): Flow<Result<List<Book>>>
    fun getFavoriteBookList(): Flow<List<Book>>
    suspend fun searchBooks(query: String, page: String): Result<List<Book>>
    suspend fun getDetailBook(id: String): Result<BookDetail>
    suspend fun setFavoriteBook(book: FavoriteEntity): Result<String>
    suspend fun getIsInFavoriteBook(bookId: String): Boolean
    suspend fun deleteFavoriteBook(id: Int): Result<String>
}