package id.myone.capstone_books_store.book.presentation.detailBook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import id.myone.book.R
import id.myone.book.databinding.FragmentDetailBookBinding
import id.myone.capstone_books_store.book.di.provideModuleDependencies
import id.myone.core.domain.entity.BookDetail
import id.myone.core.domain.utils.Result
import org.koin.android.ext.android.inject

val loadFeatures by lazy { provideModuleDependencies() }
fun injectFeatures() = loadFeatures

class DetailBookFragment : Fragment() {
    private lateinit var binding: FragmentDetailBookBinding
    private val detailBookViewModel: DetailBookViewModel by inject()

    private var bookDetailTemp: BookDetail? = null
    private var isFavoriteBook: Boolean = false
    private lateinit var bookId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.loading.loadingContent.visibility = View.VISIBLE
        binding.contentBookDetail.visibility = View.GONE

        bookId = requireArguments().getString(BOOK_ID)!!

        binding.backButton.setOnClickListener {
            view.findNavController().popBackStack()
        }

        binding.addFavorite.setOnClickListener {
            setFavoriteBook()
        }

        this.loadDetailBook(savedInstanceState)
    }

    private fun showLoading() {
        binding.contentBookDetail.visibility = View.GONE
        binding.loading.loadingContent.visibility = View.VISIBLE
    }

    private fun loadDetailBook(savedInstanceState: Bundle?) {
        detailBookViewModel.bookDetails.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> showLoading()
                is Result.Error -> showErrorMessage()
                is Result.Success -> showSuccessData(it.data!!)
            }
        }

        if (savedInstanceState == null) detailBookViewModel.loadBookDetail(bookId)
        this.getBookFavoriteStatus()

    }

    private fun getBookFavoriteStatus() {
        detailBookViewModel.getBookFavoriteStatus(bookId)
            .observe(viewLifecycleOwner) { isFavorite ->
                isFavoriteBook = isFavorite

                val iconDrawable = if (isFavorite) {
                    binding.addFavorite.text = getString(R.string.delete_favorite)
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_favorite_24
                    )
                } else {
                    binding.addFavorite.text = getString(R.string.add_favorite)
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_favorite_border_24
                    )
                }

                binding.addFavorite.setCompoundDrawablesWithIntrinsicBounds(
                    iconDrawable, null, null, null,
                )
            }
    }


    private fun setFavoriteBook() {
        bookDetailTemp?.let {
            detailBookViewModel.setFavoriteBook(bookId, it, !isFavoriteBook)
                .observe(viewLifecycleOwner) {
                    this.getBookFavoriteStatus()
                }
        }
    }

    private fun showSuccessData(bookDetail: BookDetail) {
        bookDetailTemp = bookDetail

        with(binding) {
            loading.loadingContent.visibility = View.GONE
            binding.contentBookDetail.visibility = View.VISIBLE

            author.text = getString(R.string.author_by, bookDetail.authors.trim())
            bookTitle.text = bookDetail.title.trim()
            bookSubTitle.text = bookDetail.subtitle.trim()
            bookPrice.text = bookDetail.price.trim()

            description.text = bookDetail.desc.trim()
            isbn10.text = bookDetail.isbn10.trim()
            isbn13.text = bookDetail.isbn13.trim()
            publisher.text = bookDetail.publisher.trim()
            pages.text = getString(R.string.pages_count, bookDetail.pages.trim())
            year.text = bookDetail.year.trim()
            ratting.text = bookDetail.rating.trim()
            url.text = bookDetail.url.trim()

            Glide.with(this@DetailBookFragment).load(bookDetail.image).fitCenter().into(bookImage)
        }
    }

    private fun showErrorMessage() {
        binding.loading.loadingContent.visibility = View.GONE
        binding.loading.loadingContent.visibility = View.GONE
        binding.information.information.visibility = View.VISIBLE
    }

    companion object {
        const val BOOK_ID = "BOOK_ID"
    }
}