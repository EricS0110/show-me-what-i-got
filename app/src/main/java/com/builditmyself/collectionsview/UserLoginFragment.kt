package com.builditmyself.collectionsview

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ReturnThis
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.dataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.builditmyself.collectionsview.data.SettingsDataStore
import com.builditmyself.collectionsview.databinding.FragmentUserLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch


class UserLoginFragment : Fragment() {
    private var _binding: FragmentUserLoginBinding? = null
    private val binding get() = _binding!!

    // Track user settings/preferences/inputs/whatever-you-want-to-call-it
    private lateinit var loginDataStore: SettingsDataStore
    private var storedUsername: String = ""
    private var storedPassword: String = ""
    private var storedCluster: String = ""
    private var storedUri: String = ""
    private var storedDatabase: String = ""

    /////////////////////////////////////////////
    //
    //
    // Input validation options
    //
    //
    /////////////////////////////////////////////
    private fun validUserAndPassword(): Boolean {
        // Get all available fields
        val userName = binding.userEntryEdit.text.toString()
        val userPass = binding.passwordEntryEdit.text.toString()

        // Only proceed if User & Password not blank
        if (userName.isBlank() || userPass.isBlank()) {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle(resources.getString(R.string.invalid_user_or_pass))
                .setMessage(resources.getString(R.string.invalid_user_pass_message))
                .show()
            return false
        }
        // Check if username and password defined in preferences
        if (storedUsername == "" || storedPassword == "") {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle("Cached value updates")
                .setMessage("Updating stored username and password")
                .show()
            lifecycleScope.launch {
                loginDataStore.saveUsernameToDataStore(usernameString = userName, requireContext())
            }
            lifecycleScope.launch {
                loginDataStore.savePasswordToDataStore(passwordString = userPass, requireContext())
            }
            return false
        }
        if (storedUsername != userName || storedPassword != userPass) {
            val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_NEGATIVE -> {
                        Toast.makeText(this.context, "Values not overwritten, please check entered text", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    DialogInterface.BUTTON_POSITIVE -> {
                        lifecycleScope.launch {
                            loginDataStore.saveUsernameToDataStore(usernameString = userName, requireContext())
                            loginDataStore.savePasswordToDataStore(passwordString = userPass, requireContext())
                        }
                        Toast.makeText(this.context, "New values are saved", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                }
            }

            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle("Credentials mismatch")
                .setMessage("Cached values do not match input\n" +
                        "CLEAR VALUES AFTER INPUT!!!")
                .setNegativeButton("Cancel", dialogClickListener)
                .setPositiveButton("Overwrite", dialogClickListener)
                .show()
            return false
        }
        return true
    }

    private fun filledMongoCredentials(): Boolean {
        // Get all available fields
        val userCluster = binding.clusterEditText.text.toString()
        val userUri = binding.uriEditText.text.toString()
        val userDatabase = binding.databaseEditText.text.toString()

        // Check if all values are empty
        val allStoredEmpty = (storedCluster == "" && storedUri == "" && storedDatabase == "")
        val allInputBlank = (userCluster.isBlank() && userUri.isBlank() && userDatabase.isBlank())
        val allStoredFull = (storedCluster != "" && storedUri != "" && storedDatabase != "")
        val minOneInputFilled = (userCluster.isNotBlank() || userUri.isNotBlank() || userDatabase.isNotBlank())
        val anyStoredEmpty = (storedCluster == "" || storedUri == "" || storedDatabase == "")
        val anyInputsMismatchStored = (userCluster != storedCluster || userUri != storedUri || userDatabase != storedDatabase)
        if (allStoredEmpty && allInputBlank) {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle("Alert!")
                .setMessage("The MongoDB credentials are empty and none are provided!  " +
                        "Please enter values as defined for your MongoDB instance.")
                .show()
            return false
        }
        if (allStoredEmpty && minOneInputFilled) {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle("Notice - ")
                .setMessage("Updating stored MongoDB Credentials \n\n" +
                        "PLEASE NOTE YOU WILL NEED TO TRY TO LOG IN AGAIN TO PROCEED!")
                .show()
            lifecycleScope.launch {
                loginDataStore.saveClusterToDataStore(clusterString = userCluster, requireContext())
                loginDataStore.saveUriToDataStore(uriString = userUri, requireContext())
                loginDataStore.saveDatabaseToDataStore(dbString = userDatabase, requireContext())
            }
            return false
        }
        if (anyStoredEmpty) {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle("Alert!")
                .setMessage("One or more MongoDB Cred is empty...\n" +
                        "Stored Cluster: '$storedCluster'\n" +
                        "Stored URI: '$storedUri'\n" +
                        "Stored Database: '$storedDatabase'\n\n" +
                        "CHECKING THE MISSING VALUE(S)...\n\n" +
                        "YOU WILL NEED TO CLICK LOGIN TO PROCEED")
                .show()
            if (userCluster.isNotBlank()) {
                lifecycleScope.launch {
                    loginDataStore.saveClusterToDataStore(clusterString = userCluster, requireContext())
                }
            }
            if (userUri.isNotBlank()) {
                lifecycleScope.launch {
                    loginDataStore.saveUriToDataStore(uriString = userUri, requireContext())
                }
            }
            if (userDatabase.isNotBlank()) {
                lifecycleScope.launch {
                    loginDataStore.saveDatabaseToDataStore(dbString = userDatabase, requireContext())
                }
            }
            return false
        }
        if (allStoredFull && minOneInputFilled && anyInputsMismatchStored) {
            val mongoCredsClickListener = DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_NEGATIVE -> {
                        Toast.makeText(this.context, "Values not overwritten, please check entered text", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    DialogInterface.BUTTON_POSITIVE -> {
                        if (userCluster.isNotBlank() && userCluster != storedCluster) {
                            lifecycleScope.launch {
                                loginDataStore.saveClusterToDataStore(clusterString = userCluster, requireContext())
                            }
                        }
                        if (userUri.isNotBlank() && userUri != storedUri) {
                            lifecycleScope.launch {
                                loginDataStore.saveUriToDataStore(uriString = userUri, requireContext())
                            }
                        }
                        if (userDatabase.isNotBlank() && userDatabase != storedDatabase) {
                            lifecycleScope.launch {
                                loginDataStore.saveDatabaseToDataStore(dbString = userDatabase, requireContext())
                            }
                        }
                        Toast.makeText(this.context, "New values are saved", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                }
            }

            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle("MongoDB creds mismatch")
                .setMessage("Cached values do not match input")
                .setNegativeButton("Cancel", mongoCredsClickListener)
                .setPositiveButton("Overwrite", mongoCredsClickListener)
                .show()
            return false
        }
        return true
    }

    private fun determineNextSteps(): Boolean {
        // Get all available fields
        val userName = binding.userEntryEdit.text.toString()
        val userPass = binding.passwordEntryEdit.text.toString()
        val userCluster = binding.clusterEditText.text.toString()
        val userUri = binding.uriEditText.text.toString()
        val userDatabase = binding.databaseEditText.text.toString()

        // Logic variables for cleaner IF tree
        val userAndPassEntered = (userName != "" && userPass != "")
        val userAndPassCached = (storedUsername != "" && storedPassword != "")

        if (!userAndPassEntered) {
            Toast.makeText(this.context, "Username or password not provided, please ensure both are entered", Toast.LENGTH_SHORT).show()
            return false
        }
        else {
            if (!userAndPassCached) {
                Toast.makeText(this.context, "nothing stored, caching the new values", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "values already cached, comparing the entered values", Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }


    /////////////////////////////////////////////
    //
    //
    // Android Application flow functions
    //
    //
    /////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.login_button).setOnClickListener() {
            if (determineNextSteps()){
                Toast.makeText(this.context, "Made it through logic tree", Toast.LENGTH_LONG).show()
            }

//            val allCredentialsCached = (storedUsername != "" && storedPassword != "" && storedCluster != "" && storedUri != "" && storedDatabase != "")
//            //
//            // TODO: Figure out a way to check MongoDB connection is valid with Python, only proceed if that check passes
//            //
//            if (allCredentialsCached) {
//                Toast.makeText(this.context, "All creds stored, checking if valid", Toast.LENGTH_SHORT).show()
//            }
//            if (validUserAndPassword() && filledMongoCredentials()) {
//                Toast.makeText(this.context, "Good to go forward", Toast.LENGTH_LONG).show()
//            }
        }
        view.findViewById<ImageView>(R.id.help_button_1).setOnClickListener() {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle(resources.getString(R.string.mongo_help_title_1))
                .setMessage(resources.getString(R.string.mongo_user_credentials))
                .show()
        }
        view.findViewById<ImageView>(R.id.help_button_2).setOnClickListener() {
            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle(resources.getString(R.string.mongo_help_title_1))
                .setMessage(resources.getString(R.string.mongo_generalized_string))
                .show()
        }

        // Initialize the SettingsDataStore
        loginDataStore = SettingsDataStore(requireContext())
        loginDataStore.usernameFlow.asLiveData().observe(viewLifecycleOwner, { value -> storedUsername = value })
        loginDataStore.passwordFlow.asLiveData().observe(viewLifecycleOwner, { value -> storedPassword = value })
        loginDataStore.clusterFlow.asLiveData().observe(viewLifecycleOwner, { value -> storedCluster = value })
        loginDataStore.uriFlow.asLiveData().observe(viewLifecycleOwner, { value -> storedUri = value })
        loginDataStore.dbFlow.asLiveData().observe(viewLifecycleOwner, { value -> storedDatabase = value })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}