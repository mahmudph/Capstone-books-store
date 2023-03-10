package id.myone.capstone_books_store.onboarding.presenter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import id.myone.capstone_books_store.onboarding.adapter.SliderAdapter
import id.myone.capstone_books_store.onboarding.adapter.SliderData
import id.myone.capstone_books_store.onboarding.di.provideModuleDependencies
import id.myone.onboarding.R
import id.myone.onboarding.databinding.FragmentOnBoardingBinding
import org.koin.android.ext.android.inject


private val loadFeatures by lazy { provideModuleDependencies() }
private fun injectFeatures() = loadFeatures


class OnBoardingFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentOnBoardingBinding
    private lateinit var sliderAdapter: SliderAdapter

    private lateinit var sliderDataList: Array<SliderData>

    private lateinit var sliderDotIndicator: Array<TextView>

    private val onBoardingViewModel: OnBoardingViewModel by inject()


    private val pageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            /**
             * set visibility of the previous btn
             */
            binding.previous.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE


            /**
             * should change next btn icon to done icon
             */
            if (position == sliderDataList.size - 1) {
                binding.next.setImageResource(R.drawable.ic_baseline_check_circle_24)

            } else {
                binding.next.setImageResource(R.drawable.ic_baseline_arrow_circle_right_24)
            }
            /**
             * change a dot indicator of the application intro
             */
            provideSliderDotIndicator(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectFeatures()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sliderDataList = provideSliderData()
        sliderDotIndicator = provideSliderDotIndicatorData()

        sliderAdapter = SliderAdapter(requireActivity(), sliderDataList)

        binding.viewPager.adapter = sliderAdapter
        binding.viewPager.orientation = ORIENTATION_HORIZONTAL

        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)

        binding.next.setOnClickListener(this)
        binding.previous.setOnClickListener(this)
    }

    private fun provideSliderData(): Array<SliderData> {
        val sliderDataListTemp = mutableListOf<SliderData>()

        val introTitle = resources.getStringArray(R.array.title_intro)
        val introSubtitle = resources.getStringArray(R.array.subtitle_intro)
        val introImage = resources.obtainTypedArray(R.array.image_intro)

        if (introTitle.size != introSubtitle.size) throw IllegalStateException("intro data does not have same size")

        for (item in introTitle.indices) {
            sliderDataListTemp.add(
                SliderData(
                    title = introTitle[item],
                    description = introSubtitle[item],
                    image = introImage.getResourceId(item, -1)
                )
            )
        }

        introImage.recycle()
        return sliderDataListTemp.toTypedArray()
    }

    private fun provideSliderDotIndicatorData(): Array<TextView> = arrayOf(
        binding.dotFirst,
        binding.dotSecond,
        binding.dotThree
    )


    private fun provideSliderDotIndicator(position: Int) {

        val colorActive = ContextCompat.getColor(requireContext(), R.color.active_indicator)
        val colorInactive = ContextCompat.getColor(requireContext(), R.color.inactive_indicator)

        for (dotItem in sliderDotIndicator.indices) {
            if (position == dotItem) {
                val currentActiveDot = sliderDotIndicator[dotItem]
                currentActiveDot.setTextColor(colorActive)
            } else {
                val currentInactiveDot = sliderDotIndicator[dotItem]
                currentInactiveDot.setTextColor(colorInactive)
            }
        }
    }

    override fun onDestroyView() {
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        val currentItem = binding.viewPager.currentItem

        when (v?.id) {
            R.id.next -> {

                if (currentItem == sliderDataList.size - 1) {
                    onBoardingViewModel.setIsPassOnBoardingPage().observe(viewLifecycleOwner) {
                        val navigation =
                            OnBoardingFragmentDirections.actionOnboardingToBooksCollections()
                        findNavController().navigate(navigation)
                    }
                } else {
                    if (currentItem < sliderDataList.size - 1) {
                        binding.viewPager.currentItem = currentItem + 1
                    }
                }
            }

            R.id.previous -> {
                if (currentItem > 0) {
                    binding.viewPager.currentItem = currentItem - 1
                }
            }
        }
    }

}