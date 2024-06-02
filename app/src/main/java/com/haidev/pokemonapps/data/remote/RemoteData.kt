package com.haidev.pokemonapps.data.remote

import javax.inject.Inject

class RemoteData @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    override suspend fun doGetPokemon(limit: Int, offset: Int) {
        apiService.getPokemon(limit, offset)
    }
}