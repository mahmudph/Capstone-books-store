package id.myone.capstone_books_store.onboarding.presenter.slider


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.myone.capstone_books_store.onboarding.adapter.SliderData
import id.myone.capstone_books_store.onboarding.databinding.FragmentSlideBinding


class SlideFragment : Fragment() {

    private lateinit var sliderData: SliderData
    private lateinit var binding: FragmentSlideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sliderData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_PARAM, SliderData::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable(ARG_PARAM)!!
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSlideBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("DiscouragedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            sliderTitle.text = sliderData.title
            sliderDesc.text = sliderData.description
            sliderImage.setImageResource(sliderData.image)
        }
    }

    companion object {

        private const val ARG_PARAM = "SLIDE_PARAM"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SlideFragment.
         */
        @JvmStatic
        fun newInstance(sliderData: SliderData) = SlideFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PARAM, sliderData)
            }
        }
    }
}