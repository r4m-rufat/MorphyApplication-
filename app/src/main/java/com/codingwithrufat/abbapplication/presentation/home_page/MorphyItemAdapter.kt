package com.codingwithrufat.abbapplication.presentation.home_page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codingwithrufat.abbapplication.R
import com.codingwithrufat.abbapplication.network.model.ResultsItem
import com.codingwithrufat.abbapplication.presentation.detail_page.DetailFragment
import com.codingwithrufat.abbapplication.utils.MorphyParcelableItem

class MorphyItemAdapter(
    private val context: Context,
    private var list: List<ResultsItem?>
) : RecyclerView.Adapter<MorphyItemAdapter.ViewHolder>() {

    fun updateList(newList: List<ResultsItem?>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_recyler_morphy_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position]?.name
        holder.gender.text = list[position]?.gender
        holder.species.text = list[position]?.species
        holder.status.text = list[position]?.status
        Glide.with(context)
            .load(list[position]?.image)
            .into(holder.imgPoke)

        holder.itemView.setOnClickListener { view ->

            list[position]?.let {
                val item = MorphyParcelableItem(
                    it.name,
                    it.status,
                    it.gender,
                    it.species,
                    it.location?.url,
                    it.url,
                    it.image
                )
                val bundle = Bundle()
                bundle.putParcelable("item", item)
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_detailFragment, bundle)
            }

        }

    }

    override fun getItemCount(): Int {
        if (list.isEmpty()) {
            return 0
        }
        return list.size
    }

    class ViewHolder(item_view: View) : RecyclerView.ViewHolder(item_view) {
        val name = item_view.findViewById(R.id.morphy_name) as TextView
        val status = item_view.findViewById(R.id.morphy_status) as TextView
        val species = item_view.findViewById(R.id.morphy_species) as TextView
        val gender = item_view.findViewById(R.id.morphy_gender) as TextView
        val imgPoke = item_view.findViewById(R.id.img_morphy) as ImageView
    }

}