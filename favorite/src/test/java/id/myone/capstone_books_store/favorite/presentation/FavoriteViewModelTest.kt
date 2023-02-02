package id.myone.capstone_books_store.favorite.presentation


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import id.myone.capstone_books_store.favorite.domain.usecase.GetFavoriteBookUseCase
import id.myone.core.domain.entity.Book
import id.myone.test_utility.utility.MainDispatcherRule
import id.myone.test_utility.utility.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {
    @Mock
    lateinit var getFavoriteBookUseCase: GetFavoriteBookUseCase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()


    private lateinit var favoriteViewModel: FavoriteViewModel

    private val favoriteBooks = listOf(
        Book(
            id = "10",
            title = "title",
            subtitle = "subtitle",
            price = "$20",
            image = "http://test.image/abc.png",
            url = "http://test.image/book"
        )
    )

    @Test
    fun `should success to get list of the favorite book list and not empty`() = runTest {
        // assign
        val expectedFavoriteBookResultList = flowOf(favoriteBooks)

        `when`(getFavoriteBookUseCase()).thenReturn(expectedFavoriteBookResultList)
        favoriteViewModel = FavoriteViewModel(getFavoriteBookUseCase)
        // act
        val favoriteBookResult = favoriteViewModel.getFavoriteBookList.getOrAwaitValue()
        // verify
        verify(getFavoriteBookUseCase).invoke()
        // assert
        Assert.assertTrue(favoriteBookResult.isNotEmpty())
        Assert.assertEquals(favoriteBookResult.size, favoriteBooks.size)
        Assert.assertEquals(favoriteBookResult.first().id, favoriteBooks.first().id)
        Assert.assertEquals(favoriteBookResult.first().title, favoriteBooks.first().title)

    }

    @Test
    fun `should success and return empty data when user have not yet add book into favorite `() = runTest {
        // assign
        val expectedFavoriteBookResultList = flowOf<List<Book>>(emptyList())

        `when`(getFavoriteBookUseCase()).thenReturn(expectedFavoriteBookResultList)
        favoriteViewModel = FavoriteViewModel(getFavoriteBookUseCase)
        // act
        val favoriteBookResult = favoriteViewModel.getFavoriteBookList.getOrAwaitValue()
        // verify
        verify(getFavoriteBookUseCase).invoke()
        // assert
        Assert.assertTrue(favoriteBookResult.isEmpty())
    }
}