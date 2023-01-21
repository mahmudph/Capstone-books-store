/**
 * Created by Mahmud on 16/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store

import com.google.android.play.core.splitcompat.SplitCompatApplication
import id.myone.book.di.bookUseCaseModel
import id.myone.book.di.bookViewModelModule
import id.myone.capstone_books_store.di.appViewModelModule
import id.myone.core.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ApplicationBase : SplitCompatApplication() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(applicationContext)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    appStorageModule,
                    repositoryModule,
                    bookUseCaseModel,
                    appViewModelModule,
                    bookViewModelModule,
                    utilityModule
                )
            )
        }
    }
}