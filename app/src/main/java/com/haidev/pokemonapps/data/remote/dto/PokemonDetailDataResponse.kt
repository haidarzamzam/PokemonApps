package com.haidev.pokemonapps.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDetailDataResponse(
    val abilities: List<Ability>?,
    val forms: List<Form>?,
    val height: Int?,
    val id: Int?,
    val name: String?,
    val stats: List<Stat>?,
    val types: List<Type>?,
    val weight: Int?
) : Parcelable {
    @Parcelize
    data class Ability(
        val ability: AbilityX?,
        val is_hidden: Boolean?,
        val slot: Int?
    ) : Parcelable {
        @Parcelize
        data class AbilityX(
            val name: String?,
            val url: String?
        ) : Parcelable
    }

    @Parcelize
    data class Form(
        val name: String?,
        val url: String?
    ) : Parcelable

    @Parcelize
    data class Stat(
        val base_stat: Int?,
        val effort: Int?,
        val stat: StatX?
    ) : Parcelable {
        @Parcelize
        data class StatX(
            val name: String?,
            val url: String?
        ) : Parcelable
    }

    @Parcelize
    data class Type(
        val slot: Int?,
        val type: TypeX?
    ) : Parcelable {
        @Parcelize
        data class TypeX(
            val name: String?,
            val url: String?
        ) : Parcelable
    }
}