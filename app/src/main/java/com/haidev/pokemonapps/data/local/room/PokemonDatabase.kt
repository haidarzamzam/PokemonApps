package com.haidev.pokemonapps.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.haidev.pokemonapps.data.local.entity.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
}