package id.myone.capstone_books_store.search_book.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.myone.capstone_books_store.search_book.domain.usecase.SearchBookUseCase
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import id.myone.test_utility.utility.MainDispatcherRule
import id.myone.test_utility.utility.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var searchBookUseCase: SearchBookUseCase

    private lateinit var searchViewModel: SearchViewModel

    private val searchQuery = "java for absolutely beginner"

    private val books = listOf(
        Book(
            id = "10",
            title = "java absolutely beginner",
            subtitle = "subtitle",
            price = "$20",
            image = "http://test.image/abc.png",
            url = "http://test.image/book"
        )
    )

    @Test
    fun `should success to search and return book list related with query`() = runTest {

        // assign
        `when`(searchBookUseCase(searchQuery)).thenReturn(
            flowOf(
                Result.Loading(),
                Result.Success(books)
            )
        )

        searchViewModel = SearchViewModel(searchBookUseCase)
        // act
        searchViewModel.queryChannel.value = searchQuery
        val result = searchViewModel.searchBookListResult

        result.observeForTesting {
            // verify
//            verify(searchBookUseCase).invoke(searchQuery)

            // assert
            assertTrue(result.value is Result.Success)
            assertNotNull(result.value?.data)

            assertTrue(result.value!!.data!!.isNotEmpty())
            assertEquals(result.value?.data, books)

        }
    }

}