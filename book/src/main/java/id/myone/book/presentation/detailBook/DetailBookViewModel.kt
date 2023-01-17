/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.book.presentation.detailBook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.myone.book.domain.usecase.GetDetailBookUseCase
import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.launch

class DetailBookViewModel(private val getDetailBookUseCase: GetDetailBookUseCase): ViewModel() {
    private val _bookDetail = MutableLiveData<Result<BookDetail>>()
    val bookDetail: LiveData<Result<BookDetail>> = _bookDetail

    fun loadBookDetail(bookId: String) {
        viewModelScope.launch {
            val result = getDetailBookUseCase(bookId)
            _bookDetail.postValue(result)
        }
    }
}