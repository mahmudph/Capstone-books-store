/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.data.source.remote

import id.myone.core.data.source.remote.network.ApiResponse
import id.myone.core.data.source.remote.network.ApiServices
import id.myone.core.data.source.remote.response.BookModel
import id.myone.core.data.source.remote.response.DetailBooksResponse
import id.myone.core.data.source.remote.response.ListBooksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.SocketException

class RemoteDataSource(private val apiServices: ApiServices) {
    suspend fun getBookList(): Flow<ApiResponse<List<BookModel>>> = flow {
        try {
            val bookList = apiServices.getNewListBooks().books
            if (bookList.isNotEmpty()) emit(ApiResponse.Success(bookList))
            else emit(ApiResponse.Empty)
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getDetailBook(id: String): ApiResponse<DetailBooksResponse> {
        return try {
            val result = apiServices.getDetailBook(id)
            ApiResponse.Success(result)
        } catch (e: SocketException) {
            ApiResponse.Error("failed to connect service, please try again")
        } catch (e: Exception) {
            ApiResponse.Error("failed to get detail book")
        }

    }

    suspend fun searchBook(query: String, page: String): ApiResponse<ListBooksResponse> {
        return try {
            val result = apiServices.searchBook(query, page)
            if (result.books.isNotEmpty()) ApiResponse.Success(result)
            else ApiResponse.Empty
        } catch (e: SocketException) {
            ApiResponse.Error("failed to connect service, please try again")
        } catch (e: Exception) {
            ApiResponse.Error("failed to search a book")
        }
    }
}