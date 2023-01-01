package cstjean.restovin.database

import androidx.room.TypeConverter
import java.util.*

/**
 * Classe permettant de convertir différents types
 */
class VinTypeConverters {

    /**
     * Permet de transformer un UUID en string
     *
     * @param uuid Le UUID dans la base de données
     *
     * @return Un UUID en String
     */
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    /**
     * Permet de créer un UUID à partir d'une String
     *
     * @param uuid Le UUID en String
     *
     * @return Un UUID de type UUID
     */
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
}