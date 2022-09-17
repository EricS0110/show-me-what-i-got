package com.builditmyself.collectionsview

import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.os.Bundle
import android.system.Os.accept
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.builditmyself.collectionsview.data.Connection
import com.builditmyself.collectionsview.databinding.FragmentUserLoginBinding
import com.builditmyself.collectionsview.model.MongoDataViewModel
import com.builditmyself.collectionsview.model.ConnectionViewModel
import com.builditmyself.collectionsview.model.ConnectionViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.NonCancellable.start


class UserLoginFragment : Fragment() {
    private var _binding: FragmentUserLoginBinding? = null
    private val binding get() = _binding!!
    private val mongoViewModel: MongoDataViewModel by activityViewModels()
    private val connectionViewModel: ConnectionViewModel by activityViewModels {
        ConnectionViewModelFactory(
            (activity?.application as ConnectionApplication).database.connectionDao()
        )
    }
    lateinit var connection: Connection

    private fun isEntryValid(): Boolean {
        return connectionViewModel.isEntryValid(
            binding.userEntryEdit.text.toString(),
            binding.passwordEntryEdit.text.toString()
        )
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
            Toast.makeText(this.context, "Working", Toast.LENGTH_LONG).show()
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