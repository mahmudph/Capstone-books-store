/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.book.presentation.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.myone.capstone_books_store.book.domain.usecase.GetBookListUseCase
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.launch

class BookViewModel(private val getBookListUseCase: GetBookListUseCase) : ViewModel() {
    private val _bookList = MutableLiveData<Result<List<Book>>>()
    val bookListData: LiveData<Result<List<Book>>> = _bookList


    init {
        /**
         * get the book  list when book view is created
         */
        getBookList(true)
    }


    fun getBookList(shouldFetch: Boolean = false) {
        viewModelScope.launch {
            val result = getBookListUseCase(shouldFetch)
            result.collect { _bookList.postValue(it) }
        }
    }
}