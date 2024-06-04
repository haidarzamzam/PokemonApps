package com.haidev.pokemonapps.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
        val isFromFav = intent.getBooleanExtra("isFromFav", false)
        viewModel.getDetailPokemon(idPokemon)

        Glide.with(this@DetailPokemonActivity)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${idPokemon}.png")
            .into(binding.ivPokemon)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataDetailPokemon.collect { data ->
                    when (data) {
                        is Resource.Loading -> {
                            binding.pbLoading.isVisible = true
                        }

                        is Resource.Success -> {
                            binding.pbLoading.isVisible = false
                            binding.btnCatch.isVisible = !isFromFav
                            binding.btnCatch.isEnabled = true
                            binding.tvName.text = data.data?.name?.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            }
                            binding.tvWeight.text = data.data?.weight.toString()
                            binding.tvHeight.text = data.data?.height.toString()
                            binding.tvTypes.text =
                                data.data?.types?.joinToString(", ") { it.type?.name.toString() }
                            binding.tvAbility.text =
                                data.data?.abilities?.joinToString(", ") { it.ability?.name.toString() }
                            binding.tvStat.text =
                                data.data?.stats?.joinToString(", ") { it.stat?.name.toString() }
                            binding.btnCatch.setOnClickListener {
                                if (Random.nextBoolean()) {
                                    showInputDialog(
                                        this@DetailPokemonActivity,
                                        data.data?.id.toString()
                                    )
                                } else {
                                    Toast.makeText(
                                        this@DetailPokemonActivity,
                                        "Failed Catch Pokemon, Try Again",
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

    private fun showInputDialog(context: Context, idPokemon: String) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_nickname_pokemon, null)
        builder.setView(view)

        val etNickname = view.findViewById<EditText>(R.id.etNickname)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        val dialog = builder.create()
        dialog.show()

        btnSave.setOnClickListener {
            val inputText = etNickname.text.toString()
            Toast.makeText(context, "Catch: $inputText Success", Toast.LENGTH_SHORT).show()
            viewModel.catchPokemon(inputText, idPokemon)
            dialog.dismiss()
        }
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