package id.myone.capstone_books_store.book.presentation.book

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.myone.book.R
import id.myone.book.databinding.FragmentBookBinding
import id.myone.capstone_books_store.book.di.provideModuleDependencies
import id.myone.capstone_books_store.book.presentation.detailBook.DetailBookFragment
import id.myone.core.adapter.BookListAdapter
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import org.koin.android.ext.android.inject

private val loadFeatures by lazy { provideModuleDependencies() }
private fun injectFeatures() = loadFeatures

class BookFragment : Fragment(), BookListAdapter.OnClickItemBookList {

    private val bookViewModel: BookViewModel by inject()

    private lateinit var fragmentBookBinding: FragmentBookBinding
    private lateinit var bookListAdapter: BookListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bookListAdapter = BookListAdapter()
        bookListAdapter.setOnClickListener(this)
        fragmentBookBinding = FragmentBookBinding.inflate(inflater, container, false)
        return fragmentBookBinding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.requestDataFromServer()


        with(fragmentBookBinding) {
            booksList.setHasFixedSize(true)
            booksList.layoutManager = GridLayoutManager(context, 2)
            this.booksList.adapter = bookListAdapter
        }

        fragmentBookBinding.refreshBookList.setOnRefreshListener {
            this.requestDataFromServer()
        }

        fragmentBookBinding.searchBookPanel.setOnTouchListener { _, _ ->
            findNavController().navigate(R.id.search_books)
            return@setOnTouchListener true
        }

    }

    private fun requestDataFromServer() {
        bookViewModel.bookList.observe(viewLifecycleOwner) {
             when (it) {
                is Result.Loading -> showLoading()
                is Result.Error -> showOnError()
                is Result.Success -> showOnSuccess(it.data!!)
            }
        }
    }

    private fun showLoading() {
        fragmentBookBinding.loading.loadingContent.visibility = View.VISIBLE
    }

    private fun showOnSuccess(bookList: List<Book>) {
        bookListAdapter.setData(bookList)
        fragmentBookBinding.refreshBookList.isRefreshing = false
        fragmentBookBinding.booksList.visibility = View.VISIBLE
        fragmentBookBinding.loading.loadingContent.visibility = View.GONE
    }

    private fun showOnError() {
        fragmentBookBinding.booksList.visibility = View.GONE
        fragmentBookBinding.info.information.visibility = View.VISIBLE
        fragmentBookBinding.loading.loadingContent.visibility = View.GONE
    }

    override fun onPressItem(bookId: String) {
        val bundle = Bundle().apply { putString(DetailBookFragment.BOOK_ID, bookId) }
        findNavController().navigate(R.id.action_bookFragment_to_detailBookFragment, args = bundle)
    }
}