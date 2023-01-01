package cstjean.restovin.database

import androidx.room.*
import cstjean.restovin.Vin
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Interface VinDao
 */
@Dao
interface VinDao {
    /**
     * Permet d'obtenir tous les vins de la base de données
     *
     * @return La liste des vins dans la base de données
     */
    @Query("SELECT * FROM vin")
    fun getVins(): Flow<List<Vin>>

    /**
     * Permet d'obtenir un vin dans la base de données selon son ID
     *
     * @param id L'id du vin qu'on veut obtenir
     *
     * @return Le vin selon l'id donné
     */
    @Query("SELECT * FROM vin WHERE id=(:id)")
    fun getVin(id: UUID): Flow<Vin?>

    /**
     * Permet d'ajouter un vin dans la base de données
     *
     * @param vin Le vin qu'on veut ajouter
     */
    @Insert
    fun addVin(vin: Vin)

    /**
     * Permet d'updater un vin déjà présent dans la base de données
     *
     * @param vin Le vin qu'on veut updater
     */
    @Update
    fun updateVin(vin: Vin)

    /**
     * Permet de supprimer un vin dans la base de données
     *
     * @param vin Le vin qu'on veut supprimer
     */
    @Delete
    fun deleteVin(vin: Vin)
}