package com.builditmyself.collectionsview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.builditmyself.collectionsview.databinding.FragmentCollectionSelectionBinding
import com.builditmyself.collectionsview.model.MongoDataViewModel


class CollectionSelectionFragment : Fragment() {

    private var _binding: FragmentCollectionSelectionBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MongoDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionSelectionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the collections list from MongoDB connection
        val pythonInstance = sharedViewModel.pythonInstance.value
        val mongoInstance = sharedViewModel.mongoInterface.value
        val pyModule = pythonInstance!!.getModule("mongo-interface")
        val rawCollectionList = pyModule.callAttr("get_collection_list", mongoInstance)
        val collectionList = rawCollectionList.asList()

        val collectionsArray: ArrayList<String> = ArrayList()
        for (entry in collectionList) {
            collectionsArray.add(entry.toString())
        }

        val collectionSpinner: Spinner = view.findViewById(R.id.collection_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            collectionsArray
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            collectionSpinner.adapter = adapter
        }

        view.findViewById<Button>(R.id.collection_selection_button).setOnClickListener() {
            val spinnerSelection = collectionSpinner.selectedItem.toString()
            sharedViewModel.setCollection(spinnerSelection)
            findNavController().navigate(R.id.action_collectionSelectionFragment_to_fieldSelectionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}