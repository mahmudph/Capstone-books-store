package id.myone.capstone_books_store.book.presentation.book

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.myone.capstone_books_store.book.R
import id.myone.capstone_books_store.book.databinding.FragmentBookBinding
import id.myone.capstone_books_store.book.di.provideModuleDependencies
import id.myone.capstone_books_store.book.presentation.detailBook.DetailBookFragment
import id.myone.core.adapter.BookListAdapter
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import id.myone.core.utils.DynamicFeatureManagerUtility
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class BookFragment : Fragment(), BookListAdapter.OnClickItemBookList {

    private val bookViewModel: BookViewModel by viewModel()
    private val moduleUtility: DynamicFeatureManagerUtility by inject()

    private var fragmentBookBinding: FragmentBookBinding? = null
    private var bookListAdapter: BookListAdapter? = null

    private var hasLoadData = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideModuleDependencies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bookListAdapter = BookListAdapter()
        bookListAdapter?.setOnClickListener(this)
        fragmentBookBinding = FragmentBookBinding.inflate(inflater, container, false)
        return fragmentBookBinding!!.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentBookBinding?.also {
            it.booksList.setHasFixedSize(true)
            it.booksList.layoutManager = GridLayoutManager(context, 2)
            it.booksList.adapter = bookListAdapter

            it.refreshBookList.setOnRefreshListener {
                bookViewModel.getBookList(true)
            }

            it.searchBookPanel.setOnTouchListener { _, _ ->

                moduleUtility.setSplitInstallerCallback(object :
                    DynamicFeatureManagerUtility.SplitInstallManagerCallback {
                    override fun onInstallInProgress(progress: Int) {
                        Log.i(this@BookFragment.javaClass.name, progress.toString())
                    }

                    override fun onSuccess() {
                        val searchBook = NavDeepLinkRequest.Builder
                            .fromUri("books-app://search".toUri())
                            .build()

                        findNavController().navigate(searchBook)
                    }

                    override fun onError(e: Exception) {
                        e.printStackTrace()
                        Log.e(this@BookFragment.javaClass.name, e.toString())
                    }
                })

                moduleUtility.installModule("search_book")

                return@setOnTouchListener true
            }
        }
        this.requestDataFromServer()
    }

    private fun requestDataFromServer() {
        bookViewModel.bookListData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    showLoading()
                }
                is Result.Error -> showOnError()
                is Result.Success -> {
                    it.data?.let { data ->
                        hasLoadData = true
                        showOnSuccess(data)
                    }
                }
                else -> {}
            }
        }
    }

    private fun showLoading() {
        fragmentBookBinding?.refreshBookList?.visibility = View.GONE
        fragmentBookBinding?.loading?.visibility = View.VISIBLE
    }

    private fun showOnSuccess(bookList: List<Book>) {
        val delay = if (!hasLoadData) 1200L  else  0L
        Handler(Looper.getMainLooper()).postDelayed({
            bookListAdapter?.setData(bookList)
            fragmentBookBinding?.also {
                it.refreshBookList.isRefreshing = false
                it.refreshBookList.visibility = View.VISIBLE
                it.booksList.visibility = View.VISIBLE
                it.loading.visibility = View.GONE
            }
        }, delay)
    }

    private fun showOnError() {
        fragmentBookBinding?.also {
            it.booksList.visibility = View.GONE
            it.info.information.visibility = View.VISIBLE
            it.loading.visibility = View.GONE
        }
    }

    override fun onPressItem(bookId: String) {
        val bundle = Bundle().apply { putString(DetailBookFragment.BOOK_ID, bookId) }
        findNavController().navigate(R.id.action_bookFragment_to_detailBookFragment, args = bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBookBinding = null
        bookListAdapter = null
    }
}