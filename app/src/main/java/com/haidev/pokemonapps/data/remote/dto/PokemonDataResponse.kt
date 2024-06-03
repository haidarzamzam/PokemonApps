package com.haidev.pokemonapps.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDataResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<Result>?
) : Parcelable {
    @Parcelize
    data class Result(
        val name: String?,
        val url: String?,
        val id: Int?
    ) : Parcelable
}