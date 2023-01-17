/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.book.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.myone.book.domain.usecase.GetBookListUseCase

class BookViewModel(private val getBookListUseCase: GetBookListUseCase) : ViewModel() {
    val bookList = getBookListUseCase().asLiveData()
}