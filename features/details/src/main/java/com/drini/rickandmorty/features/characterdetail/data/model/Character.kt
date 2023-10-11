package com.drini.rickandmorty.features.characterdetail.data.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String,
    val origin: CharacterOrigin,
    val location: CharacterLocation,
    val episode: List<String>
)
