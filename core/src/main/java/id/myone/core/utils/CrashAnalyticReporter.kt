/**
 * Created by Mahmud on 02/02/23.
 * mahmud120398@gmail.com
 */

package id.myone.core.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics


@Suppress("Unused")
class CrashAnalyticReporter(private val crashAnalytic: FirebaseCrashlytics) {

    /**
     * set the log for specific user id
     * this can be used to easy when we track the error in the future
     * @param userId String
     * @return Unit
     */
    fun setCrashAnalyticUserId(userId: String) {
        crashAnalytic.setUserId(userId)
    }

    /**
     * custom key value exception
     * @param key
     * @param value
     * @return Unit
     */
    fun setCustomLog(key: String, value: String) {
        crashAnalytic.setCustomKey(key, value)
    }

    /**
     * report a throwable to the crash analytic
     * @param throws Throwable
     */
    fun recordException(throws: Throwable) {
        crashAnalytic.recordException(throws)
    }

    /**
     *  set crash analytic collection should be disabled or enabled
     *  @param isEnabled: Boolean
     */
    fun setCrashlyticsCollectionStatus(isEnabled: Boolean) {
        crashAnalytic.setCrashlyticsCollectionEnabled(isEnabled)
    }

    /**
     * log a error
     * @param message String
     */
    fun log(message: String) {
        crashAnalytic.log(message)
    }

    companion object {
        @JvmStatic
        fun createCrashAnalyticInstance(): CrashAnalyticReporter {
            val crashAnalytic = FirebaseCrashlytics.getInstance()
            return CrashAnalyticReporter(crashAnalytic)
        }
    }

}