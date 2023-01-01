package cstjean.restovin

import android.content.Context
import androidx.room.Room
import java.util.*
import java.util.concurrent.Executors
import cstjean.restovin.database.VinDatabase
import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * Permet de définir le nom de la base de données de l'application
 */
private const val DATABASE_NAME = "vin-database"

/**
 * Repository de la base de données
 *
 * @param context Le contexte de l'application
 *
 * @property database La base de données de l'application
 * @property executor Executor du repository
 * @property vinDao Le DAO de la base de données
 * @property filesDir Le dossier contenant les photos
 */
class VinRepository private constructor(context: Context) {

    private val database: VinDatabase = Room.databaseBuilder(
        context.applicationContext,
        VinDatabase::class.java,
        DATABASE_NAME
    )
        .build()

    private val executor = Executors.newSingleThreadExecutor()
    private val vinDao = database.vinDao()

    private val filesDir = context.applicationContext.filesDir

    /**
     * Permet d'obtenir le fichier contenant les photos
     */
    fun getPhotoFile(vin: Vin): File = File(filesDir, vin.photoFilename)

    /**
     * Permet de faire le lien avec le DAO et la base de données pour la fonction getVins
     */
    fun getVins(): Flow<List<Vin>> = vinDao.getVins()

    /**
     * Permet de faire le lien avec le DAO et la base de données pour la fonction getVin
     */
    fun getVin(id: UUID): Flow<Vin?> = vinDao.getVin(id)

    /**
     * Permet de faire le lien avec le DAO et la base de données pour la méthode addVin
     */
    fun addVin(vin: Vin) {
        executor.execute {
            vinDao.addVin(vin)
        }
    }

    /**
     * Permet de faire le lien avec le DAO et la base de données pour la méthode updateVin
     */
    fun updateVin(vin: Vin) {
        executor.execute {
            vinDao.updateVin(vin)
        }
    }

    /**
     * Permet de faire le lien avec le DAO et la base de données pour la méthode supprimerVin
     */
    fun supprimerVin(vin: Vin) {
        executor.execute {
            vinDao.deleteVin(vin)
        }
    }

    /**
     * Companion object contenant l'instance du repository de la base de données
     * Singleton
     *
     * @property INSTANCE l'instance du repository
     */
    companion object {
        private var INSTANCE: VinRepository? = null

        /**
         * Permet d'initialiser le repository
         *
         * @param context Le context de l'application
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = VinRepository(context)
            }
        }

        /**
         * Permet d'obtenir l'instance du repository
         */
        fun get(): VinRepository {
            return INSTANCE ?: throw IllegalStateException("VinRepository must be initialized.")
        }
    }
}