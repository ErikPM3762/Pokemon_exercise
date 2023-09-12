package com.example.pokemonexercise.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.local.PokemonEntity
import com.example.pokemonexercise.R
import com.example.pokemonexercise.ui.dialogs.DialogUtils
import com.example.pokemonexercise.ui.home.PokemonHomeFragmentDirections.goToPokemonDetail
import com.example.pokemonexercise.ui.home.adapters.AdapterPokemon
import com.example.pokemonexercise.ui.home.listeners.OpenPokemonListener
import com.example.pokemonexercise.ui.utils.PreferencesManager
import com.example.pokemonexercise.ui.utils.isConnected
import com.example.toolsAndResources.databinding.FragmentPokemonHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PokemonHomeFragment : Fragment() {
    private lateinit var binding: FragmentPokemonHomeBinding
    lateinit var adapter: AdapterPokemon
    private val viewModel: PokemonHomeViewModel by viewModels()
    var currentOffset = 0
    private var isRequestInProgress = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferenceManager = PreferencesManager(requireContext())
        val isEmptyDB = preferenceManager.getBoolean(PreferencesManager.EMPTY_DATA_BASE, false)
        val recyclerView: RecyclerView = binding.pokemonList
        if (!isEmptyDB) {
            if (requireActivity().isConnected(requireContext())) {
                viewModel.getListPokemon(currentOffset, false)
                preferenceManager.saveBoolean(PreferencesManager.EMPTY_DATA_BASE, true)
            } else {
                preferenceManager.saveBoolean(PreferencesManager.EMPTY_DATA_BASE, false)
                DialogUtils.showErrorDialogInternet(requireActivity().supportFragmentManager)
            }
        } else {
            viewModel.getAllData()
            viewModel.chargeDB()
        }
        observeEvents()


        adapter = AdapterPokemon(requireContext(), object : OpenPokemonListener {
            override fun open(id: Int) {
                val action = goToPokemonDetail(id)
                findNavController().navigate(action)
            }

            override fun openImageFull() {
                binding.rootExpandend.visibility = View.VISIBLE
                openImageScreen()
            }

            override fun saveFavorite(isFavorite: Boolean, idPokemon: Int) {
                viewModel.toggleFavorite(idPokemon, isFavorite)
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            binding.rootExpandend.visibility = View.GONE
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("SuspiciousIndentation")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (linearLayoutManager != null && !isRequestInProgress && linearLayoutManager.findLastCompletelyVisibleItemPosition() == (adapter.data.size - 1)) {
                    currentOffset = adapter.data.size
                    isRequestInProgress = true
                    viewModel.getListPokemon(currentOffset, true)
                    binding.loadingAnimationView.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeEvents() {
        viewModel.success.observe(viewLifecycleOwner) {
            val preferenceManager = PreferencesManager(requireContext())
            val isEmptyDB = preferenceManager.getBoolean(PreferencesManager.EMPTY_DATA_BASE, false)
            viewModel.currentPokemon.observe(viewLifecycleOwner) { result ->
                binding.loadingAnimationView.visibility = View.GONE
                isRequestInProgress = false
                if (isEmptyDB)  adapter.addItem(result)
                else viewModel.currentListOffline.observe(viewLifecycleOwner){
                    adapter.addItem(it)
                }
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.successMore.observe(viewLifecycleOwner) {
            viewModel.currentPokemon.observe(viewLifecycleOwner) { result ->
                binding.loadingAnimationView.visibility = View.GONE
                isRequestInProgress = false
                adapter.addItem(result)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun openImageScreen() {
        val currentImage = adapter.getCurrentImage()
        if (currentImage != null) {
            val fullImageView = binding.imgPokemonFull
            Glide.with(this)
                .load(currentImage)
                .into(fullImageView)
        }
    }
}
