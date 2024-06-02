package com.haidev.pokemonapps.data

import com.haidev.pokemonapps.data.remote.RemoteData
import com.haidev.pokemonapps.data.remote.dto.PokemonDataResponse
import com.haidev.pokemonapps.util.ErrorUtils
import com.haidev.pokemonapps.util.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val remoteData: RemoteData,
) {
    fun getPokemon(limit: Int, offset: Int): Flow<NetworkResult<PokemonDataResponse>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = remoteData.getPokemon(limit, offset)
            emit(NetworkResult.Success(response))
        } catch (e: Exception) {
            emit(NetworkResult.Error(ErrorUtils.getErrorThrowableMsg(e)))
        }
    }
}