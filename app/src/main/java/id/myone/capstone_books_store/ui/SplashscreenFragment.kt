package id.myone.capstone_books_store.ui

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
import id.myone.capstone_books_store.databinding.FragmentSplashscreenBinding

class SplashscreenFragment : Fragment() {
    private lateinit var splashscreenBinding: FragmentSplashscreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        splashscreenBinding = FragmentSplashscreenBinding.inflate(layoutInflater)
        return splashscreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToDashboard()
        }, 3000)
    }


    private fun navigateToDashboard() {
        val dashboard = NavDeepLinkRequest.Builder.fromUri(
            DASHBOARD_PAGE.toUri()
        ).build()

        findNavController().navigate(dashboard)
    }

    companion object {
        private const val DASHBOARD_PAGE = "books-app://books-collection"
    }
}