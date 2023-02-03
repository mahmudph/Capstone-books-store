/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.search_book.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.myone.capstone_books_store.search_book.domain.usecase.SearchBookUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*


@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel(private val searchBookUseCase: SearchBookUseCase) : ViewModel() {

    private val queryChannel = MutableStateFlow("")

    val searchBookListResult = queryChannel.debounce(300)
        .distinctUntilChanged()
        .filter { it.isNotEmpty() }
        .flatMapLatest { searchBookUseCase(it) }
        .asLiveData()


    fun searchBook(data: String) {
        queryChannel.value = data
    }
}