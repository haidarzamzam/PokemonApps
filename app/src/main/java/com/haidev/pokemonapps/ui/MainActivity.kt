package com.haidev.pokemonapps.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.haidev.pokemonapps.databinding.ActivityMainBinding
import com.haidev.pokemonapps.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        lifecycleScope.launch {
            mainViewModel.dataState.collect {
                when (it) {
                    is NetworkResult.Loading -> {
                        Log.d("PokemonApps", "Loading")
                    }

                    is NetworkResult.Success -> {
                        Log.d("PokemonApps", it.data.toString())

                    }

                    is NetworkResult.Error -> {
                        Log.d("PokemonApps", it.toString())
                    }
                }
            }
        }

    }
}