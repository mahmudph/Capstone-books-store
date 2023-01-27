/**
 * Created by Mahmud on 21/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.utils

import android.content.Context
import android.content.SharedPreferences

class SecureStorageApp(private val sharedPreferences: SharedPreferences) {

    private val editSharePreferences: SharedPreferences.Editor by lazy {
        sharedPreferences.edit()
    }

    fun setBoolValue(key: String, value: Boolean) {
        editSharePreferences.putBoolean(key, value)
        editSharePreferences.apply()
    }

    fun getBoolValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    companion object {
        @JvmStatic
        fun getSharePreferencesData(context: Context): SharedPreferences {
            return context.getSharedPreferences("BOOKSTORE", Context.MODE_PRIVATE)
        }
    }
}