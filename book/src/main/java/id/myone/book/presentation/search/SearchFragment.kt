package id.myone.book.presentation.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.myone.book.databinding.FragmentSearchBinding
import id.myone.core.adapter.BookListAdapter
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import org.koin.android.ext.android.inject
import java.net.URLEncoder

class SearchFragment : Fragment(), BookListAdapter.OnClickItemBookList {

    private lateinit var searchBinding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by inject()

    private lateinit var adapter: BookListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = BookListAdapter()
        adapter.setOnClickListener(this)
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return searchBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.bookList.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    this.setVisibility(View.VISIBLE, View.GONE)
                }

                is Result.Success -> {
                    this.setVisibility(View.GONE, View.VISIBLE)
                    this.updateBookResult(it.data!!)
                }
            }
        }

        searchBinding.searchField.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= v!!.right - searchBinding.searchField.compoundDrawables[2].bounds.width()) {
                    this@SearchFragment.searchBook()
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

    private fun setVisibility(showError: Int = View.VISIBLE, showBookResult: Int = View.GONE) {
        searchBinding.info.information.visibility = showError
        searchBinding.searchBookContainer.visibility = showBookResult
    }

    private fun updateBookResult(bookList: List<Book>) {
        adapter.setData(bookList)
        searchBinding.bookSearchList.adapter = adapter
        searchBinding.bookSearchList.layoutManager = LinearLayoutManager(context)
    }


    private fun searchBook() {
        searchBinding.loading.loadingContent.visibility = View.VISIBLE
        searchBinding.searchField.text.let {
            if (it.trim().isNotEmpty()) searchViewModel.searchBook(it.trim().toString())
        }
    }

    override fun onPressItem(bookId: String) {
        val encodeBookId = URLEncoder.encode(bookId, "utf-8")
        val request = NavDeepLinkRequest.Builder.fromUri(
            "$BOOK_DETAIL$encodeBookId".toUri()
        ).build()

        findNavController().navigate(request)
    }

    companion object {
        private const val BOOK_DETAIL = "books-app://book-detail"
    }
}