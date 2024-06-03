package com.haidev.pokemonapps.data.remote

import com.haidev.pokemonapps.data.remote.dto.PokemonDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("pokemon")
    suspend fun getPokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<PokemonDataResponse>
}