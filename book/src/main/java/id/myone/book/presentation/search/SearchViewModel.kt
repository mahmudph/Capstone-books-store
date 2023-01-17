/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.book.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.myone.book.domain.usecase.SearchBookUseCase
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.launch

class SearchViewModel(private val searchBookUseCase: SearchBookUseCase) : ViewModel() {

    private val resultData = MutableLiveData<Result<List<Book>>>()
    val bookList: LiveData<Result<List<Book>>> = resultData

    fun searchBook(query: String, page: String = "0") {
        viewModelScope.launch {
            val result = searchBookUseCase(query, page)
            resultData.postValue(result)
        }
    }
}