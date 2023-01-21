/**
 * Created by Mahmud on 21/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.onboarding.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.myone.core.utils.SecureStorageApp

class OnBoardingViewModel(private val storage: SecureStorageApp): ViewModel() {

    fun setIsPassOnBoardingPage() = liveData {
        storage.setBoolValue(isPasOnBoardingPage, true)
        emit(true)
    }

    companion object {
        private const val isPasOnBoardingPage = "PASS_ON_ON_BOARDING"
    }
}