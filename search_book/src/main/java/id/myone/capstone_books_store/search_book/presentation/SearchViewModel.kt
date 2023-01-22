/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.search_book.presentation

import androidx.lifecycle.*
import id.myone.capstone_books_store.search_book.domain.usecase.SearchBookUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*


@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(private val searchBookUseCase: SearchBookUseCase) : ViewModel() {

    private var page = "1"
    private val queryChannel = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    val searchBookResult = queryChannel.debounce(300).distinctUntilChanged().filter {
        it.isNotEmpty()
    }.mapLatest {
        searchBookUseCase(it)
    }.asLiveData()

    fun searchBook(query: String, pageIn: String = "1") {
        page = pageIn
        queryChannel.value = query
    }
}