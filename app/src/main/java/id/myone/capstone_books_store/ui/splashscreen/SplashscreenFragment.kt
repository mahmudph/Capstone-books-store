package id.myone.capstone_books_store.ui.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.myone.capstone_books_store.databinding.FragmentSplashscreenBinding
import org.koin.android.ext.android.inject

@SuppressLint("CustomSplashScreen")
class SplashscreenFragment : Fragment() {
    private lateinit var splashscreenBinding: FragmentSplashscreenBinding


    private val splashscreenViewModel: SplashscreenViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        splashscreenBinding = FragmentSplashscreenBinding.inflate(layoutInflater)
        return splashscreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        splashscreenViewModel.checkIsPassedBoarding().observe(viewLifecycleOwner) {
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToDashboard(it)
            }, 3000)
        }

    }

    private fun navigateToDashboard(value: Boolean) {
        val dashboardNavigation = if (value) {
            SplashscreenFragmentDirections.actionSplashscreenToBooksCollections()
        } else {
            SplashscreenFragmentDirections.actionSplashscreenToOnboarding()
        }
        findNavController().navigate(dashboardNavigation)
    }

}