package com.drini.rickandmorty.features.characterlist.data

import com.drini.rickandmorty.core.network.Request
import com.drini.rickandmorty.core.network.handleRequest
import com.drini.rickandmorty.features.characterlist.data.model.CharacterListResponse
import javax.inject.Inject

interface CharacterListRepository {
    suspend fun getCharacterList(page: Int): Request<CharacterListResponse>
}

class CharacterListRepositoryImpl @Inject constructor(
    private val service: CharacterListService
) : CharacterListRepository {

    override suspend fun getCharacterList(page: Int): Request<CharacterListResponse> =
        handleRequest { service.getCharacterList(page) }
}
