package id.myone.capstone_books_store.book.domain.usecase

import id.myone.core.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetBookFavoriteStatusUseCaseTest {

    @Mock lateinit var repository: Repository

    private val bookId = "1"
    private lateinit var getBookFavoriteStatusUseCase: GetBookFavoriteStatusUseCase

    @Before
    fun setUp() {
        getBookFavoriteStatusUseCase = GetBookFavoriteStatusUseCase(repository)
    }

    @Test
    fun `getIsInFavoriteBook should return true when book id is in list of favorite book`() = runTest {
        // assign
        `when`(repository.getIsInFavoriteBook(bookId)).thenReturn(true)
        // act
        val isFavoriteBook = getBookFavoriteStatusUseCase(bookId)
        // verify
        verify(repository).getIsInFavoriteBook(bookId)
        // assert
        Assert.assertTrue(isFavoriteBook)
    }

    @Test
    fun `getIsInFavoriteBook should return false when book id is not in list of favorite book`() = runTest {
        // assign
        `when`(repository.getIsInFavoriteBook(bookId)).thenReturn(false)
        // act
        val isFavoriteBook = getBookFavoriteStatusUseCase(bookId)
        // verify
        verify(repository).getIsInFavoriteBook(bookId)
        // assert
        Assert.assertFalse(isFavoriteBook)
    }
}