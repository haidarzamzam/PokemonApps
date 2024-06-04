package com.haidev.pokemonapps.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haidev.pokemonapps.data.PokemonRepository
import com.haidev.pokemonapps.data.local.entity.PokemonEntity
import com.haidev.pokemonapps.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritePokemonViewModel @Inject constructor(private val repository: PokemonRepository) :
    ViewModel() {
    private val _dataMyPokemon =
        MutableStateFlow<Resource<List<PokemonEntity>>>(Resource.Loading())
    val dataMyPokemon: StateFlow<Resource<List<PokemonEntity>>> get() = _dataMyPokemon

    init {
        getMyPokemon()
    }

    private fun getMyPokemon() {
        viewModelScope.launch {
            _dataMyPokemon.value = Resource.Loading()
            repository.getAllPokemonLocal().collect {
                _dataMyPokemon.value = Resource.Success(it)
            }
        }
    }

    fun deletePokemon(pokemonEntity: PokemonEntity) {
        viewModelScope.launch {
            repository.deletePokemonLocal(pokemonEntity)
            getMyPokemon()
        }
    }

    fun renamePokemon(nickname: String, id: String, fibonacci: Int) {
        viewModelScope.launch {
            val pokemonEntity = PokemonEntity(
                id = id,
                nickname = nickname,
                fibonacci = findNextFibonacci(fibonacci)
            )
            repository.updatePokemonLocal(pokemonEntity)
            getMyPokemon()
        }
    }

    private fun findNextFibonacci(number: Int): Int {
        if (number < 0) {
            return -1
        }

        when (number) {
            0 -> return 1
            1 -> return 2
        }

        var a = 0
        var b = 1

        while (b < number) {
            val temp = a + b
            a = b
            b = temp
        }

        return if (b == number) a + b else -1
    }
}