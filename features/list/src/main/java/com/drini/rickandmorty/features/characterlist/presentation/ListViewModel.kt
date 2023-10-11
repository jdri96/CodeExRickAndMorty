package com.drini.rickandmorty.features.characterlist.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drini.rickandmorty.core.network.Request
import com.drini.rickandmorty.features.characterlist.data.model.Character
import com.drini.rickandmorty.features.characterlist.domain.CharacterListInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val interactor: CharacterListInteractor
) : ViewModel() {

    var characterList = mutableStateOf<List<Character>>(listOf())
    var loadError = mutableStateOf(EMPTY)
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var curPage = FIRST_PAGE

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            isLoading.value = true

            when (val result = interactor.getCharacterList(curPage)) {
                is Request.Success -> {
                    curPage++

                    endReached.value = curPage >= result.data.info.pages
                    loadError.value = EMPTY
                    isLoading.value = false
                    characterList.value += result.data.results
                }
//remove request
                is Request.Error -> {
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

    companion object {
        const val FIRST_PAGE = 1
        const val EMPTY = ""
        const val GENERIC_ERROR_MESSAGE = "Something went wrong..."
    }
}
