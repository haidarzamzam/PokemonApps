package com.haidev.pokemonapps.data.remote

interface RemoteDataSource {
    suspend fun doGetPokemon(limit: Int, offset: Int)
}