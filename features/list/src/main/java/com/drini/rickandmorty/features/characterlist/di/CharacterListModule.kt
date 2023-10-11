package com.drini.rickandmorty.features.characterlist.di

import com.drini.rickandmorty.features.characterlist.data.CharacterListRepository
import com.drini.rickandmorty.features.characterlist.data.CharacterListRepositoryImpl
import com.drini.rickandmorty.features.characterlist.data.CharacterListService
import com.drini.rickandmorty.features.characterlist.domain.CharacterListInteractor
import com.drini.rickandmorty.features.characterlist.domain.CharacterListInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CharacterListModule {

    @Provides
    @Singleton
    fun providesCharacterListService(
        okHttpClient: OkHttpClient,
        @Named("BaseUrl") baseUrl: String
    ): CharacterListService = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(CharacterListService::class.java)

    @Provides
    @Singleton
    fun provideCharacterListRepository(
        service: CharacterListService
    ): CharacterListRepository = CharacterListRepositoryImpl(service)

    @Provides
    @Singleton
    fun provideCharacterListInteractor(
        repository: CharacterListRepository
    ): CharacterListInteractor = CharacterListInteractorImpl(repository)
}
