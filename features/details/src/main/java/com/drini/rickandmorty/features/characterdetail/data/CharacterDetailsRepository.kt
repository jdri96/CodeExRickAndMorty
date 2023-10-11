package com.drini.rickandmorty.features.characterdetail.data

import com.drini.rickandmorty.core.network.Request
import com.drini.rickandmorty.core.network.handleRequest
import com.drini.rickandmorty.features.characterdetail.data.model.Character
import com.drini.rickandmorty.features.characterdetail.data.model.Episode
import javax.inject.Inject

interface CharacterDetailsRepository {
    suspend fun getCharacterDetails(characterId: Int): Request<Character>
    suspend fun getEpisodeName(episodeId: Int): Request<Episode>
}

class CharacterDetailsRepositoryImpl @Inject constructor(
    private val service: CharacterDetailsService
) : CharacterDetailsRepository {

    override suspend fun getCharacterDetails(characterId: Int): Request<Character> =
        handleRequest { service.getCharacterDetails(characterId) }

    override suspend fun getEpisodeName(episodeId: Int): Request<Episode> =
        handleRequest { service.getEpisodeName(episodeId) }

}