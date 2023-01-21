/**
 * Created by Mahmud on 20/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.capstone_books_store.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.myone.capstone_books_store.onboarding.presenter.slider.SlideFragment

class SliderAdapter(activity: FragmentActivity, private val slider: Array<SliderData>): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = slider.size

    override fun createFragment(position: Int): Fragment {
        return SlideFragment.newInstance(slider[position])
    }
}