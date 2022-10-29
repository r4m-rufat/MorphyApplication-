package com.codingwithrufat.abbapplication.presentation.home_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.codingwithrufat.abbapplication.R
import com.codingwithrufat.abbapplication.databinding.FragmentHomeBinding
import com.codingwithrufat.abbapplication.presentation.detail_page.DetailFragment
import com.codingwithrufat.abbapplication.utils.MorphyParcelableItem
import com.codingwithrufat.abbapplication.utils.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment(), MorphyItemAdapter.OnClickedItemListener {

    private var _binding: FragmentHomeBinding? = null

    private val binding
        get() = _binding as FragmentHomeBinding

    private lateinit var viewModel: HomePageViewModel

    private lateinit var morphyItemAdapter: MorphyItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]

        getObservableData()
        setupAdapter()

        return binding.root
    }

    private fun getObservableData() {

        val queryMap = HashMap<String, String>()

        viewModel.getAllData(queryMap)

        viewModel.data.observe(viewLifecycleOwner) { response ->

            when (response) {

                is NetworkResponse.LOADING -> {

                    // show Progress Bar

                }

                is NetworkResponse.SUCCEED -> {

                    // get list of the data and set it to adapter
                    Log.d(TAG, "getObservableData: data successfully comes")
                    response.data?.results?.let {
                        morphyItemAdapter.updateList(it)
                    }

                }

                is NetworkResponse.ERROR -> {

                    // show the error message with toast
                    Toast.makeText(requireContext(), response.msg, Toast.LENGTH_SHORT).show()

                }

            }

        }

    }

    private fun setupAdapter() {

        morphyItemAdapter = MorphyItemAdapter(requireContext(), emptyList(), this)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = morphyItemAdapter
        }

    }

    override fun onClickItem(item: MorphyParcelableItem) {

        val bundle = Bundle()
        bundle.putParcelable("item", item)
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, DetailFragment::class.java, bundle)
            .addToBackStack(null)
            .commit()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}