/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.book.presentation.detailBook

import androidx.lifecycle.*
import id.myone.book.domain.usecase.GetDetailBookUseCase

class DetailBookViewModel(private val getDetailBookUseCase: GetDetailBookUseCase) : ViewModel() {

    fun loadBookDetail(bookId: String) = getDetailBookUseCase(bookId).asLiveData()
}