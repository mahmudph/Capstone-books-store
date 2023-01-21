/**
 * Created by Mahmud on 21/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import id.myone.core.utils.SecureStorageApp

class SplashscreenViewModel(private val secureStorageApp: SecureStorageApp) : ViewModel() {
    
    fun checkIsPassedBoarding() = liveData {
        val isPassed = secureStorageApp.getBoolValue(isPasOnBoardingPage)
        emit(isPassed)
    }

    companion object {
        private const val isPasOnBoardingPage = "PASS_ON_ON_BOARDING"
    }
}