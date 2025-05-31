package com.builditmyself.collectionsview
//
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import androidx.fragment.app.activityViewModels
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.builditmyself.collectionsview.databinding.FragmentResultsBinding
//import com.builditmyself.collectionsview.model.MongoDataViewModel
//
//
//class ResultsFragment : Fragment() {
//
//    private var _binding: FragmentResultsBinding? = null
//    private val binding get() = _binding!!
//    private val sharedViewModel: MongoDataViewModel by activityViewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentResultsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        view.findViewById<Button>(R.id.button_to_collection).setOnClickListener {
//            findNavController().navigate(R.id.action_resultsFragment_to_collectionSelectionFragment)
//        }
//        view.findViewById<Button>(R.id.button_to_search).setOnClickListener {
//            findNavController().navigate(R.id.action_resultsFragment_to_fieldSelectionFragment)
//        }
//
//        val itemResults = getResults() // This IS the right spot to put this
//        val recyclerView = requireView().findViewById<RecyclerView>(R.id.results_recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this.activity)
//        val adapter = CustomAdapter(itemResults)
//        recyclerView.adapter = adapter
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun getResults(): ArrayList<ItemViewModel> {
//        val pythonInstance = sharedViewModel.pythonInstance.value
//        val mongoInterface = sharedViewModel.mongoInterface.value
//        val selectedCollection = sharedViewModel.collectionSelection.value.toString()
//        val searchField = sharedViewModel.fieldSelection.value.toString()
//        val searchCriteria = sharedViewModel.searchCriteria.value.toString()
//        val cardData = ArrayList<ItemViewModel>()
//
//        val pythonModule = pythonInstance!!.getModule("mongo-interface")
//        val rawItemResults = pythonModule.callAttr("get_items", mongoInterface, selectedCollection, searchField, searchCriteria)
//        val itemResults = rawItemResults.asList()
//        for (entry in itemResults) {
//            Log.v("MANUAL", "Entry String: $entry")
//            cardData.add(ItemViewModel(entry.toString()))
//        }
//
//        return cardData
//    }
//
//}