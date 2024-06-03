package com.haidev.pokemonapps.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haidev.pokemonapps.data.remote.dto.PokemonDataResponse
import com.haidev.pokemonapps.databinding.ItemMainRowBinding
import java.util.Locale

class ItemMainAdapter : RecyclerView.Adapter<ItemMainAdapter.CharactersAdapterVh>() {
    class CharactersAdapterVh(var binding: ItemMainRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<PokemonDataResponse.Result>() {
        override fun areItemsTheSame(
            oldItem: PokemonDataResponse.Result,
            newItem: PokemonDataResponse.Result
        ):
                Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PokemonDataResponse.Result,
            newItem: PokemonDataResponse.Result
        ):
                Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(dataResponse: List<PokemonDataResponse.Result?>?) {
        asyncListDiffer.submitList(dataResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            CharactersAdapterVh {
        val binding =
            ItemMainRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharactersAdapterVh(binding)
    }

    override fun onBindViewHolder(holder: CharactersAdapterVh, position: Int) {
        val data = asyncListDiffer.currentList[position]
        holder.binding.apply {
            tvPokemonName.text = data.name?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            Glide.with(holder.itemView.context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${data.id}.png")
                .into(holder.binding.ivPokemonPicture)
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }
}