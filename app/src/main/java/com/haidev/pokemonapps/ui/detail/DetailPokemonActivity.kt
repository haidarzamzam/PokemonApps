package com.haidev.pokemonapps.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.haidev.pokemonapps.R
import com.haidev.pokemonapps.databinding.ActivityDetailPokemonBinding
import com.haidev.pokemonapps.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

@AndroidEntryPoint
class DetailPokemonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPokemonBinding
    private val viewModel: DetailPokemonViewModel by viewModels<DetailPokemonViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailPokemonBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        initUI()
        initData()
    }

    private fun initData() {
        val idPokemon = intent.getIntExtra("idPokemon", 0)
        viewModel.getDetailPokemon(idPokemon)

        Glide.with(this@DetailPokemonActivity)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${idPokemon}.png")
            .into(binding.ivPokemon)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataDetailPokemon.collect { it ->
                    when (it) {
                        is Resource.Loading -> {
                            binding.pbLoading.isVisible = true
                        }

                        is Resource.Success -> {
                            binding.pbLoading.isVisible = false
                            binding.btnCatch.isEnabled = true
                            binding.tvName.text = it.data?.name?.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            }
                            binding.tvWeight.text = it.data?.weight.toString()
                            binding.tvHeight.text = it.data?.height.toString()
                            binding.tvTypes.text = it.data?.types?.joinToString(", ") { it.type?.name.toString() }
                            binding.tvAbility.text = it.data?.abilities?.joinToString(", ") { it.ability?.name.toString() }
                            binding.tvStat.text = it.data?.stats?.joinToString(", ") { it.stat?.name.toString() }
                            binding.btnCatch.setOnClickListener {
                                if (Random.nextBoolean()) {
                                    Toast.makeText(
                                        this@DetailPokemonActivity,
                                        "Success Catch Pokemon",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@DetailPokemonActivity,
                                        "Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        is Resource.Error -> {
                            binding.pbLoading.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private fun initUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}