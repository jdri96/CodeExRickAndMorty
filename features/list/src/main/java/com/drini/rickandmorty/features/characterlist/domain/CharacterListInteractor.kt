package com.drini.rickandmorty.features.characterlist.domain

import com.drini.rickandmorty.core.network.Request
import com.drini.rickandmorty.features.characterlist.data.CharacterListRepository
import com.drini.rickandmorty.features.characterlist.data.model.CharacterListResponse
import javax.inject.Inject

interface CharacterListInteractor {
    suspend fun getCharacterList(page: Int): Request<CharacterListResponse>
}

class CharacterListInteractorImpl @Inject constructor(
    private val repository: CharacterListRepository
) : CharacterListInteractor {

    override suspend fun getCharacterList(page: Int): Request<CharacterListResponse> =
        repository.getCharacterList(page)
}
