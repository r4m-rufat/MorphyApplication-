package com.codingwithrufat.abbapplication.presentation.detail_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codingwithrufat.abbapplication.databinding.FragmentDetailBinding
import com.codingwithrufat.abbapplication.utils.MorphyParcelableItem

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    private val binding
        get() = _binding as FragmentDetailBinding

    private var morphy: MorphyParcelableItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            morphy = it.getParcelable("item")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater)

        setItems()

        return binding.root
    }

    private fun setItems() {

        Glide.with(requireContext())
            .load(morphy?.image)
            .into(binding.morphyImage)

        binding.txtSpecies.text = morphy?.species
        binding.txtStatus.text = morphy?.status
        binding.txtGender.text = morphy?.gender
        binding.txtLocationUrl.text = morphy?.location
        binding.txtCharacterUrl.text = morphy?.character

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}