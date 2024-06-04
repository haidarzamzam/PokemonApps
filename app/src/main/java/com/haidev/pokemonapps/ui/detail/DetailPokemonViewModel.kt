package com.haidev.pokemonapps.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haidev.pokemonapps.data.PokemonRepository
import com.haidev.pokemonapps.data.local.entity.PokemonEntity
import com.haidev.pokemonapps.data.remote.dto.PokemonDetailDataResponse
import com.haidev.pokemonapps.util.ErrorUtils
import com.haidev.pokemonapps.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailPokemonViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {
    private val _dataDetailPokemon =
        MutableStateFlow<Resource<PokemonDetailDataResponse>>(Resource.Loading())
    val dataDetailPokemon: StateFlow<Resource<PokemonDetailDataResponse>> get() = _dataDetailPokemon

    fun getDetailPokemon(id: Int) {
        viewModelScope.launch {
            _dataDetailPokemon.value = Resource.Loading()
            repository.getPokemonDetail(id)
                .catch {
                    _dataDetailPokemon.value = Resource.Error(ErrorUtils.getErrorThrowableMsg(it))
                }
                .collect {
                    _dataDetailPokemon.value = it
                }
        }
    }

    fun catchPokemon(nickname: String, id: String) {
        viewModelScope.launch {
            val pokemonEntity = PokemonEntity(
                id = id,
                nickname = nickname,
                fibonacci = 0
            )
            repository.insertPokemonLocal(pokemonEntity)
        }
    }
}