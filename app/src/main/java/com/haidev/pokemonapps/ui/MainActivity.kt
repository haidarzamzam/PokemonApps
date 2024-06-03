package com.haidev.pokemonapps.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.haidev.pokemonapps.databinding.ActivityMainBinding
import com.haidev.pokemonapps.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()

    private val adapterMain by lazy { ItemMainAdapter() }
    private val paginationScrollListener by lazy {
        PaginationScrollListener(
            binding.rvPokemon.layoutManager as LinearLayoutManager
        ) {
            mainViewModel.offsetValue.value += 20
            mainViewModel.getPokemon()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.getRoot()
        setContentView(view)

        binding.rvPokemon.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = adapterMain
            addOnScrollListener(paginationScrollListener)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.dataPokemon.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.pbLoading.isVisible = true
                        }

                        is Resource.Success -> {
                            binding.pbLoading.isVisible = false
                            adapterMain.saveData(it.data)
                            paginationScrollListener.setLoaded()
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
}