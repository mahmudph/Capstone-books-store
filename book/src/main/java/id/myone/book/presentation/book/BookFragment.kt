package id.myone.book.presentation.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.myone.book.databinding.FragmentBookBinding
import id.myone.core.adapter.BookListAdapter
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import org.koin.android.ext.android.inject

class BookFragment : Fragment() {

    private val bookViewModel: BookViewModel by inject()

    private lateinit var fragmentBookBinding: FragmentBookBinding
    private lateinit var bookListAdapter: BookListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bookListAdapter = BookListAdapter()
        fragmentBookBinding = FragmentBookBinding.inflate(inflater, container, false)
        return fragmentBookBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookViewModel.bookList.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> showOnError()
                is Result.Success -> showOnSuccess(it.data!!)
            }
        }

        with(fragmentBookBinding) {
            booksList.setHasFixedSize(true)
            booksList.layoutManager = LinearLayoutManager(context)
            this.booksList.adapter = bookListAdapter
        }
    }

    private fun showOnSuccess(bookList: List<Book>) {
        bookListAdapter.setData(bookList)
        fragmentBookBinding.booksList.visibility = View.VISIBLE
        fragmentBookBinding.loading.loadingContent.visibility = View.GONE
    }

    private fun showOnError() {
        fragmentBookBinding.booksList.visibility = View.GONE
        fragmentBookBinding.info.information.visibility = View.VISIBLE
        fragmentBookBinding.loading.loadingContent.visibility = View.GONE
    }
}