package com.haidev.pokemonapps.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: String = "",

    @field:ColumnInfo(name = "name")
    val name: String = "",

    @field:ColumnInfo(name = "fibonacci")
    val fibonacci: Int = 0
)