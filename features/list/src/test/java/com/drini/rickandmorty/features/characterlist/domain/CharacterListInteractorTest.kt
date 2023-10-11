package com.drini.rickandmorty.features.characterlist.domain

import com.drini.rickandmorty.core.network.Request
import com.drini.rickandmorty.features.characterlist.data.CharacterListRepository
import com.drini.rickandmorty.features.characterlist.data.model.Character
import com.drini.rickandmorty.features.characterlist.data.model.CharacterListInfo
import com.drini.rickandmorty.features.characterlist.data.model.CharacterListResponse
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.random.Random

internal class CharacterListInteractorTest {

    @Mock
    private lateinit var repository: CharacterListRepository

    private lateinit var interactor: CharacterListInteractor

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        interactor = CharacterListInteractorImpl(repository)
    }

    @Test
    fun `getCharacterList should return data from repository`() = runBlocking {
        val page = Random.nextInt()
        val mockResponse = CharacterListResponse(
            info = CharacterListInfo(pages = Random.nextInt()), results = listOf(
                Character(
                    id = Random.nextInt(),
                    name = RAND_STRING,
                    status = RAND_STRING,
                    species = RAND_STRING,
                    image = RAND_STRING
                )
            )
        )
        `when`(repository.getCharacterList(page)).thenReturn(Request.Success(mockResponse))

        val result = interactor.getCharacterList(page)

        assert(result is Request.Success)
    }

    @Test
    fun `getCharacterList in case of error should return Error`() = runBlocking {
        val page = Random.nextInt()
        `when`(repository.getCharacterList(page)).thenReturn(
            Request.Error(
                code = Random.nextInt(),
                message = RAND_STRING
            )
        )

        val result = interactor.getCharacterList(page)

        assert(result is Request.Error)
    }

    companion object {
        const val RAND_STRING = "TEST"
    }
}