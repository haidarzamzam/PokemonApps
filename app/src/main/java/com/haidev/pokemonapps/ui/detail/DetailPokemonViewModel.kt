package com.haidev.pokemonapps.ui.detail

import androidx.lifecycle.ViewModel
import com.haidev.pokemonapps.data.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailPokemonViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel()