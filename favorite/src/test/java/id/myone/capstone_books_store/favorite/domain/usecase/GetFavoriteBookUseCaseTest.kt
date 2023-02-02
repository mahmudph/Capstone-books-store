package id.myone.capstone_books_store.favorite.domain.usecase



import app.cash.turbine.test
import id.myone.core.domain.entity.Book
import id.myone.core.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class GetFavoriteBookUseCaseTest {

    @Mock lateinit var repository: Repository
    private lateinit var getFavoriteBookUseCase: GetFavoriteBookUseCase

    @Before
    fun setUp() {
        getFavoriteBookUseCase = GetFavoriteBookUseCase(repository)
    }


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
    fun `get list of the favorite book should return no empty data`() = runTest {

        // arrange
        val expectedDataFlow = flowOf(favoriteBooks)
        `when`(repository.getFavoriteBookList()).thenReturn(expectedDataFlow)

        // act
        val favoriteBookList = getFavoriteBookUseCase()

        favoriteBookList.test {
            // verify
            verify(repository).getFavoriteBookList()

            // assert
            val dataState = awaitItem()
            Assert.assertTrue(dataState.isNotEmpty())
            Assert.assertEquals(dataState, favoriteBooks)
            awaitComplete()
        }
    }

    @Test
    fun `get list of the favorite book should return empty list of book`() = runTest {

        // arrange
        val expectedDataFlow = flowOf<List<Book>>(emptyList())
        `when`(repository.getFavoriteBookList()).thenReturn(expectedDataFlow)

        // act
        val favoriteBookList = getFavoriteBookUseCase()

        favoriteBookList.test {

            // verify
            verify(repository).getFavoriteBookList()

            // assert
            val dataState = awaitItem()
            Assert.assertTrue(dataState.isEmpty())
            awaitComplete()
        }
    }
}