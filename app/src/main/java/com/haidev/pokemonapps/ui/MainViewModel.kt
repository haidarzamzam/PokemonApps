package com.haidev.pokemonapps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haidev.pokemonapps.data.PokemonRepository
import com.haidev.pokemonapps.data.remote.dto.PokemonDataResponse
import com.haidev.pokemonapps.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {

    private val _dataState =
        MutableStateFlow<NetworkResult<PokemonDataResponse>>(NetworkResult.Loading())
    val dataState: StateFlow<NetworkResult<PokemonDataResponse>> = _dataState

    init {
        getPokemon(20, 0)
    }

    fun getPokemon(limit: Int, offset: Int) {
        viewModelScope.launch {
            repository.getPokemon(limit, offset).collect {
                _dataState.value = it
            }
        }
    }
}