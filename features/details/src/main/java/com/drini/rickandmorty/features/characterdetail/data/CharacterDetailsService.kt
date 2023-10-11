package com.drini.rickandmorty.features.characterdetail.data

import com.drini.rickandmorty.features.characterdetail.data.model.Character
import com.drini.rickandmorty.features.characterdetail.data.model.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterDetailsService {

    @GET(CHARACTER_DETAIL_PATH)
    suspend fun getCharacterDetails(
        @Path(CHARACTER_ID) characterId: Int
    ): Response<Character>

    @GET(EPISODE_NAME_PATH)
    suspend fun getEpisodeName(
        @Path(EPISODE_ID) episodeId: Int
    ): Response<Episode>

    companion object {
        const val CHARACTER_ID = "characterId"
        const val CHARACTER_DETAIL_PATH = "character/{$CHARACTER_ID}"

        const val EPISODE_ID = "episodeId"
        const val EPISODE_NAME_PATH = "episode/{$EPISODE_ID}"
    }
}
