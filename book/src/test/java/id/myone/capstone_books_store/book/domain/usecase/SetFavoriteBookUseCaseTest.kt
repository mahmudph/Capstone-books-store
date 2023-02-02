package id.myone.capstone_books_store.book.domain.usecase


import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SetFavoriteBookUseCaseTest {

    @Mock lateinit var repository: Repository
    private lateinit var setFavoriteStatusUseCase: SetFavoriteBookUseCase

    @Before
    fun setUp() {
        setFavoriteStatusUseCase = SetFavoriteBookUseCase(repository)
    }

    private val bookId = "1"
    private val detailBook = BookDetail(
        title = "title",
        subtitle = "subtitle",
        isbn10 = "isbn10",
        isbn13 = "isbn13",
        pages = "1",
        price = "$20",
        image = "http://api.storybook.io/mlkit/image.png",
        url = "http://api.storybook.io/mlkit",
        publisher = "publisher",
        authors = "author",
        desc = "desc",
        year = "2022",
        error = "none",
        pdf = null,
        rating = "10"
    )

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    @Test
    fun `should success to set or add book in favorite book list`() = runTest {

        // assign
        `when`(repository.insertFavoriteBook(any())).thenReturn(
            Result.Success("success to add book into favorite book list")
        )
        // act
        val result = setFavoriteStatusUseCase(bookId, detailBook, true)
        // verify
        verify(repository).insertFavoriteBook(any())
        // assert
        Assert.assertTrue(result is Result.Success)
        Assert.assertEquals(result.data, "success to add book into favorite book list")
    }

    @Test
    fun `should delete book from list of favorite book when book has already in list favorite`() = runTest {

        // assign
        `when`(repository.deleteFavoriteBook(bookId)).thenReturn(
            Result.Success("success to delete book from favorite book list")
        )
        // act
        val result = setFavoriteStatusUseCase(bookId, detailBook, false)
        // verify
        verify(repository).deleteFavoriteBook(any())
        // assert
        Assert.assertTrue(result is Result.Success)
        Assert.assertEquals(result.data, "success to delete book from favorite book list")
    }
}