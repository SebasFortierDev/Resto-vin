package cstjean.restovin

import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview
import java.io.File

/**
 * Fragment pour la liste des vins.
 *
 * @property vinsListViewModel ViewModel pour la liste des vins.
 * @property vinDetailViewModel ViewModel pour les détails d'un vin.
 * @property vinsRecyclerView RecyclerView de la liste des vins.
 * @property rechercheEditText Champ input pour modifier la valeur du filtre
 * @property derniereTailleListe Derniere taille de la liste de vins (avec ou sans filtre)
 * @property adapter Voir [vinsRecyclerView].
 */
class VinsListFragment : Fragment() {

    private val vinsListViewModel: VinsListViewModel by activityViewModels()
    private val vinDetailViewModel: VinDetailViewModel by activityViewModels()

    private lateinit var vinsRecyclerView: CarouselRecyclerview
    private lateinit var rechercheEditText: EditText

    private var derniereTailleListe: Int = 0

    private var adapter: VinAdapter? = VinAdapter(emptyList())

    /**
     * Initialisation du Fragment.
     *
     * @param savedInstanceState Les données conservées au changement d'état.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**
     * Instanciation de la vue.
     *
     * @param inflater Pour instancier la vue.
     * @param container Le parent qui contiendra notre vue.
     * @param savedInstanceState Les données conservées au changement d'état.
     *
     * @return La vue instanciée.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vins_liste, container, false)
        vinsRecyclerView = view.findViewById(R.id.vins_recycler_view)

        rechercheEditText = view.findViewById(R.id.recherche)

        vinsRecyclerView.adapter = adapter
        vinsRecyclerView.set3DItem(true)
        vinsRecyclerView.setAlpha(true)

        return view
    }

    /**
     * Démarrage du Fragment.
     */
    override fun onStart() {
        super.onStart()

        rechercheEditText.text.clear()

        val rechercheWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // ...
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vinsListViewModel.filtreRechercheLiveData.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // ...
            }
        }
        rechercheEditText.addTextChangedListener(rechercheWatcher)
    }

    /**
     * Fonction s'exécutant après la création de la vue du fragment
     *
     * @param view La vue qu'on affiche
     * @param savedInstanceState Les données conservées au changement d'état
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vinsListViewModel.filtreRechercheLiveData.observe(
            viewLifecycleOwner,
            { filtre ->
                filtre?.let {
                    appliqueFiltreRecherche(filtre)
                }
            }
        )

        vinsListViewModel.vinsLiveData.observe(
            viewLifecycleOwner,
            { vins ->
                vins?.let {
                    updateUI(vins)
                }
            }
        )
    }

    /**
     * Permet d'appliquer le filtre entrée dans le champ de la recherche
     *
     * Retourne les vins qui contiennent le filtre dans leur nom
     *
     * @param filtre le filtre entré dans le champ de la recherche
     */
    private fun appliqueFiltreRecherche(filtre: String) {
        val vins = vinsListViewModel.vinsLiveData.value
        val vinsFiltre = mutableListOf<Vin>()

        if (vins.isNullOrEmpty()) {
            return
        }

        if (filtre.isNotEmpty()) {
            vins.forEach {
                if (it.nom.lowercase().contains(filtre.lowercase())) {
                    vinsFiltre.add(it)
                }
            }

            if (vinsFiltre.size != derniereTailleListe) {
                updateUI(vinsFiltre, false)
                derniereTailleListe = vinsFiltre.size
            } else {
                updateUI(vinsFiltre)
            }
        } else {
            if (vins.size != derniereTailleListe) {
                updateUI(vins, false)
                derniereTailleListe = vins.size
            } else {
                updateUI(vins)
            }
        }
    }

    /**
     * Met à j our la vue en fonction des données du ViewModel.
     *
     * @param vins La liste des vins
     * @param scroll si on scroll on non
     */
    private fun updateUI(vins: List<Vin>, scroll: Boolean = true) {
        adapter = VinAdapter(vins)
        vinsRecyclerView.adapter = adapter

        if (scroll) {
            if (vinsListViewModel.position > vins.size) {
                vinsListViewModel.position = 0
            }
            vinsRecyclerView.scrollToPosition(vinsListViewModel.position)
        }
    }

    /**
     * Classe VinHolder
     *
     * @param view La vue
     *
     * @property vin Le vin associé au ViewHolder.
     * @property photoFile Le fichier de photo
     * @property nomText Le TextView affichant le nom du vin
     * @property typeAlcoolText Le TextView affichant le type du vin
     * @property paysOrigineText Le TextView affichant le pays d'origine du vin
     * @property producteurText Le TextView affichant le nom du producteur
     * @property photoView Le ImageView affichant la photo du vin
     */
    private inner class VinHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private lateinit var vin: Vin
        private lateinit var photoFile: File

        val nomText: TextView = view.findViewById(R.id.nom_produit)
        val typeAlcoolText: TextView = view.findViewById(R.id.type_alcool)
        val paysOrigineText: TextView = view.findViewById(R.id.pays_origine)
        val producteurText: TextView = view.findViewById(R.id.producteur)

        val photoView: ImageView = view.findViewById(R.id.demo_produit)

        init {
            itemView.setOnClickListener(this)
        }

        /**
         * @param vin Le vin à associer au ViewHolder.
         */
        fun bind(vin: Vin) {
            this.vin = vin

            nomText.text = vin.nom
            typeAlcoolText.text = vin.typeAlcool
            paysOrigineText.text = vin.paysOrigine
            producteurText.text = vin.producteur

            photoFile = vinDetailViewModel.getPhotoFile(vin)
            updatePhotoView()
        }

        /**
         * Event click sur un ViewHolder.
         *
         * @param v La vue cliquée.
         */
        override fun onClick(v: View?) {
            vinsListViewModel.position = this.adapterPosition
            vinDetailViewModel.selectIdVin(vin.id)
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
    }

    /**
     * Classe VinAdapter
     *
     * @property vins La liste des vins pour l'Adapter du RecyclerView.
     */
    private inner class VinAdapter(var vins: List<Vin>) : RecyclerView.Adapter<VinHolder>() {

        /**
         * Création du ViewHolder
         *
         * @param parent Le parent où ajouter notre ViewHolder.
         * @param viewType Le type de la nouvelle vue.
         *
         * @return Le ViewHolder instancié.
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VinHolder {
            val view = layoutInflater.inflate(R.layout.liste_item_vin, parent, false)
            return VinHolder(view)
        }

        /**
         * Bind sur le ViewHolder selon la position
         *
         * @param holder Le ViewHolder qui est bindé.
         * @param position La position à charger.
         */
        override fun onBindViewHolder(holder: VinHolder, position: Int) {
            val vin: Vin = vins[position]
            holder.bind(vin)
        }

        /**
         * La quantité de données dans le RecyclerView.
         *
         * @return le nombre de vins dans la liste
         */
        override fun getItemCount(): Int = vins.size
    }


    /**
     * Initialise les options dans le menu de l'application
     *
     * @param menu Le menu de l'application
     * @param inflater Pour instancier le menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_vins_list, menu)
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
            R.id.nouveau_vin -> {
                val vin = Vin()
                val vins = vinsListViewModel.vinsLiveData.value
                if (!vins.isNullOrEmpty()) {
                    vinsListViewModel.position = vins.size
                } else {
                    vinsListViewModel.position = 0
                }
                vinsListViewModel.addVin(vin)

                vinDetailViewModel.selectIdVin(vin.id)
                rechercheEditText.text.clear()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}