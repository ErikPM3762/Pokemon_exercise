package com.example.pokemonexercise.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.data.local.PokemonEntity
import com.example.pokemonexercise.ui.home.listeners.OpenPokemonListener
import com.example.pokemonexercise.ui.utils.generateInitials
import com.example.toolsAndResources.R


class AdapterPokemon(
    private val context: Context,
    private val listener: OpenPokemonListener
) : RecyclerView.Adapter<AdapterPokemon.PokemonHolder>() {

    private var currentImagePosition: Int = -1
    var data: MutableList<PokemonEntity> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonHolder, position: Int) {
        val pokemon = data[position]
        holder.bind(pokemon, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addItem(item: List<PokemonEntity>) {
        data.clear()
        data.addAll(item)
        notifyDataSetChanged()
    }

    inner class PokemonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name_pokemon)
        private val image: ImageView = itemView.findViewById(R.id.img_pokemon)
        private val txtImage: TextView = itemView.findViewById(R.id.initialsText)
        private val rootElement: View = itemView.findViewById(R.id.root_element)
        private val favorite: ImageView = itemView.findViewById(R.id.favorite)

        fun bind(pokemon: PokemonEntity, position: Int) {
            name.text = pokemon.nombre
            if (position != RecyclerView.NO_POSITION) {
                val imageUrl = pokemon.sprites
                val pokemonName = pokemon.nombre

                if (pokemon.favorite) {
                    favorite.setImageResource(R.drawable.estrella)
                } else {
                    favorite.setImageResource(R.drawable.favorito)
                }

                if (imageUrl.isNotEmpty()) {
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.usuario)
                        .error(R.drawable.usuario)

                    Glide.with(context)
                        .load(imageUrl)
                        .apply(requestOptions)
                        .into(image)
                } else {
                    val initials = generateInitials(pokemonName)
                    val backgroundResId = R.drawable.background_initials
                    val textColor = R.color.txt_color_initials

                    if (initials.isNotEmpty()) {
                        image.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.background_initials
                            )
                        )
                        image.setBackgroundResource(backgroundResId)
                        txtImage.setTextColor(ContextCompat.getColor(context, textColor))
                        txtImage.visibility = View.VISIBLE
                        txtImage.text = initials
                    } else {
                        txtImage.visibility = View.GONE
                        image.setImageResource(R.drawable.usuario)
                    }
                }

                rootElement.setOnClickListener {
                    listener.open(
                        pokemon.id
                    )
                }

                image.setOnClickListener {
                    currentImagePosition = position
                    listener.openImageFull()
                }

                favorite.setOnClickListener {
                    val pokemon = data[position]
                    pokemon.favorite = !pokemon.favorite
                    listener.saveFavorite(pokemon.favorite, pokemon.id)

                    if (pokemon.favorite) {
                        favorite.setImageResource(R.drawable.estrella)
                    } else {
                        favorite.setImageResource(R.drawable.favorito)
                    }
                    notifyItemChanged(position)
                }


            }
        }
    }

    fun getCurrentImage(): String? {
        return if (currentImagePosition != -1 && currentImagePosition < data.size) {
            data[currentImagePosition].sprites
        } else {
            null
        }
    }
}

