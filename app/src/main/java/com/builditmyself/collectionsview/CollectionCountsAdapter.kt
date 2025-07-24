package com.builditmyself.collectionsview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.builditmyself.collectionsview.databinding.ItemCollectionCountBinding

class CollectionCountsAdapter(
    private var items: List<CollectionCount>
) : RecyclerView.Adapter<CollectionCountsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCollectionCountBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCollectionCountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.collectionName.text = item.name
        holder.binding.collectionCount.text = item.count.toString()
    }

    override fun getItemCount() = items.size

    fun submitList(newItems: List<CollectionCount>) {
        items = newItems
        notifyDataSetChanged()
    }
}