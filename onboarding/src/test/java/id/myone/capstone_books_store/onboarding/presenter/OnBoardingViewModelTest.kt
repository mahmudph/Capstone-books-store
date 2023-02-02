package id.myone.capstone_books_store.onboarding.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.myone.core.utils.SecureStorageApp
import id.myone.test_utility.utility.MainDispatcherRule
import id.myone.test_utility.utility.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class OnBoardingViewModelTest {

    @Mock lateinit var storageApp: SecureStorageApp
    private lateinit var onBoardingViewModel: OnBoardingViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        onBoardingViewModel = OnBoardingViewModel(storageApp)
    }

    private val onBoardingKey = "PASS_ON_ON_BOARDING"
    private val onBoardingValue = true

    @Test
    fun `should set passed on boarding page and set to the local storage successfully`() = runTest {
        // arrange
        `when`(storageApp.setBoolValue(onBoardingKey, onBoardingValue)).thenAnswer { }
        // act
        val data = onBoardingViewModel.setIsPassOnBoardingPage()

        data.observeForTesting {
            // verify
            verify(storageApp).setBoolValue(onBoardingKey, onBoardingValue)

            // assert
            Assert.assertNotNull(data.value)
            Assert.assertTrue(data.value!!)
        }
    }
}