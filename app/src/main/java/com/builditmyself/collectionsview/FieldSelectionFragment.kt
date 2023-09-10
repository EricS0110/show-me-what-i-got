package com.builditmyself.collectionsview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.builditmyself.collectionsview.databinding.FragmentFieldSelectionBinding
import com.builditmyself.collectionsview.model.MongoDataViewModel


class FieldSelectionFragment : Fragment() {

    private var _binding: FragmentFieldSelectionBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MongoDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFieldSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the field list from MongoDB connection
        val pythonInstance = sharedViewModel.pythonInstance.value
        val mongoInstance = sharedViewModel.mongoInterface.value
        val selectedCollection = sharedViewModel.collectionSelection.value
        val pyModule = pythonInstance!!.getModule("mongo-interface")
        val rawFieldList = pyModule.callAttr("get_field_list", mongoInstance, selectedCollection)
        val fieldList = rawFieldList.asList()

        val fieldsArray: ArrayList<String> = ArrayList()
        for (entry in fieldList) {
            fieldsArray.add(entry.toString())
        }

        val fieldSpinner: Spinner = view.findViewById(R.id.field_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            fieldsArray
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            fieldSpinner.adapter = adapter
        }

        view.findViewById<Button>(R.id.field_search_button).setOnClickListener() {
            val fieldSelection = fieldSpinner.selectedItem.toString()
            val searchCriteria = binding.searchCriteriaEntryText.text.toString()
            sharedViewModel.setField(fieldSelection)
            sharedViewModel.setCriteria(searchCriteria)
            findNavController().navigate(R.id.action_fieldSelectionFragment_to_resultsFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}