package com.drini.rickandmorty.features.characterdetail.domain

import com.drini.rickandmorty.core.network.Request
import com.drini.rickandmorty.features.characterdetail.data.CharacterDetailsRepository
import com.drini.rickandmorty.features.characterdetail.data.model.Character
import com.drini.rickandmorty.features.characterdetail.data.model.Episode
import javax.inject.Inject

interface CharacterDetailsInteractor {
    suspend fun getCharacterDetails(characterId: Int): Request<Character>
    suspend fun getEpisodeName(episodeId: Int): Request<Episode>
}

class CharacterDetailsInteractorImpl @Inject constructor(
    private val repository: CharacterDetailsRepository
) : CharacterDetailsInteractor {

    override suspend fun getCharacterDetails(characterId: Int): Request<Character> =
        repository.getCharacterDetails(characterId)

    override suspend fun getEpisodeName(episodeId: Int): Request<Episode> =
        repository.getEpisodeName(episodeId)

}