/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.book.domain.usecase

import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class GetDetailBookUseCase(private val repository: Repository) {
    operator fun invoke(bookId: String): Flow<Result<BookDetail>> {
        return repository.getDetailBook(bookId)
    }
}