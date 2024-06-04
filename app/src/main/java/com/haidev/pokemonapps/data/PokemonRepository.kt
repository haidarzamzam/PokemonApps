package com.haidev.pokemonapps.data

import com.haidev.pokemonapps.data.local.entity.PokemonEntity
import com.haidev.pokemonapps.data.local.room.PokemonDao
import com.haidev.pokemonapps.data.remote.RemoteData
import com.haidev.pokemonapps.data.remote.dto.PokemonDataResponse
import com.haidev.pokemonapps.data.remote.dto.PokemonDetailDataResponse
import com.haidev.pokemonapps.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PokemonRepository @Inject constructor(
    private val remoteData: RemoteData,
    private val localData: PokemonDao,
    private val ioDispatcher: CoroutineContext
) {
    fun getPokemon(offset: Int): Flow<Resource<PokemonDataResponse>> {
        return flow {
            emit(remoteData.getPokemon(offset))
        }.flowOn(ioDispatcher)
    }

    fun getPokemonDetail(id: Int): Flow<Resource<PokemonDetailDataResponse>> {
        return flow {
            emit(remoteData.getPokemonDetail(id))
        }.flowOn(ioDispatcher)
    }

    fun getAllPokemonLocal(): Flow<List<PokemonEntity>> {
        return flow {
            emit(localData.getAllPokemon())
        }.flowOn(ioDispatcher)
    }

    fun insertPokemonLocal(pokemonEntity: PokemonEntity) {
        localData.insertPokemon(pokemonEntity)
    }

    suspend fun updatePokemonLocal(pokemonEntity: PokemonEntity) {
        localData.updatePokemon(pokemonEntity)
    }

    fun deletePokemonLocal(pokemonEntity: PokemonEntity) {
        localData.deletePokemon(pokemonEntity)
    }

}