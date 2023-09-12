package com.example.pokemonexercise.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.data.local.PokemonEntity
import com.example.pokemonexercise.R
import com.example.pokemonexercise.ui.home.PokemonHomeViewModel
import com.example.pokemonexercise.ui.utils.generateInitials
import com.example.pokemonexercise.ui.utils.loadImageOrSetInitials
import com.example.toolsAndResources.databinding.FragmentPokemonDetailBinding
import com.example.toolsAndResources.databinding.FragmentPokemonHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {
    private lateinit var binding: FragmentPokemonDetailBinding
    private val viewModel: PokemonDetailViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = requireArguments().getInt("id")
        viewModel.getPokemonDetail(id)
        observeEvents()
    }


    @SuppressLint("SetTextI18n")
    private fun observeEvents() {
        viewModel.pokemonData.observe(viewLifecycleOwner) {
            val pokemonData = it.peekContent()
            binding.apply {
                namePokemon.text = pokemonData.nombre
                typePokemon.text = pokemonData.tipo[0]
                txtWeight.text = "${pokemonData.peso} kilogramos"
                txtHeight.text = "${pokemonData.altura} metros"

                if (pokemonData.favorite) {
                    favorite.setImageResource(com.example.toolsAndResources.R.drawable.estrella)
                } else {
                    favorite.setImageResource(com.example.toolsAndResources.R.drawable.favorito)
                }
                val name = generateInitials(pokemonData.nombre)
                imgPokemon.loadImageOrSetInitials(
                    pokemonData.sprites,
                    name,
                    requireContext(),
                    com.example.toolsAndResources.R.drawable.background_initials,
                    com.example.toolsAndResources.R.color.txt_color_initials
                )
            }

            setFavorite(it.peekContent())

        }
    }

    private fun setFavorite(pokemon: PokemonEntity) {
        binding.favorite.setOnClickListener {
            pokemon.favorite = !pokemon.favorite
            val id = requireArguments().getInt("id")
            viewModel.toggleFavorite(id,pokemon.favorite)
            if (pokemon.favorite) {
                binding.favorite.setImageResource(com.example.toolsAndResources.R.drawable.estrella)
            } else {
                binding.favorite.setImageResource(com.example.toolsAndResources.R.drawable.favorito)
            }
        }
    }
}
