package id.myone.capstone_books_store.favorite.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import id.myone.capstone_books_store.favorite.di.provideModuleDependencies
import id.myone.core.adapter.BookListAdapter
import id.myone.favorite.databinding.FragmentFavoriteBinding
import org.koin.android.ext.android.inject

private val loadFeatures by lazy { provideModuleDependencies() }
private fun injectFeatures() = loadFeatures

class FavoriteFragment : Fragment(), BookListAdapter.OnClickItemBookList {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by inject()

    private lateinit var bookListAdapter: BookListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        bookListAdapter = BookListAdapter()
        bookListAdapter.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding.booksList) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = bookListAdapter
        }

        this.provideFavoriteBooks()
    }

    private fun provideFavoriteBooks() {
        favoriteViewModel.favoriteBookList.observe(viewLifecycleOwner) {
            binding.loading.loadingContent.visibility = View.GONE

            if(it.isNotEmpty()) {
                binding.booksList.visibility = View.VISIBLE
                bookListAdapter.setData(it)
            } else {
                binding.infoFavorite.visibility = View.VISIBLE
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