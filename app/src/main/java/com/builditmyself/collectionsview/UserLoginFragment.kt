package com.builditmyself.collectionsview

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.builditmyself.collectionsview.data.SettingsDataStore
import com.builditmyself.collectionsview.databinding.FragmentUserLoginBinding
import com.builditmyself.collectionsview.model.MongoDataViewModel
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

    private val sharedViewModel: MongoDataViewModel by activityViewModels()

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
        val mongoCredentialsEntered = (userCluster != "" && userDatabase != "" && userUri != "")
        val mongoCredentialsCached = (storedCluster != "" && storedDatabase != "" && storedUri != "")


        if (!userAndPassEntered) {
            Toast.makeText(this.context, "Username or password not provided, please ensure both are entered", Toast.LENGTH_SHORT).show()
            return false
        }
        else {
            if (!userAndPassCached && !mongoCredentialsEntered && !mongoCredentialsCached) {
                lifecycleScope.launch {
                    loginDataStore.saveUsernameToDataStore(usernameString = userName, requireContext())
                    loginDataStore.savePasswordToDataStore(passwordString = userPass, requireContext())
                }
                Toast.makeText(this.context, "Username and password now cached, please enter database credentials below", Toast.LENGTH_SHORT).show()
                return false
            }
            if (!userAndPassCached && mongoCredentialsEntered && !mongoCredentialsCached) {
                lifecycleScope.launch {
                    loginDataStore.saveUsernameToDataStore(usernameString = userName, requireContext())
                    loginDataStore.savePasswordToDataStore(passwordString = userPass, requireContext())
                    loginDataStore.saveClusterToDataStore(clusterString = userCluster, requireContext())
                    loginDataStore.saveUriToDataStore(uriString = userUri, requireContext())
                    loginDataStore.saveDatabaseToDataStore(dbString = userDatabase, requireContext())
                }
                return true
            }
            if (userAndPassCached && !mongoCredentialsEntered && !mongoCredentialsCached) {
                if (validUserAndPassword()) {
                    Toast.makeText(
                        this.context,
                        "Username and password match cache, but database credentials missing",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return false
            }
            if (userAndPassCached && !mongoCredentialsEntered && mongoCredentialsCached) {
                return validUserAndPassword()
            }
            if (userAndPassCached && mongoCredentialsEntered && !mongoCredentialsCached) {
                if (validUserAndPassword()) {
                    lifecycleScope.launch {
                        loginDataStore.saveClusterToDataStore(clusterString = userCluster, requireContext())
                        loginDataStore.saveUriToDataStore(uriString = userUri, requireContext())
                        loginDataStore.saveDatabaseToDataStore(dbString = userDatabase, requireContext())
                    }
                    Toast.makeText(this.context, "Database credentials cached, attempting login", Toast.LENGTH_SHORT).show()
                    return true
                }
                else {
                    Toast.makeText(this.context, "Database credentials not cached, please provide correct username and password", Toast.LENGTH_SHORT).show()
                    return false
                }
            }
            if (userAndPassCached && mongoCredentialsEntered && mongoCredentialsCached) {
                return (validUserAndPassword() && filledMongoCredentials())
            }
        }

        return true
    }

    private fun validMongoConnection(): Boolean {
        val pythonInstance = sharedViewModel.pythonInstance.value
        val pyModule = pythonInstance!!.getModule("mongo-interface")
        val mongoInterface = pyModule.callAttr("get_mongo_connection", storedUsername, storedPassword, storedCluster, storedDatabase, storedUri)
        val mongoRawTestResult = pyModule.callAttr("test_mongo_connection", mongoInterface)
        val mongoTestResult = mongoRawTestResult.toInt()
        return if (mongoTestResult >= 0) {
            sharedViewModel.setMongoInterface(mongoInterface)
            true
        } else {
            false
        }
    }


    /////////////////////////////////////////////
    //
    //
    // Android Application flow functions
    //
    //
    /////////////////////////////////////////////
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the SettingsDataStore
        loginDataStore = SettingsDataStore(requireContext())
        loginDataStore.usernameFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            storedUsername = value
        }
        loginDataStore.passwordFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            storedPassword = value
        }
        loginDataStore.clusterFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            storedCluster = value
        }
        loginDataStore.uriFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            storedUri = value
        }
        loginDataStore.dbFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            storedDatabase = value
        }

        view.findViewById<Button>(R.id.login_button).setOnClickListener() {
            if (determineNextSteps() && validMongoConnection()){
                findNavController().navigate(R.id.action_userLogin_to_collectionSelectionFragment)
            }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}