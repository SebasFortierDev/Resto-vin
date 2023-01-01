package cstjean.restovin

import android.app.Application


/**
 *  L'application Vin
 */

class VinApplication : Application() {

    /**
     * Initialisation de l'application Vin
     */
    override fun onCreate() {
        super.onCreate()
        VinRepository.initialize(this)
    }
}