package com.codingwithrufat.abbapplication.presentation.home_page

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codingwithrufat.abbapplication.R
import com.codingwithrufat.abbapplication.databinding.FragmentHomeBinding
import com.codingwithrufat.abbapplication.network.model.ResultsItem
import com.codingwithrufat.abbapplication.utils.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding
        get() = _binding as FragmentHomeBinding

    private lateinit var viewModel: HomePageViewModel

    private lateinit var morphyItemAdapter: MorphyItemAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    var isLastPage = false
    var isLoading = false
    var isScrolling = false
    var list: MutableList<ResultsItem?> = mutableListOf()

    private val genderSpinnerItems = arrayListOf("Mixed", "Male", "Female", "Genderless", "Unknown")
    private val statusSpinnerItems = arrayListOf("Mixed", "Alive", "Dead", "Unknown")
    private val speciesSpinnerItems = arrayListOf(
        "Mixed",
        "Human",
        "Humanoid",
        "Robot",
        "Animal",
        "Alien",
        "Cronenberg",
        "Poopybutthole",
        "Mythological Creature"
    )
    private lateinit var genderSpinnerAdapter: ArrayAdapter<String>
    private lateinit var statusSpinnerAdapter: ArrayAdapter<String>
    private lateinit var speciesSpinnerAdapter: ArrayAdapter<String>

    private val queryMap = HashMap<String, String>()

    private var filterVisibility = false

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
        setupSpinnerItems()
        clickedFilterButton()
        clickedFilterIcon()
        setupSearchView()
        refreshLayout()

        return binding.root
    }

    private fun getObservableData() {

        viewModel.getAllData()

        viewModel.data.observe(viewLifecycleOwner) { response ->

            when (response) {

                is NetworkResponse.LOADING -> {

                    // show Progress Bar
                    binding.progressBar.visibility = VISIBLE

                }

                is NetworkResponse.SUCCEED -> {

                    // get list of the data and set it to adapter
                    Log.d(TAG, "getObservableData: data successfully comes")
                    binding.progressBar.visibility = GONE
                    response.data?.results?.let {
                        list.addAll(it)
                        morphyItemAdapter.updateList(list)
                    }

                    // when the page is refreshed
                    binding.swipeRefreshLayout.isRefreshing = false

                }

                is NetworkResponse.ERROR -> {

                    // show the error message with toast
                    Log.d(TAG, "getObservableData: ${response.msg}")
                    binding.progressBar.visibility = GONE

                }

            }

        }

    }

    private fun refreshLayout() {

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getAllData()
            viewModel.resetPaginationElements() // page is refreshed
        }

    }

    private fun setupSpinnerItems() {

        genderSpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.layout_selected_spinner_item,
            genderSpinnerItems
        )
        genderSpinnerAdapter.setDropDownViewResource(R.layout.layout_custom_dropdown_view_resource)
        binding.genderSpinner.adapter = genderSpinnerAdapter

        statusSpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.layout_selected_spinner_item,
            statusSpinnerItems
        )
        statusSpinnerAdapter.setDropDownViewResource(R.layout.layout_custom_dropdown_view_resource)
        binding.statusSpinner.adapter = statusSpinnerAdapter

        speciesSpinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.layout_selected_spinner_item,
            speciesSpinnerItems
        )
        speciesSpinnerAdapter.setDropDownViewResource(R.layout.layout_custom_dropdown_view_resource)
        binding.speciesSpinner.adapter = speciesSpinnerAdapter

    }

    private fun clickedFilterButton() {

        binding.btnFilter.setOnClickListener {

            filterVisibility = !filterVisibility
            if (filterVisibility)
                binding.filterCard.visibility = VISIBLE
            else
                binding.filterCard.visibility = GONE

            queryMap["gender"] = binding.genderSpinner.selectedItem.toString().checkSpinnerItem()
            queryMap["status"] = binding.statusSpinner.selectedItem.toString().checkSpinnerItem()
            queryMap["species"] = binding.speciesSpinner.selectedItem.toString().checkSpinnerItem()
            viewModel.updateQueryMap(queryMap)
            viewModel.updateResponseStateToLoading()
            list = mutableListOf()
            morphyItemAdapter.updateList(list)
            viewModel.getAllData()

        }

    }

    private fun setupSearchView() {

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.updateQueryMapItem("name", it)
                    list = mutableListOf()
                    viewModel.resetPaginationElements()
                    viewModel.updateResponseStateToLoading()
                    viewModel.getAllData()
                } ?: viewModel.updateQueryMapItem("name", "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun clickedFilterIcon() {

        binding.icFilter.setOnClickListener {

            filterVisibility = !filterVisibility
            if (filterVisibility)
                binding.filterCard.visibility = VISIBLE
            else
                binding.filterCard.visibility = GONE

        }

    }

    private fun setupAdapter() {

        morphyItemAdapter = MorphyItemAdapter(requireContext(), emptyList())
        gridLayoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager

            adapter = morphyItemAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    val firstItemPosition = gridLayoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = gridLayoutManager.childCount
                    val totalItemCount = gridLayoutManager.itemCount

                    val isNotLoadingAndLastPage = !isLastPage && !isLoading
                    val isAtLastItem =
                        firstItemPosition + visibleItemCount >= totalItemCount
                    val isNotAtBeginning = visibleItemCount >= 0
                    val isTotalMoreThanVisible = totalItemCount >= PAGE_SIZE

                    val shouldPaginate =
                        isNotLoadingAndLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible

                    if (shouldPaginate) {
                        viewModel.incrementPageNumber()
                        viewModel.getAllData()
                        isScrolling = false
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true;
                    }
                }
            })

        }

    }

    /**
     * if spinner item for filter
     * 1) "Mixed" it must return empty string
     * 2) "Human" it must return this item
     */
    private fun String.checkSpinnerItem(): String {
        return if (this == "Mixed")
            ""
        else
            this
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}