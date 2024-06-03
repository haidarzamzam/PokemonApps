package com.haidev.pokemonapps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haidev.pokemonapps.data.PokemonRepository
import com.haidev.pokemonapps.data.remote.dto.PokemonDataResponse
import com.haidev.pokemonapps.util.ErrorUtils
import com.haidev.pokemonapps.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {
    private val _dataPokemon =
        MutableStateFlow<Resource<List<PokemonDataResponse.Result>>>(Resource.Loading())
    val dataPokemon: StateFlow<Resource<List<PokemonDataResponse.Result>>> get() = _dataPokemon

    var offsetValue = MutableStateFlow(0)

    init {
        getPokemon(offsetValue.value)
    }

    fun getPokemon(offset: Int = offsetValue.value) {
        viewModelScope.launch {
            val currentData = _dataPokemon.value.data.orEmpty()
            _dataPokemon.value = Resource.Loading()
            repository.getPokemon(offset)
                .catch {
                    _dataPokemon.value = Resource.Error(ErrorUtils.getErrorThrowableMsg(it))
                }
                .map { pokemon ->
                    pokemon.data?.results?.map {
                        val regex = Regex("""/(\d+)/$""")
                        val matchResult = regex.find(it.url.toString())
                        val idPokemon = matchResult?.groups?.get(1)?.value?.toIntOrNull() ?: -1
                        it.copy(id = idPokemon)
                    }
                }
                .collect { pokemonData ->
                    if (pokemonData != null) {
                        val updatedData = currentData + pokemonData
                        _dataPokemon.value = Resource.Success(updatedData)
                    }
                }
        }
    }
}