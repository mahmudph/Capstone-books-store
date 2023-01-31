package id.myone.core.data.source

import id.myone.core.data.source.remote.network.ApiResponse
import id.myone.core.domain.utils.Result
import id.myone.core.utils.AppExecutors
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType>(private val mExecutors: AppExecutors) {

    private val result: Flow<Result<ResultType>> = flow {

        emit(Result.Loading())

        val dBounce = loadFromDB().first()

        if (shouldFetch(dBounce)) {

            emit(Result.Loading())

            when (val apiResponse = createCall().first()) {

                is ApiResponse.Success -> {
                    saveCallResult(apiResponse.data)
                    emitAll(loadFromDB().map { Result.Success(it) })
                }

                is ApiResponse.Empty -> {
                    emitAll(loadFromDB().map { Result.Success(it) })
                }

                is ApiResponse.Error -> {
                    emit(Result.Error(apiResponse.errorMessage))
                }
            }

        } else {
            emitAll(loadFromDB().map { Result.Success(it) })
        }
    }

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Result<ResultType>> = result
}