package com.haidev.pokemonapps.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.haidev.pokemonapps.R
import com.haidev.pokemonapps.databinding.ActivityDetailPokemonBinding
import dagger.hilt.android.AndroidEntryPoint

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