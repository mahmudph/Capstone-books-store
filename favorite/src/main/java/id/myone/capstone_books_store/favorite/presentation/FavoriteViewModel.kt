/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.favorite.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import id.myone.capstone_books_store.favorite.domain.usecase.GetFavoriteBookUseCase
import id.myone.core.domain.entity.Book

class FavoriteViewModel(getFavoriteBookUseCase: GetFavoriteBookUseCase) : ViewModel() {
    private val _favoriteBookList = getFavoriteBookUseCase().asLiveData(viewModelScope.coroutineContext)
    val getFavoriteBookList: LiveData<List<Book>> = _favoriteBookList
}