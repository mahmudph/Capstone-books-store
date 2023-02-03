package id.myone.capstone_books_store.favorite.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.myone.capstone_books_store.favorite.databinding.FragmentFavoriteBinding
import id.myone.capstone_books_store.favorite.di.provideModuleDependencies
import id.myone.core.adapter.BookListAdapter
import id.myone.core.domain.entity.Book
import id.myone.core.domain.utils.Result
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteFragment : Fragment(), BookListAdapter.OnClickItemBookList {

    private var binding: FragmentFavoriteBinding? = null
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    private var bookListAdapter: BookListAdapter? = null

    private var hasLoadData: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideModuleDependencies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        bookListAdapter = BookListAdapter()
        bookListAdapter?.setOnClickListener(this)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding!!.booksList) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = bookListAdapter
        }

        this.provideFavoriteBooks()

    }

    private fun provideFavoriteBooks() {
        favoriteViewModel.getFavoriteBookList.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    binding?.loading?.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    if (it.data!!.isNotEmpty()) {
                        if (!hasLoadData) {
                            hasLoadData = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                this.handleOnSuccessLoadData(it.data!!)
                            }, 500)
                        } else {
                            this.handleOnSuccessLoadData(it.data!!)
                        }
                    } else {
                        binding?.loading?.visibility = View.GONE
                        binding!!.infoFavorite.visibility = View.VISIBLE
                    }
                }
                else -> {
                    binding!!.infoFavorite.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun handleOnSuccessLoadData(data: List<Book>) {
        binding?.loading?.visibility = View.GONE
        binding!!.booksList.visibility = View.VISIBLE
        bookListAdapter?.setData(data)
    }

    override fun onPressItem(bookId: String) {

        val detailBooks = NavDeepLinkRequest.Builder
            .fromUri("books-app://book-detail/$bookId".toUri())
            .build()

        findNavController().navigate(detailBooks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        bookListAdapter = null
    }
}