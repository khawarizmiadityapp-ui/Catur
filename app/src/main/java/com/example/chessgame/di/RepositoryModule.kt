package com.example.chessgame.di

import com.example.chessgame.data.repository.ChessRepositoryImpl
import com.example.chessgame.domain.repository.ChessRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChessRepository(impl: ChessRepositoryImpl): ChessRepository
}
