/**
 * Created by Mahmud on 19/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.book.domain.usecase

import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result
import id.myone.core.utils.DataMapper

class SetFavoriteBookUseCase(private val repository: Repository) {

    suspend operator fun invoke(
        bookId: String,
        bookDetail: BookDetail,
        value: Boolean,
    ): Result<String> {
        val book = DataMapper.transformFromBookDetailToBook(bookDetail, bookId)
        return if (value) repository.insertFavoriteBook(book)
        else repository.deleteFavoriteBook(bookId)
    }
}