package com.builditmyself.collectionsview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.builditmyself.collectionsview.data.SettingsDataStore
import com.builditmyself.collectionsview.databinding.FragmentNewLoginPageBinding
import com.builditmyself.collectionsview.model.MongoDataViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import kotlinx.coroutines.launch

class NewLoginPage : Fragment() {
    private var _binding: FragmentNewLoginPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingsDataStore: SettingsDataStore
    private var storedUsername: String? = null
    private var storedPassword: String? = null
    private var storedCluster: String? = null
    private var storedUri: String? = null
    private var storedDatabase: String? = null

    private val sharedViewModel: MongoDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewLoginPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the SettingsDataStore
        settingsDataStore = SettingsDataStore(requireContext())
        settingsDataStore.usernameFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            storedUsername = value
        }
        settingsDataStore.passwordFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            storedPassword = value
        }
        settingsDataStore.clusterFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            storedCluster = value
        }
        settingsDataStore.uriFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            storedUri = value
        }
        settingsDataStore.databaseFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            storedDatabase = value
        }

        view.findViewById<ImageView>(R.id.new_login_help).setOnClickListener {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle(resources.getString(R.string.mongo_help_title))
                .setMessage(resources.getString(R.string.mongo_help_text))
                .show()
        }

        view.findViewById<Button>(R.id.connect_button).setOnClickListener {
            val username = binding.newUserEntryEdit.text.toString()
            val password = binding.newPasswordEntryEdit.text.toString()
            val cluster = binding.clusterEditText.text.toString()
            val uri = binding.newUriEditText.text.toString()
            val database = binding.newDatabaseEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank() && cluster.isNotBlank() &&
                uri.isNotBlank() && database.isNotBlank()) {
                // Try to connect to MongoDB with the provided credentials
                // If the connection is successful, store the credentials
                // and save the MongoDB client to the sharedViewModel
                try {
                    val connectionString =
                        "mongodb+srv://$username:$password@$cluster.$uri.mongodb.net"
                    val client: MongoClient = MongoClients.create(connectionString)
                    sharedViewModel.setConnection(client)

                    // Save the credentials to the data store
                    viewLifecycleOwner.lifecycleScope.launch {
                        settingsDataStore.saveCredentials(
                            username = username,
                            password = password,
                            cluster = cluster,
                            uri = uri,
                            database = database,
                            context = requireContext()
                        )
                    }

                    // Navigate to the home fragment
                    requireActivity().supportFragmentManager.popBackStack()
                } catch (e: Exception) {
                    e.printStackTrace()
                    MaterialAlertDialogBuilder(this.requireContext())
                        .setTitle(resources.getString(R.string.connection_error_title))
                        .setMessage(resources.getString(R.string.connection_error_message))
                        .show()
                }

            } else {
                MaterialAlertDialogBuilder(this.requireContext())
                    .setTitle(resources.getString(R.string.login_entry_error_title))
                    .setMessage(resources.getString(R.string.login_entry_error_message))
                    .show()
            }
        }
    }
}