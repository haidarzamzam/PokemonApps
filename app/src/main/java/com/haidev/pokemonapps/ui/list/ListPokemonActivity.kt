package com.haidev.pokemonapps.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haidev.pokemonapps.R
import com.haidev.pokemonapps.databinding.ActivityListPokemonBinding
import com.haidev.pokemonapps.ui.detail.DetailPokemonActivity
import com.haidev.pokemonapps.ui.favorite.FavoritePokemonActivity
import com.haidev.pokemonapps.ui.list.adapter.ItemListPokemonAdapter
import com.haidev.pokemonapps.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListPokemonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPokemonBinding
    private val viewModel: ListPokemonViewModel by viewModels<ListPokemonViewModel>()

    private val adapterMain by lazy {
        ItemListPokemonAdapter {
            val intent = Intent(this, DetailPokemonActivity::class.java)
            intent.putExtra("idPokemon", it.id)
            startActivity(intent)
        }
    }
    private val paginationScrollListener by lazy {
        PaginationScrollListener(
            binding.rvPokemon.layoutManager as LinearLayoutManager
        ) {
            viewModel.offsetValue.value += 20
            viewModel.getPokemon()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPokemonBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        initUI()
        initData()
    }

    private fun initUI() {
        setSupportActionBar(binding.toolbar)

        binding.rvPokemon.apply {
            layoutManager =
                LinearLayoutManager(this@ListPokemonActivity, RecyclerView.VERTICAL, false)
            adapter = adapterMain
            addOnScrollListener(paginationScrollListener)
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataPokemon.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.pbLoading.isVisible = true
                        }

                        is Resource.Success -> {
                            binding.pbLoading.isVisible = false
                            adapterMain.saveData(it.data)
                            paginationScrollListener.setLoaded()
                            paginationScrollListener.setLastPage(it.data?.isEmpty() == true)
                        }

                        is Resource.Error -> {
                            binding.pbLoading.isVisible = false
                        }
                    }
                }
            }
        }
    }

    class PaginationScrollListener(
        private val layoutManager: LinearLayoutManager,
        private val loadMoreItems: () -> Unit
    ) : RecyclerView.OnScrollListener() {

        private var isLoading = false
        private var isLastPage = false

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                ) {
                    isLoading = true
                    loadMoreItems()
                }
            }
        }

        fun setLoaded() {
            isLoading = false
        }

        fun setLastPage(lastPage: Boolean) {
            isLastPage = lastPage
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                startActivity(Intent(this@ListPokemonActivity, FavoritePokemonActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}