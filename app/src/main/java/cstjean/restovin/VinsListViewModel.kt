package cstjean.restovin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

/**
 * ViewModel pour la liste des vins
 *
 * @property vinRepository Repository des vins
 * @property vinsLiveData Livedata des vins
 * @property filtreRechercheLiveData Livedata du filtre de recherche
 * @property position la position dans le carrousel
 */
class VinsListViewModel : ViewModel() {
    private val vinRepository = VinRepository.get()
    val vinsLiveData = vinRepository.getVins().asLiveData()

    val filtreRechercheLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    var position: Int = 0

    /**
     * Permet l'ajout d'un vin
     *
     * @param vin Le vin qu'on veut ajouter
     */
    fun addVin(vin: Vin) {
        vinRepository.addVin(vin)
    }
}