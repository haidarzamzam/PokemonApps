package com.haidev.pokemonapps.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.haidev.pokemonapps.data.local.entity.PokemonEntity

@Dao
interface PokemonDao {
    @Query("SELECT * FROM pokemon")
    fun getAllPokemon(): List<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPokemon(pokemonData: PokemonEntity)

    @Update
    suspend fun updatePokemon(pokemonData: PokemonEntity)

    @Delete
    fun deletePokemon(pokemonData: PokemonEntity)
}