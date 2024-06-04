package com.haidev.pokemonapps.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haidev.pokemonapps.R
import com.haidev.pokemonapps.data.local.entity.PokemonEntity
import com.haidev.pokemonapps.databinding.ItemMyPokemonRowBinding
import java.util.Locale

class ItemListMyPokemonAdapter(
    private val onItemClicked: (PokemonEntity) -> Unit,
    private val onItemRename: (PokemonEntity) -> Unit,
    private val onItemRelease: (PokemonEntity) -> Unit
) :
    RecyclerView.Adapter<ItemListMyPokemonAdapter.CharactersAdapterVh>() {
    class CharactersAdapterVh(var binding: ItemMyPokemonRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<PokemonEntity>() {
        override fun areItemsTheSame(
            oldItem: PokemonEntity,
            newItem: PokemonEntity
        ):
                Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PokemonEntity,
            newItem: PokemonEntity
        ):
                Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(dataResponse: List<PokemonEntity?>?) {
        asyncListDiffer.submitList(dataResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            CharactersAdapterVh {
        val binding =
            ItemMyPokemonRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharactersAdapterVh(binding)
    }

    override fun onBindViewHolder(holder: CharactersAdapterVh, position: Int) {
        val data = asyncListDiffer.currentList[position]
        holder.binding.apply {
            tvPokemonName.text = data.nickname.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            } + " - " + data.fibonacci.toString()
            Glide.with(holder.itemView.context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${data.id}.png")
                .into(holder.binding.ivPokemonPicture)

            btnMore.setOnClickListener { view ->
                val popupMenu = PopupMenu(holder.itemView.context, view)
                popupMenu.inflate(R.menu.menu_item)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.actionEdit -> {
                            if (data != null) {
                                onItemRename(data)
                            }
                            true
                        }

                        R.id.actionDelete -> {
                            if (data != null) {
                                onItemRelease(data)
                            }
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
            }
        }

        holder.itemView.setOnClickListener {
            if (data != null) {
                onItemClicked(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }
}