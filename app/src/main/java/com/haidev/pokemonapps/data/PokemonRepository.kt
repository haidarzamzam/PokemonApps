package com.haidev.pokemonapps.data

import com.haidev.pokemonapps.data.remote.RemoteData
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val remoteData: RemoteData,
) {
    suspend fun getPokemonRemote(limit: Int, offset: Int) {
        remoteData.doGetPokemon(limit, offset)
    }
}