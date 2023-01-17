/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.favorite.domain.usecase

import id.myone.core.data.source.local.entity.FavoriteEntity
import id.myone.core.domain.entity.Book
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result

class SetFavoriteBookUseCase(private val repository: Repository) {

    suspend operator fun invoke(book: Book, value: Boolean): Result<String> {
        val isExistBook = repository.getIsInFavoriteBook(book.id)

        /// if favorite book is exist then return message book is already in favorite tbl
        if (isExistBook && value) return Result.Error("Book is already in favorite ")

        return repository.setFavoriteBook(
            FavoriteEntity(
                bookId = book.id,
                status = value
            )
        )
    }
}