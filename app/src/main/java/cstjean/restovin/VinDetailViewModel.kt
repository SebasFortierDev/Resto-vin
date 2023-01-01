package cstjean.restovin

import androidx.lifecycle.*
import java.io.File
import java.util.*

/**
 * ViewModel des détails du vin
 *
 * @property vinRepository Le repository des vins
 * @property vinSelectedIdLiveData L'id du vin selectionnée
 * @property vinLiveData Le vin selectionné
 */
class VinDetailViewModel : ViewModel() {
    private val vinRepository = VinRepository.get()
    val vinSelectedIdLiveData = MutableLiveData<UUID>()

    val vinLiveData: LiveData<Vin?> =
        Transformations.switchMap(vinSelectedIdLiveData) { vinId ->
            vinRepository.getVin(vinId).asLiveData()
        }

    /**
     * Permet de mettre à jour l'ID du vin selectionné
     *
     * @param vinId L'ID du vin selectionné
     */
    fun selectIdVin(vinId: UUID) {
        vinSelectedIdLiveData.value = vinId
    }

    /**
     * Permet de mettre à jour le vin selectionné
     *
     * @param vin Le vin selectionné
     */
    fun saveVin(vin: Vin) {
        vinRepository.updateVin(vin)
    }

    /**
     * Permet de supprimer un vin
     *
     * @param vin Le vin qu'on veut supprimer
     */
    fun supprimerVin(vin: Vin) {
        vinRepository.supprimerVin(vin)
    }

    /**
     * Permet d'obtenir le fichier contenant les photos
     *
     * @return La photo dans le fichier de photo
     */
    fun getPhotoFile(vin: Vin): File {
        return vinRepository.getPhotoFile(vin)
    }
}