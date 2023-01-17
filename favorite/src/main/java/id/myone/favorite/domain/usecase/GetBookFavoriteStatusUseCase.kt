/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.favorite.domain.usecase

import id.myone.core.domain.repository.Repository

class GetBookFavoriteStatusUseCase(private val repository: Repository) {
    suspend operator fun invoke(bookId: String): Boolean {
        return repository.getIsInFavoriteBook(bookId)
    }
}