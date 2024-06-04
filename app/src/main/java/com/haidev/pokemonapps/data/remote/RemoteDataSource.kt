package com.haidev.pokemonapps.data.remote

import com.haidev.pokemonapps.data.remote.dto.PokemonDataResponse
import com.haidev.pokemonapps.data.remote.dto.PokemonDetailDataResponse
import com.haidev.pokemonapps.util.Resource

interface RemoteDataSource {
    suspend fun getPokemon(offset: Int): Resource<PokemonDataResponse>

    suspend fun getPokemonDetail(id: Int): Resource<PokemonDetailDataResponse>
}