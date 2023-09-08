package com.builditmyself.collectionsview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.builditmyself.collectionsview.databinding.FragmentFieldSelectionBinding
import com.builditmyself.collectionsview.model.MongoDataViewModel


class FieldSelectionFragment : Fragment() {

    private var _binding: FragmentFieldSelectionBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MongoDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFieldSelectionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
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


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}