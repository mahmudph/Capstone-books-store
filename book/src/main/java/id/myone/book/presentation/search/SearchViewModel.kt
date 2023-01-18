/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.book.presentation.search

import androidx.lifecycle.*
import id.myone.book.domain.usecase.SearchBookUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*


@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(private val searchBookUseCase: SearchBookUseCase) : ViewModel() {

    private var page = "1"
    private val queryChannel = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    val searchBookResult = queryChannel.debounce(300).distinctUntilChanged().mapLatest {
        searchBookUseCase(it)
    }.asLiveData()

    fun searchBook(query: String, pageIn: String = "1") {
        page = pageIn
        queryChannel.value = query
    }
}