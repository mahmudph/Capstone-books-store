/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.favorite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.myone.capstone_books_store.favorite.domain.usecase.GetFavoriteBookUseCase

class FavoriteViewModel(getFavoriteBookUseCase: GetFavoriteBookUseCase) : ViewModel() {
    val favoriteBookList = getFavoriteBookUseCase().asLiveData()
}