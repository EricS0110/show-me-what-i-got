package com.builditmyself.collectionsview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.fragment.app.activityViewModels
import com.builditmyself.collectionsview.databinding.FragmentHomeBinding
import com.builditmyself.collectionsview.model.MongoDataViewModel
import kotlin.getValue
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MongoDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.collectionsCountsRecyclerView) { v, insets ->
            val topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            v.setPadding(
                v.paddingLeft,
                topInset,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        val adapter = CollectionCountsAdapter(emptyList())
        binding.collectionsCountsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.collectionsCountsRecyclerView.adapter = adapter

        sharedViewModel.collectionCounts.observe(viewLifecycleOwner) { pyObj ->
            if (pyObj != null) {
                val pyDict = pyObj.asMap() // Chaquopy: returns Map<PyObject, PyObject>
                val items = pyDict.entries.map { (k, v) ->
                    CollectionCount(k.toString(), (v.toJava(Int::class.java) as? Int) ?: 0)
                }
                adapter.submitList(items)
            }
        }
    }

}