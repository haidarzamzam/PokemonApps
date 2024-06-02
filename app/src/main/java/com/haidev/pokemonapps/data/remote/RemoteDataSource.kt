package com.haidev.pokemonapps.data.remote

import com.haidev.pokemonapps.data.remote.dto.PokemonDataResponse

interface RemoteDataSource {
    suspend fun getPokemon(limit: Int, offset: Int): PokemonDataResponse
}