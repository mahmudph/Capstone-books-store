/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.search_book.domain.usecase

import id.myone.core.domain.entity.Book
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result

class SearchBookUseCase(private val repository: Repository) {

    suspend operator fun invoke(query: String, page: String = "1"): Result<List<Book>> {
        return repository.searchBooks(query, page)
    }
}