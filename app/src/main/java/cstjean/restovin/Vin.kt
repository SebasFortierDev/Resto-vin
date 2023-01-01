package cstjean.restovin

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


/**
 * Classe Vin
 *
 * @property id L'id du vin (UUID)
 * @property nom Le nom du vin
 * @property typeAlcool Le type de l'alcool
 * @property paysOrigine Le pays d'origine de l'alcool
 * @property producteur Le producteur de l'alcool
 * @property photoFilename Le nom du fichier contenant la photo
 */
@Entity
data class Vin(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var nom: String = "",
    var typeAlcool: String = "",
    var paysOrigine: String = "",
    var producteur: String = ""
) {
    val photoFilename
        get() = "IMG_$id.jpg"

}



