package com.drini.rickandmorty.features.characterdetail.di

import com.drini.rickandmorty.features.characterdetail.data.CharacterDetailsRepository
import com.drini.rickandmorty.features.characterdetail.data.CharacterDetailsRepositoryImpl
import com.drini.rickandmorty.features.characterdetail.data.CharacterDetailsService
import com.drini.rickandmorty.features.characterdetail.domain.CharacterDetailsInteractor
import com.drini.rickandmorty.features.characterdetail.domain.CharacterDetailsInteractorImpl
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
object CharacterDetailsModule {

    @Provides
    @Singleton
    fun providesCharacterDetailsService(
        okHttpClient: OkHttpClient,
        @Named("BaseUrl") baseUrl: String
    ): CharacterDetailsService = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(CharacterDetailsService::class.java)

    @Provides
    @Singleton
    fun provideCharacterDetailsRepository(
        service: CharacterDetailsService
    ): CharacterDetailsRepository = CharacterDetailsRepositoryImpl(service)

    @Provides
    @Singleton
    fun provideCharacterDetailsInteractor(
        repository: CharacterDetailsRepository
    ): CharacterDetailsInteractor = CharacterDetailsInteractorImpl(repository)
}
