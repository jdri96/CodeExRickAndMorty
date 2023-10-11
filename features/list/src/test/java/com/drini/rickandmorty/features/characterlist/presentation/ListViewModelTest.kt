package com.drini.rickandmorty.features.characterlist.presentation

import com.drini.rickandmorty.core.network.Request
import com.drini.rickandmorty.features.characterlist.data.model.Character
import com.drini.rickandmorty.features.characterlist.data.model.CharacterListInfo
import com.drini.rickandmorty.features.characterlist.data.model.CharacterListResponse
import com.drini.rickandmorty.features.characterlist.domain.CharacterListInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.random.Random

@ExperimentalCoroutinesApi
internal class ListViewModelTest {

    @Mock
    private lateinit var interactor: CharacterListInteractor
    private lateinit var viewModel: ListViewModel
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ListViewModel(interactor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `loadCharacters successfully handles success status`() = runTest {
        val mockResp = CharacterListResponse(
            info = CharacterListInfo(pages = Random.nextInt()), results = listOf(
                mockedCharacter,
                mockedCharacter
            )
        )
        `when`(interactor.getCharacterList(anyInt())).thenReturn(
            Request.Success<CharacterListResponse>(
                data = mockResp
            )
        )

        viewModel.loadCharacters()

        assert(
            viewModel.loadError.value.isEmpty() &&
                    !viewModel.isLoading.value &&
                    viewModel.characterList.value.contains(mockResp.results.last())
        )
    }

    @Test
    fun `loadCharacters successfully loads generic error`() = runTest {
        `when`(interactor.getCharacterList(anyInt())).thenReturn(
            Request.Exception(
                e = Throwable(
                    message = "err"
                )
            )
        )

        viewModel.loadCharacters()

        assert(viewModel.loadError.value.isNotEmpty())
    }

    @Test
    fun `loadCharacters successfully loads error`() = runTest {
        `when`(interactor.getCharacterList(anyInt())).thenReturn(
            Request.Error<CharacterListResponse>(code = 404, message = ERROR_MESSAGE)
        )

        viewModel.loadCharacters()

        assert(
            (viewModel.loadError.value.isNotEmpty())
                    && (viewModel.loadError.value != GENERIC_ERROR_MESSAGE)
        )
    }

    companion object {
        const val GENERIC_ERROR_MESSAGE = "Something went wrong..."
        const val ERROR_MESSAGE = "ERROR"
        private const val RAND_STRING = "TEST"
        val mockedCharacter = Character(
            id = Random.nextInt(),
            name = RAND_STRING,
            status = RAND_STRING,
            species = RAND_STRING,
            image = RAND_STRING
        )
    }
}