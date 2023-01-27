package id.myone.capstone_books_store.search_book.presentation

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.myone.capstone_books_store.search_book.R
import id.myone.capstone_books_store.search_book.databinding.FragmentSearchBinding
import id.myone.capstone_books_store.search_book.di.provideModuleDependencies
import id.myone.core.adapter.BookListAdapter
import id.myone.core.domain.utils.Result
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


val loadFeatures by lazy { provideModuleDependencies() }
fun injectFeatures() = loadFeatures

class SearchFragment : Fragment(), BookListAdapter.OnClickItemBookList {

    private lateinit var searchBinding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by inject()

    private lateinit var adapter: BookListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(this.javaClass.name, "configuration is changes")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        provideBookListAdapter()
        return searchBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.resultBookList.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Result.Loading -> this@SearchFragment.showLoading()
                is Result.Error -> this@SearchFragment.setVisibility(
                    View.VISIBLE,
                    View.GONE
                )
                is Result.Success -> {
                    if (res.data!!.isEmpty()) {
                        searchBinding.apply {
                            searchInformation.visibility = View.VISIBLE
                            loading.loadingContent.visibility = View.GONE
                            bookSearchList.visibility = View.GONE

                            title.text = getString(R.string.data_not_found)
                            subTitle.text = getString(R.string.data_not_found_desc)
                        }

                    } else {
                        adapter.setData(res.data!!)
                        this@SearchFragment.setVisibility(View.GONE, View.VISIBLE)
                    }
                }
            }
        }

        searchBinding.searchField.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= v!!.right - searchBinding.searchField.compoundDrawables[2].bounds.width()) {
                    this.searchBook()
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })

        searchBinding.searchField.setOnEditorActionListener { _, eventId, _ ->
            if (eventId == EditorInfo.IME_ACTION_SEARCH) {
                this.searchBook()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        searchBinding.searchField.addTextChangedListener {
            this.searchBook()
        }
    }

    private fun showLoading() {
        searchBinding.loading.loadingContent.visibility = View.VISIBLE
    }

    private fun provideBookListAdapter() {
        adapter = BookListAdapter()
        adapter.setOnClickListener(this)
        searchBinding.bookSearchList.adapter = adapter
        searchBinding.bookSearchList.layoutManager = GridLayoutManager(context, 2)
    }

    private fun setVisibility(showError: Int = View.VISIBLE, showBookResult: Int = View.GONE) {
        searchBinding.info.information.visibility = showError
        searchBinding.bookSearchList.visibility = showBookResult
        searchBinding.loading.loadingContent.visibility = View.GONE
        searchBinding.searchInformation.visibility = View.GONE
    }


    private fun searchBook() {

        searchBinding.apply {
            if (searchField.text.trim().isNotEmpty()) {
                searchField.text.let {
                    searchInformation.visibility = View.GONE
                    loading.loadingContent.visibility = View.VISIBLE
                    bookSearchList.visibility = View.GONE
                    lifecycleScope.launch {
                        searchViewModel.queryChannel.value = it.trim().toString()
                    }
                }
            }
        }
    }

    override fun onPressItem(bookId: String) {
        val detailBooks = NavDeepLinkRequest.Builder
            .fromUri("books-app://book-detail/$bookId".toUri())
            .build()
        findNavController().navigate(detailBooks)
    }
}