package com.drini.rickandmorty.features.characterlist.data.model

data class CharacterListResponse(
    val info: CharacterListInfo,
    val results: List<Character>
)
