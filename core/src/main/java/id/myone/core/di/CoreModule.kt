/**
 * Created by Mahmud on 15/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.di

import android.content.SharedPreferences
import androidx.room.Room
import id.myone.core.data.source.RepositoryImpl
import id.myone.core.data.source.local.LocalDataSource
import id.myone.core.data.source.local.room.BookDatabase
import id.myone.core.data.source.remote.RemoteDataSource
import id.myone.core.data.source.remote.network.ApiConfig
import id.myone.core.domain.repository.Repository
import id.myone.core.utils.AppExecutors
import id.myone.core.utils.ModuleUtility
import id.myone.core.utils.SecureStorageApp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val databaseModule = module {
    factory { get<BookDatabase>().booksDao() }

    single {
        Room.databaseBuilder(
            androidContext(), BookDatabase::class.java, "db_book_store.db",
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        ApiConfig.provideApiService()
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<Repository> { RepositoryImpl(get(), get(), get()) }
}


val utilityModule = module {
    factory { ModuleUtility(get()) }
}


val appStorageModule = module {
    single { SecureStorageApp.getSharePreferencesData(androidContext()) }
    single { SecureStorageApp(get()) }
}