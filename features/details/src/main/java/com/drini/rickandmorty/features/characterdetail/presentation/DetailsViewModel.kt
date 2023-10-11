package com.drini.rickandmorty.features.characterdetail.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drini.rickandmorty.core.network.Request
import com.drini.rickandmorty.features.characterdetail.data.model.Character
import com.drini.rickandmorty.features.characterdetail.domain.CharacterDetailsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val interactor: CharacterDetailsInteractor
) : ViewModel() {

    val character = mutableStateOf<Character?>(null)
    var loadError = mutableStateOf(EMPTY)
    var isLoading = mutableStateOf(false)
    var episodeNameList = mutableStateOf<List<String>?>(null)

    fun getCharacterDetail(characterId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            when (val result = interactor.getCharacterDetails(characterId)) {
                is Request.Success -> {
                    loadError.value = EMPTY
                    isLoading.value = false
                    character.value = result.data

                    getAllEpisodesNamesList(result.data.episode)
                }

                is Error -> {
                    loadError.value = result.message ?: EMPTY
                    isLoading.value = false
                }

                else -> {
                    loadError.value = GENERIC_ERROR_MESSAGE
                    isLoading.value = false
                }
            }
        }
    }

    private fun getAllEpisodesNamesList(episodeList: List<String>) {
        for (episode in episodeList) {
            extractEpisodeID(episode)?.let { getEpisodeName(it) }
        }
    }

    private fun getEpisodeName(episodeId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            when (val result = interactor.getEpisodeName(episodeId)) {
                is Request.Success -> {
                    loadError.value = EMPTY
                    isLoading.value = false
                    setupEpisodeListName(result.data.name)
                }

                is Error -> {
                    loadError.value = result.message ?: EMPTY
                    isLoading.value = false
                }

                else -> {
                    loadError.value = GENERIC_ERROR_MESSAGE
                    isLoading.value = false
                }
            }
        }
    }

    private fun extractEpisodeID(url: String): Int? =
        Regex("\\d+\$").find(url)?.value?.toIntOrNull()

    private fun setupEpisodeListName(newEpisodeName: String) {
        val currentList = episodeNameList.value
        val updatedList = if (currentList == null) {
            listOf(newEpisodeName)
        } else {
            currentList + newEpisodeName
        }
        episodeNameList.value = updatedList
    }


    companion object {
        const val EMPTY = ""
        const val GENERIC_ERROR_MESSAGE = "Something went wrong..."
    }
}
