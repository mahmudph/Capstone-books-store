package id.myone.book.presentation.detailBook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.myone.book.databinding.FragmentDetailBookBinding
import id.myone.core.domain.utils.Result
import org.koin.android.ext.android.inject

class DetailBookFragment : Fragment() {
    private lateinit var binding: FragmentDetailBookBinding
    private val detailBookViewModel: DetailBookViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailBookViewModel.bookDetail.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {

                }
                is Result.Success -> {

                }
            }
        }

        this.loadDetailBook()
    }

    private fun loadDetailBook() {
        val bookId = requireArguments().getString(BOOK_ID)!!
        detailBookViewModel.loadBookDetail(bookId)
    }


    companion object {
        const val BOOK_ID = "BOOK_ID"
    }
}