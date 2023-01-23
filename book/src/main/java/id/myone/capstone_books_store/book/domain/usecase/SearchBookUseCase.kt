/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.book.domain.usecase

import id.myone.core.domain.entity.Book
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow

class SearchBookUseCase(private val repository: Repository) {

    operator fun invoke(query: String, page: String = "1"): Flow<Result<List<Book>>> {
        return repository.searchBooks(query, page)
    }
}