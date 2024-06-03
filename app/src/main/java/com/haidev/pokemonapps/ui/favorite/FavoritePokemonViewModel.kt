package com.haidev.pokemonapps.ui.favorite

import androidx.lifecycle.ViewModel
import com.haidev.pokemonapps.data.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritePokemonViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel()