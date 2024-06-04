package com.haidev.pokemonapps.di

import android.content.Context
import androidx.room.Room
import com.haidev.pokemonapps.data.local.entity.PokemonEntity
import com.haidev.pokemonapps.data.local.room.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, PokemonDatabase::class.java, "Pokemon.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideDao(db: PokemonDatabase) = db.pokemonDao()

    @Provides
    fun provideEntity() = PokemonEntity()
}