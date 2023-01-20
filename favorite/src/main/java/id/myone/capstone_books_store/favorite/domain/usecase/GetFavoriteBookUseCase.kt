/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.favorite.domain.usecase

import id.myone.core.domain.entity.Book
import id.myone.core.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetFavoriteBookUseCase (private val repository: Repository) {

    operator fun invoke(): Flow<List<Book>> {
        return repository.getFavoriteBookList()
    }
}