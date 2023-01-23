/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.search_book.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.myone.capstone_books_store.search_book.domain.usecase.SearchBookUseCase
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*


@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel(private val searchBookUseCase: SearchBookUseCase) : ViewModel() {

    private var page = "1"
    private val queryChannel = MutableStateFlow("")
    val searchResultLiveData: LiveData<Result<List<Book>>>

    init {
        val searchResult = queryChannel.debounce(300)
            .distinctUntilChanged()
            .filter { it.isNotEmpty() }
            .flatMapLatest { searchBookUseCase(it) }

        searchResultLiveData = searchResult.asLiveData()
    }


    fun searchBook(query: String, pageIn: String = "1") {
        page = pageIn
        queryChannel.value = query
    }
}