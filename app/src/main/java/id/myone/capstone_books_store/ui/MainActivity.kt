package id.myone.capstone_books_store.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.myone.capstone_books_store.R
import id.myone.capstone_books_store.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.setUpAppNavigation()
    }

    private fun setUpAppNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        val navController = navHostFragment.navController

        val applicationBarConfig = AppBarConfiguration.Builder(
            R.id.books_collections, R.id.boorkmarks, R.id.search_books
        ).build()

        setupActionBarWithNavController(navController, applicationBarConfig)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id) {
                R.id.splashscreen -> {
                    supportActionBar?.hide()
                    binding.navView.visibility = View.GONE
                }
                id.myone.book.R.id.book_details -> {
                    supportActionBar?.hide()
                    binding.navView.visibility = View.GONE
                }
                else -> {
                    supportActionBar?.hide()
                    binding.navView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_container)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}