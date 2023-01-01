package cstjean.restovin

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.math.max
import kotlin.math.min

/**
 * Utilitaire pour les photos
 */

/**
 * Permet d'obtenir le Bitmap de la photo
 *
 * @param path Le path de la photo
 * @param destWidth Le width de la photo
 * @param destHeight Le height de la photo
 *
 * @return La photo décodée
 */
fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
    val bmOptions = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, this)
        val photoW: Int = outWidth
        val photoH: Int = outHeight
        val scaleFactor: Int = max(1, min(photoW / destWidth, photoH / destHeight))
        inJustDecodeBounds = false
        inSampleSize = scaleFactor
    }
    return BitmapFactory.decodeFile(path, bmOptions)
}