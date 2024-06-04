package com.haidev.pokemonapps.ui.favorite

import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haidev.pokemonapps.R
import com.haidev.pokemonapps.databinding.ActivityFavoritePokemonBinding
import com.haidev.pokemonapps.ui.detail.DetailPokemonActivity
import com.haidev.pokemonapps.ui.favorite.adapter.ItemListMyPokemonAdapter
import com.haidev.pokemonapps.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class FavoritePokemonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritePokemonBinding
    private val viewModel: FavoritePokemonViewModel by viewModels<FavoritePokemonViewModel>()

    private val adapterMyPokemon by lazy {
        ItemListMyPokemonAdapter(onItemClicked = {
            val intent = Intent(this, DetailPokemonActivity::class.java)
            intent.putExtra("idPokemon", it.id.toInt())
            intent.putExtra("isFromFav", true)
            startActivity(intent)
        }, onItemRename = {
            showInputDialog(this, it.nickname, it.id, it.fibonacci)
        },
            onItemRelease = {
                val randomInt = Random.nextInt(1, 101)
                Toast.makeText(this, "Random number: ${randomInt}", Toast.LENGTH_SHORT).show()

                if (isPrime(randomInt)) {
                    viewModel.deletePokemon(it)
                    Toast.makeText(this, "Prime number, success release", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Not prime number, failed release", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoritePokemonBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        initUI()
        initData()
    }

    private fun initUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))

        binding.rvPokemon.apply {
            layoutManager =
                LinearLayoutManager(this@FavoritePokemonActivity, RecyclerView.VERTICAL, false)
            adapter = adapterMyPokemon
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataMyPokemon.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.pbLoading.isVisible = true
                        }

                        is Resource.Success -> {
                            binding.pbLoading.isVisible = false
                            adapterMyPokemon.saveData(it.data)
                        }

                        is Resource.Error -> {
                            binding.pbLoading.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private fun showInputDialog(
        context: Context,
        nickname: String,
        idPokemon: String,
        fibonacci: Int
    ) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_nickname_pokemon, null)
        builder.setView(view)

        val etNickname = view.findViewById<EditText>(R.id.etNickname)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        etNickname.setText(nickname)

        val dialog = builder.create()
        dialog.show()

        btnSave.setOnClickListener {
            val inputText = etNickname.text.toString()
            Toast.makeText(context, "Rename: $inputText Success", Toast.LENGTH_SHORT).show()
            viewModel.renamePokemon(inputText, idPokemon, fibonacci)
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

    private fun isPrime(num: Int): Boolean {
        if (num <= 1) {
            return false
        } else if (num <= 3) {
            return true
        } else if (num % 2 == 0 || num % 3 == 0) {
            return false
        }

        var i = 5
        while (i * i <= num) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false
            }
            i += 6
        }

        return true
    }

}