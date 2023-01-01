package cstjean.restovin

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import java.io.File

/**
 * Nom de l'autorité de l'application
 */
private const val AUTHORITY = "cstjean.restovin.fileprovider"

/**
 * Fragment pour un vin
 *
 * @property vinsListViewModel Le viewModel de la liste de vins
 * @property vinDetailViewModel Le viewModel du détail du vin
 * @property partageEstCharge Indique si le bouton partager est prêt à être modifier
 * @property vin Le vin qu'on affiche
 * @property nomEditText Champ modifiable affichant le nom du vin
 * @property typeAlcoolEditText Champ modifiable affichant le type d'alcool du vin
 * @property paysOrigineEditText Champ modifiable affichant le pays d'origine du vin
 * @property producteurEditText Champ modifiable affichant le producteur du vin
 * @property enregistrerButton Bouton permettant d'enregistrer le vin
 * @property partagerButton Bouton du menu permettant de partager un vin
 * @property photoButton Bouton qui permet d'ouvrir l'appareil photo
 * @property photoView Vue affichant la photo du vin
 * @property photoFile Le nom du fichier de la photo
 * @property photoUri URI pour accèder à la photo
 * @property getPhoto Permet d'obtenir la photo
 */
class VinFragment : Fragment() {
    private val vinsListViewModel: VinsListViewModel by activityViewModels()
    private val vinDetailViewModel: VinDetailViewModel by activityViewModels()

    private var partageEstCharge: Boolean = false

    private lateinit var vin: Vin

    private lateinit var nomEditText: EditText
    private lateinit var typeAlcoolEditText: EditText
    private lateinit var paysOrigineEditText: EditText
    private lateinit var producteurEditText: EditText

    private lateinit var enregistrerButton: Button
    private lateinit var partagerButton: MenuItem

    private lateinit var photoButton: ImageButton
    private lateinit var photoView: ImageView

    private lateinit var photoFile: File
    private lateinit var photoUri: Uri


    private val getPhoto =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            requireActivity().revokeUriPermission(
                photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            if (result.resultCode == Activity.RESULT_OK) {
                updatePhotoView()
            }
        }

    /**
     * Initialisation du Fragment.
     *
     * @param savedInstanceState Les données conservées au changement d'état.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        vin = Vin()
    }

    /**
     * Instanciation de la vue
     *
     * @param inflater Pour instancier la vue.
     * @param container Le parent qui contiendra notre interface.
     * @param savedInstanceState Les données conservées au changement d'état.
     *
     * @return La vue instanciée.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vin, container, false)

        nomEditText = view.findViewById(R.id.nom_produit)
        typeAlcoolEditText = view.findViewById(R.id.type_alcool)
        paysOrigineEditText = view.findViewById(R.id.pays_origine)
        producteurEditText = view.findViewById(R.id.producteur)

        enregistrerButton = view.findViewById(R.id.btn_ajouter)

        photoButton = view.findViewById(R.id.vin_camera)
        photoView = view.findViewById(R.id.vin_photo)

        return view
    }

    /**
     * Démarrage du Fragment.
     */
    override fun onStart() {
        super.onStart()

        val packageManager = activity?.packageManager

        enregistrerButton.setOnClickListener {
            vinDetailViewModel.saveVin(vin)

            setFragmentResult(SAVE_RESULT, bundleOf())
        }


        /* Lancement de l'activité pour la prise de photo */
        photoButton.apply {
            vinDetailViewModel.saveVin(vin)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (packageManager != null && intent.resolveActivity(packageManager) != null) {
                setOnClickListener {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    val cameraActivities: List<ResolveInfo> =
                        packageManager.queryIntentActivities(
                            intent,
                            PackageManager.MATCH_DEFAULT_ONLY
                        )
                    for (cameraActivity in cameraActivities) {
                        requireActivity().grantUriPermission(
                            cameraActivity.activityInfo.packageName,
                            photoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                    }
                    getPhoto.launch(intent)
                }
            } else {
                isEnabled = false
            }
        }

        val nomWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Vide
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vin.nom = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // Vide
            }
        }

        val typeWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Vide
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vin.typeAlcool = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // Vide
            }
        }

        val paysWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Vide
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vin.paysOrigine = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // Vide
            }
        }

        val producteurWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Vide
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vin.producteur = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // vide
            }
        }


        nomEditText.addTextChangedListener(nomWatcher)
        typeAlcoolEditText.addTextChangedListener(typeWatcher)
        paysOrigineEditText.addTextChangedListener(paysWatcher)
        producteurEditText.addTextChangedListener(producteurWatcher)
    }

    /**
     * Fonction s'exécutant après la création de la vue du fragment
     *
     * @param view La vue qu'on affiche
     * @param savedInstanceState Les données conservées au changement d'état
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        vinDetailViewModel.vinLiveData.observe(
            viewLifecycleOwner,
            { vin ->
                vin?.let {
                    this.vin = vin
                    photoFile = vinDetailViewModel.getPhotoFile(vin)
                    photoUri = FileProvider.getUriForFile(
                        requireActivity(),
                        AUTHORITY,
                        photoFile
                    )
                    updateUI()
                    updateBoutonPartager()
                }
            }
        )
    }

    /**
     * Permet de retirer la permission à notre fichier.
     */
    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    /**
     * Companion object contenant la constante permettant de sauvegarder le résultat
     */
    companion object {
        const val SAVE_RESULT = "vinfragmentsupprimer"
    }


    /**
     * Permet d'update l'interface de l'usager
     *
     * S'occupe d'actualiser les informations lors de changements
     */
    private fun updateUI() {
        nomEditText.setText(vin.nom)
        typeAlcoolEditText.setText(vin.typeAlcool)
        paysOrigineEditText.setText(vin.paysOrigine)
        producteurEditText.setText(vin.producteur)

        updatePhotoView()
    }

    /**
     * Initialise les options dans le menu de l'application
     *
     * @param menu Le menu de l'application
     * @param inflater Pour instancier le menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_vin, menu)

        partagerButton = menu.findItem(R.id.partager)
        updateBoutonPartager()
    }

    /**
     * Permet de définir l'action lorsqu'on appuie sur un certain élément du menu
     *
     * @param item L'item du menu qu'on a cliqué
     *
     * @return True si l'opération a réussi
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.supprimer -> {
                val diaBox = confirmationSuppression()
                vinsListViewModel.position--
                diaBox.show()
                true
            }
            R.id.partager -> {
                partagerPhoto()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /**
     * Permet de partager la photo du vin
     */
    private fun partagerPhoto() {
        val packageManager = activity?.packageManager

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpg"
            putExtra(
                Intent.EXTRA_STREAM,
                photoUri,
            )
        }

        if (packageManager != null && intent.resolveActivity(packageManager) != null) {
            val chooserIntent = Intent.createChooser(intent, null)

            val shareActivities: List<ResolveInfo> =
                packageManager.queryIntentActivities(
                    chooserIntent,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            for (shareActivity in shareActivities) {
                requireActivity().grantUriPermission(
                    shareActivity.activityInfo.packageName,
                    photoUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            startActivity(chooserIntent)
        }
    }

    /**
     * Permet de mettre à jour la view contenant la photo si une photo est associé au vin
     * Si aucune photo est existante, on met une photo par défaut.
     */
    private fun updatePhotoView() {
        if (photoFile.exists()) {
            val size = Point()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val display = activity?.display
                display?.getRealSize(size)
            } else {
                @Suppress("DEPRECATION")
                activity?.windowManager?.defaultDisplay?.getSize(size)
            }
            photoView.setImageBitmap(getScaledBitmap(photoFile.path, size.x, size.y))
        } else {
            photoView.setImageResource(R.drawable.ic_baseline_wine_bar_24)
        }
    }

    /**
     * Permet de rendre le bouton partager visible si le vin
     * contient une photo. Il devient invisible si celui-ci n'a
     * que la photo de base ou si aucune application sur le téléphone
     * permet de partager une photo.
     *
     * Vérifie que le bouton partager est chargé.
     *
     */
    private fun updateBoutonPartager() {
        if (!partageEstCharge) {
            partageEstCharge = true
            return
        }

        partagerButton.isVisible = if (photoFile.exists()) {
            val packageManager = activity?.packageManager
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/jpg"
                putExtra(
                    Intent.EXTRA_STREAM,
                    photoUri,
                )
            }
            packageManager != null && intent.resolveActivity(packageManager) != null
        } else {
            false
        }
    }

    /**
     * Création de la boite de dialogue pour confirmer la supression d'un vin
     *
     * @return La boite de dialogue
     */
    private fun confirmationSuppression(): AlertDialog {
        return AlertDialog.Builder(this.context)
            .setTitle(getString(R.string.supprimer_dialog_titre))
            .setMessage(getString(R.string.supprimer_dialog_message))
            .setPositiveButton(
                getString(R.string.supprimer_dialog_positif)
            ) { dialog, _ ->
                vinDetailViewModel.supprimerVin(vin)

                if (photoFile.exists()) {
                    photoFile.delete()
                }

                setFragmentResult(SAVE_RESULT, bundleOf())
                dialog.dismiss()
            }
            .setNegativeButton(
                getString(R.string.supprimer_dialog_negatif)
            ) { dialog, _ -> dialog.dismiss() }
            .create()
    }
}