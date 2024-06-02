package com.haidev.pokemonapps.data.remote

import com.haidev.pokemonapps.data.remote.dto.PokemonDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonDataResponse>
}