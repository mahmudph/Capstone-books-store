/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source

import id.myone.core.data.source.local.LocalDataSource
import id.myone.core.data.source.remote.RemoteDataSource
import id.myone.core.data.source.remote.network.ApiResponse
import id.myone.core.data.source.remote.response.BookModel
import id.myone.core.domain.entity.Book
import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.repository.Repository
import id.myone.core.domain.utils.Result
import id.myone.core.utils.AppExecutors
import id.myone.core.utils.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class RepositoryImpl(
    private val localDatasource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val appExecutors: AppExecutors
) : Repository {

    override fun getBooksList(): Flow<Result<List<Book>>> =
        object : NetworkBoundResource<List<Book>, List<BookModel>>(appExecutors) {

            override fun loadFromDB(): Flow<List<Book>> {
                return localDatasource.getBooks().map {
                    DataMapper.mapBookEntityToBookDomain(it)
                }
            }

            override fun shouldFetch(data: List<Book>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<BookModel>>> {
                return remoteDataSource.getBookList()
            }

            override suspend fun saveCallResult(data: List<BookModel>) {
                val bookEntityList = data.map { DataMapper.transformBookModelToEntity(it) }
                localDatasource.bulkInsertBook(bookEntityList)
            }

        }.asFlow()

    override fun getFavoriteBookList(): Flow<List<Book>> {
        return localDatasource.getFavoriteBooks().map {
            DataMapper.mapBookEntityToBookDomain(it)
        }
    }

    override fun searchBooks(query: String, page: String): Flow<Result<List<Book>>> = flow {

        when (val response = remoteDataSource.searchBook(query, page)) {
            is ApiResponse.Error -> emit(Result.Error(message = response.errorMessage))
            is ApiResponse.Empty -> emit(Result.Success(emptyList()))
            is ApiResponse.Success -> {
                val data = DataMapper.mapBookModelToBookDomain(response.data.books)
                emit(Result.Success(data))
            }
        }
    }

    override fun getDetailBook(id: String): Flow<Result<BookDetail>> = flow {

        emit(Result.Loading())

        when (val response = remoteDataSource.getDetailBook(id)) {
            is ApiResponse.Success -> {
                val data = DataMapper.transformDetailBookToDetailBookDomain(response.data)
                emit(Result.Success(data))
            }
            is ApiResponse.Error -> {
                emit(Result.Error(response.errorMessage))
            }
            else -> emit(Result.Error(defaultErrorMessage))
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun insertBook(book: Book): Result<String> {
        return try {
            val bookEntity = DataMapper.transformBookDomainToEntity(book)
            localDatasource.insertBook(bookEntity)
            Result.Success(successAddToFavorite)
        } catch (e: Exception) {
            Result.Error(failedToAddFavorite)
        }
    }

    override suspend fun getIsInFavoriteBook(bookId: String): Boolean {
        return try {
            val favoriteBook = localDatasource.getBookById(bookId)
            return favoriteBook?.isFavorite ?: false
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateBook(book: Book): Result<String> {
        return try {
            val bookEntity = DataMapper.transformBookDomainToEntity(book)
            localDatasource.updateBook(bookEntity)
            Result.Success(successAddToFavorite)
        } catch (e: Exception) {
            Result.Error(failedToAddFavorite)
        }
    }

    override suspend fun getBookById(bookId: String): Book? {
        return try {
            val data = localDatasource.getBookById(bookId)
            return if (data != null) DataMapper.tranformBookEntityToBook(data) else null
        } catch (e: Exception) {
            null
        }
    }


    companion object {
        private const val successAddToFavorite = "Success to set book to the list favorite books"
        private const val failedToAddFavorite = "failed to set book to the list favorite books"

        private const val defaultErrorMessage = "failed to process request, try again"
        private const val noInternetConnectionError =
            "no network available, please check your internet connection"
    }
}