/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.book.domain.usecase

import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result

class GetDetailBookUseCase(private val repository: Repository) {
    suspend operator fun invoke(bookId: String):Result<BookDetail> {
        return repository.getDetailBook(bookId)
    }
}