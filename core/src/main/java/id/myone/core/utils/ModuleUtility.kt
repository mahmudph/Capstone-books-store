package id.myone.core.utils

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest


class ModuleUtility(private val context: Context) {
    private var eventListener: ModuleUtilityCallback? = null

    interface ModuleUtilityCallback {
        fun onSuccessListener(value: Int)
        fun onnFailureListener(exception: Exception)
        fun onAlreadyInstalledModule()
    }

    fun setModuleUtilityCallback(event: ModuleUtilityCallback) {
        eventListener = event
    }

    fun doInstallFeatureModule(module: String) {

        if (eventListener == null) throw IllegalArgumentException("event listener have not yet being set")

        val splitInstallManager = SplitInstallManagerFactory.create(context)
        if (splitInstallManager.installedModules.contains(module)) {
            eventListener!!.onAlreadyInstalledModule()

        } else {

            val request = SplitInstallRequest.newBuilder()
                .addModule(module)
                .build()

            splitInstallManager.startInstall(request)
                .addOnSuccessListener { eventListener!!.onSuccessListener(it) }
                .addOnFailureListener { eventListener!!.onnFailureListener(it) }
        }
    }
}