package cstjean.restovin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

/**
 * Activity principal qui contient les fragments.
 *
 * @property vinDetailViewModel Le viewmodel des détails du vin
 */
class MainActivity : AppCompatActivity() {

    private val vinDetailViewModel: VinDetailViewModel by viewModels()

    /**
     * Initialisation de l'Activity.
     *
     * @param savedInstanceState Les données conservées au changement d'état.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = VinsListFragment()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }

        vinDetailViewModel.vinSelectedIdLiveData.observe(this) {
            val fragment = VinFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        supportFragmentManager
            .setFragmentResultListener(VinFragment.SAVE_RESULT, this) { _, _ ->
                supportFragmentManager.popBackStack()
            }

    }
}