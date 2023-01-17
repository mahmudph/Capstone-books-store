package id.myone.favorite.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.myone.core.adapter.BookListAdapter
import id.myone.favorite.databinding.FragmentFavoriteBinding
import org.koin.android.ext.android.inject
import java.net.URLEncoder

class FavoriteFragment : Fragment(), BookListAdapter.OnClickItemBookList {

    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bookAdapter = BookListAdapter()
        bookAdapter.setOnClickListener(this)

        favoriteViewModel.favoriteBookList.observe(viewLifecycleOwner) {
            binding.loading.loadingContent.visibility = View.GONE
            bookAdapter.setData(it)
        }

        with(binding.booksList) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = bookAdapter
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