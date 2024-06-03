package com.haidev.pokemonapps.data.remote

import android.content.Context
import com.haidev.pokemonapps.R
import com.haidev.pokemonapps.data.remote.dto.PokemonDataResponse
import com.haidev.pokemonapps.util.NetworkConnectivity
import com.haidev.pokemonapps.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteData @Inject constructor(
    private val apiService: ApiService,
    private val networkConnectivity: NetworkConnectivity,
    @ApplicationContext val context: Context
) : RemoteDataSource {
    override suspend fun getPokemon(offset: Int): Resource<PokemonDataResponse> {
        return when (val response =
            processCall { apiService.getPokemon(offset, 20) }) {
            is PokemonDataResponse -> Resource.Success(data = response)
            else -> Resource.Error(messageError = response as String)
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!networkConnectivity.isConnected()) {
            return context.getString(R.string.no_internet)
        }
        return try {
            val response = responseCall.invoke()
            if (response.isSuccessful) {
                response.body()
            } else {
                if (response.code() == 500 || response.code() == 503) {
                    context.getString(R.string.server_error)
                } else {
                    response.errorBody()?.string()
                }
            }
        } catch (e: IOException) {
            context.getString(R.string.network_error)
        }
    }
}