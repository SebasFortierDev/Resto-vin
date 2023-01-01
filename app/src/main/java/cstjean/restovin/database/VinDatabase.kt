package cstjean.restovin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cstjean.restovin.Vin

/**
 * Base de données contenant les vins
 */

@Database(entities = [Vin::class], version = 1, exportSchema = false)
@TypeConverters(VinTypeConverters::class)
abstract class VinDatabase : RoomDatabase() {

    /**
     * Permet de définir le DAO de la base de données
     */
    abstract fun vinDao(): VinDao
}