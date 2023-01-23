/**
 * Created by Mahmud on 23/01/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.utils

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest

class DynamicFeatureManagerUtility(
    private val splitInstallManager: SplitInstallManager
) {

    interface SplitInstallManagerCallback {
        fun onInstallInProgress(progress: Int)
        fun onSuccess()
        fun onError(e: Exception)
    }


    private var splitInstallManagerCallback: SplitInstallManagerCallback? = null


    fun setSplitInstallerCallback(event: SplitInstallManagerCallback) {
        splitInstallManagerCallback = event
    }

    fun installModule(moduleName: String) {

        if (splitInstallManagerCallback == null) {
            throw IllegalArgumentException("callback splitInstallManagerCallback have not yet being set")
        }

        val installedModules = splitInstallManager.installedModules

        if (installedModules.contains(moduleName)) {
            splitInstallManagerCallback!!.onSuccess()
        } else {

            val request = SplitInstallRequest.newBuilder().addModule(moduleName).build()

            splitInstallManager.startInstall(request)
                .addOnSuccessListener { splitInstallManagerCallback!!.onInstallInProgress(it) }
                .addOnCompleteListener { splitInstallManagerCallback!!.onSuccess() }
                .addOnFailureListener { splitInstallManagerCallback!!.onError(it) }
        }
    }

    companion object {
        @JvmStatic
        fun createSplitInstallManager(context: Context): SplitInstallManager {
            return SplitInstallManagerFactory.create(context)
        }
    }

}