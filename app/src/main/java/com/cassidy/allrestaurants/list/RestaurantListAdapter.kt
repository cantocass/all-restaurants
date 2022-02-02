package com.cassidy.allrestaurants.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cassidy.allrestaurants.common.Place

import com.cassidy.allrestaurants.databinding.ItemRestaurantBinding
import javax.inject.Inject

class RestaurantListAdapter @Inject constructor() : ListAdapter<Place, PlaceItemViewHolder>(PlaceDiffCallback) {

    object PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean = oldItem.placeId == newItem.placeId

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean = oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceItemViewHolder {
        return PlaceItemViewHolder(ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PlaceItemViewHolder, position: Int) = holder.bind(getItem(position))
}

class PlaceItemViewHolder(private val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {

   fun bind(place: Place) {
       binding.model = ItemRestaurantBindingModel(place)
   }

    override fun toString(): String {
//            return super.toString() + " '" + contentView.text + "'"
        return ""
    }
}

data class ItemRestaurantBindingModel(private val place: Place) {
    val name = place.name ?: ""
    val rating = place.rating?.toFloat() ?: 0F
    val ratingCount = place.userRatingsTotal ?: 0
    val ratingCountText = "($ratingCount)"
    val price = place.priceLevel?.let { i -> when (i){
        1 -> "$"
        2 -> "$$"
        3 -> "$$$"
        4 -> "$$$$"
        else -> "Free"
    } }
}
