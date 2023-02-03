/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.search_book.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.myone.capstone_books_store.search_book.domain.usecase.SearchBookUseCase
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel(private val searchBookUseCase: SearchBookUseCase) : ViewModel() {

    private val queryChannel = MutableStateFlow("")

    private val _bookList = MutableLiveData<Result<List<Book>>>()
    val getBookList = _bookList

    init {
        /**
         * register configuration for reactive search with
         * reactive kotlin flow
         */
        this.registerSearchBookList()
    }

    /**
     * get the search book list
     * @return Unit
     */
    private fun registerSearchBookList() {
        viewModelScope.launch {
            queryChannel.debounce(300)
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }
                .flatMapLatest { searchBookUseCase(it) }
                .collect {
                    _bookList.value = it
                }
        }
    }

    /**
     * this method used to update search of the book
     */
    fun searchBook(data: String) {
        queryChannel.value = data
    }
}