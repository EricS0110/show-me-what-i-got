package com.builditmyself.collectionsview

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.builditmyself.collectionsview.data.SettingsDataStore
import com.builditmyself.collectionsview.model.MongoDataViewModel
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Activity for main application flow
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var settingsDataStore: SettingsDataStore

    private val sharedViewModel: MongoDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        settingsDataStore = SettingsDataStore(this)

        lifecycleScope.launch {
            val username = settingsDataStore.usernameFlow.first()
            val password = settingsDataStore.passwordFlow.first()
            val cluster = settingsDataStore.clusterFlow.first()
            val uri = settingsDataStore.uriFlow.first()
            val database = settingsDataStore.databaseFlow.first()

            if (listOf(username, password, cluster, uri, database).all { it.isNotBlank() }) {
                try {
                    val connectionString =
                        "mongodb+srv://$username:$password@$cluster.$uri.mongodb.net"
                    val client: MongoClient = MongoClients.create(connectionString)
                    sharedViewModel.setConnection(client)
                navController.navigate(R.id.homeFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                    // If connection fails, clear the stored credentials
                    // and navigate to the error fragment
                    settingsDataStore.clearCredentials(context = this@MainActivity)
                    navController.navigate(R.id.connectionErrorFragment)
                }
            } else {
                navController.navigate(R.id.newLoginPage)
            }
        }

        setupActionBarWithNavController(navController)

        val bottomBar = findViewById<LinearLayout>(R.id.bottom_bar)
        val homeButton = findViewById<ImageButton>(R.id.home_button)
        val searchButton = findViewById<ImageButton>(R.id.search_button)

        homeButton.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
        searchButton.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.newLoginPage ||
                destination.id == R.id.connectionErrorFragment) {
                bottomBar.visibility = View.GONE
            } else {
                bottomBar.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Handle navigation when the user chooses Up from the action bar.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}