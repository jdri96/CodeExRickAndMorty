package com.drini.rickandmorty.features.characterlist.data

import com.drini.rickandmorty.core.network.Request
import com.drini.rickandmorty.features.characterlist.data.model.Character
import com.drini.rickandmorty.features.characterlist.data.model.CharacterListInfo
import com.drini.rickandmorty.features.characterlist.data.model.CharacterListResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response
import kotlin.random.Random


internal class ListRepositoryTest {

    @Mock
    private lateinit var service: CharacterListService

    private lateinit var repository: CharacterListRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = CharacterListRepositoryImpl(service)
    }

    @Test
    fun `getCharacterList should return data from service`() = runBlocking {
        val page = Random.nextInt()
        val expectedResponse = CharacterListResponse(
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
        val response = Response.success(expectedResponse)

        `when`(service.getCharacterList(page)).thenReturn(response)

        val result = repository.getCharacterList(page)

        if (result is Request.Success) {
            assertEquals(expectedResponse, result.data)
        } else {
            fail("Expected a CharacterListResponse, but got ${result.javaClass.simpleName}")
        }
    }

    companion object {
        const val RAND_STRING = "TEST"
    }
}