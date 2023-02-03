package id.myone.capstone_books_store.book.domain.usecase

import app.cash.turbine.test
import id.myone.core.domain.entity.Book
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result
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
class GetBookListUseCaseTest {
    @Mock
    private lateinit var repository: Repository
    private lateinit var getBookListUseCase: GetBookListUseCase

    @Before
    fun setUp() {
        getBookListUseCase = GetBookListUseCase(repository)
    }

    private val booksLists = listOf(
        Book(
            id = "1",
            title = "title",
            subtitle = "subtitle",
            price = "$20",
            image = "http://test.image/abc.png",
            url = "http://test.image/book"
        )
    )


    @Test
    fun `should return Result Success and get list of the book when call getBookListUseCase`() =
        runTest {

            // assign
            val expectedFlow = flowOf(Result.Loading(), Result.Success(booksLists))
            `when`(repository.getBooksList(true)).thenReturn(expectedFlow)

            // act
            val listBooksResult = getBookListUseCase(true)

            listBooksResult.test {
                // verify
                verify(repository).getBooksList(true)

                val loadingState = awaitItem()
                val dataState = awaitItem()

                // assert
                Assert.assertTrue(loadingState is Result.Loading)
                Assert.assertTrue(dataState is Result.Success)
                Assert.assertNotNull(dataState.data)

                Assert.assertEquals(dataState.data?.size, booksLists.size)
                Assert.assertEquals(dataState.data?.first()?.id, booksLists.first().id)
                Assert.assertEquals(dataState.data?.first()?.title, booksLists.first().title)
                awaitComplete()
            }

        }

    @Test
    fun `should return Result Error when no internet connection when call getBookListUseCase`() =
        runTest {

            // assign
            val expectedFlow = flowOf<Result<List<Book>>>(
                Result.Loading(), Result.Error(
                    "failed to connect to the server please try again"
                )
            )

            `when`(repository.getBooksList(true)).thenReturn(expectedFlow)

            // act
            val listBooksResult = getBookListUseCase(true)

            listBooksResult.test {
                // verify
                verify(repository).getBooksList(true)

                val loadingState = awaitItem()
                val dataState = awaitItem()

                // assert
                Assert.assertTrue(loadingState is Result.Loading)
                Assert.assertTrue(dataState is Result.Error)

                Assert.assertEquals(
                    dataState.message,
                    "failed to connect to the server please try again"
                )
                awaitComplete()
            }
        }
}
