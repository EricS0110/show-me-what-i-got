package com.builditmyself.collectionsview

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
import com.builditmyself.collectionsview.data.SettingsDataStore
import com.builditmyself.collectionsview.databinding.FragmentUserLoginBinding
import com.builditmyself.collectionsview.model.MongoDataViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch


class UserLoginFragment : Fragment() {
    private var _binding: FragmentUserLoginBinding? = null
    private val binding get() = _binding!!
    private val mongoViewModel: MongoDataViewModel by activityViewModels()

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
    private fun isEntryValid(): Boolean {
        // Get all available fields
        val userName = binding.userEntryEdit.text.toString()
        val userPass = binding.passwordEntryEdit.text.toString()
        val userCluster = binding.clusterEditText.text.toString()
        val userUri = binding.uriEditText.text.toString()
        val userDatabase = binding.databaseEditText.text.toString()

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
        }

        // If defined, check for non-empty-string MongoDB parameters
        // If defined, test connection to MongoDB
        //      if MongoDB connection successful, return true
        //      else notify user to verify provided credentials with alert dialog and return false

        // If not defined, verify remaining credentials not blank, test connection to MongoDB
        //      if MongoDB connection successful, add connection credentials to database
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
//            Toast.makeText(this.context, "Working", Toast.LENGTH_LONG).show()
            if (isEntryValid()) {
                Toast.makeText(this.context, "Good to go forward", Toast.LENGTH_LONG).show()
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

        // Initialize the SettingsDataStore
        loginDataStore = SettingsDataStore(requireContext())
        loginDataStore.usernameFlow.asLiveData().observe(viewLifecycleOwner,
            { value -> storedUsername = value })
        loginDataStore.passwordFlow.asLiveData().observe(viewLifecycleOwner,
            { value -> storedPassword = value})
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}