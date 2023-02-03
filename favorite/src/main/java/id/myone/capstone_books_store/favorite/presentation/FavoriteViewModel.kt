/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.favorite.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.myone.capstone_books_store.favorite.domain.usecase.GetFavoriteBookUseCase
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.launch

class FavoriteViewModel(private val getFavoriteBookUseCase: GetFavoriteBookUseCase) : ViewModel() {
    private val _favoriteBookList = MutableLiveData<Result<List<Book>>>()
    val getFavoriteBookList: LiveData<Result<List<Book>>> = _favoriteBookList

    init {
        /**
         * load favorite book only at the first initialize
         */
        this.loadFavoriteBookList()
    }

    /**
     * load favorite book
     */
    private fun loadFavoriteBookList() {
        viewModelScope.launch {
            _favoriteBookList.value = Result.Loading()
            val favoriteBookList = getFavoriteBookUseCase()
            favoriteBookList.collect {
                _favoriteBookList.value = Result.Success(it)
            }
        }
    }
}