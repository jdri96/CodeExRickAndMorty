package com.drini.rickandmorty.features.characterlist.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.drini.rickandmorty.core.extensions.getStatusColor
import com.drini.rickandmorty.core.navigation.DETAIL_NAV
import com.drini.rickandmorty.core.theme.CharacterImageHeight
import com.drini.rickandmorty.core.theme.CharacterImageOffset
import com.drini.rickandmorty.core.theme.CharacterStatusSize
import com.drini.rickandmorty.core.theme.FontLarge
import com.drini.rickandmorty.core.theme.FontMedium
import com.drini.rickandmorty.core.theme.PROGRESS_BAR_SCALE
import com.drini.rickandmorty.core.theme.PaddingLarge
import com.drini.rickandmorty.core.theme.PaddingMedium
import com.drini.rickandmorty.core.theme.PaddingSmall
import com.drini.rickandmorty.core.theme.RoundedCorner
import com.drini.rickandmorty.core.theme.ShadowElevation
import com.drini.rickandmorty.core.theme.SpacerLarge
import com.drini.rickandmorty.core.theme.SpacerMedium
import com.drini.rickandmorty.core.theme.SpacerSmall
import com.drini.rickandmorty.core.theme.Zero
import com.drini.rickandmorty.features.characterlist.data.model.Character
import com.drini.rickandmorty.features.list.R

@Composable
fun CharacterListScreen(
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            val context = LocalContext.current
            Spacer(modifier = Modifier.height(SpacerLarge))
            Image(painter = painterResource(R.drawable.rick_and_morty_logo),
                contentDescription = stringResource(R.string.character_list_logo_content_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        Toast
                            .makeText(context, "Instrumented click performed!", Toast.LENGTH_LONG)
                            .show()
                    })
            Spacer(modifier = Modifier.height(SpacerSmall))
            CharacterList(navController = navController)
        }
    }
}

@Composable
fun CharacterList(
    navController: NavController,
    viewModel: ListViewModel = hiltViewModel()
) {
    val characterList by remember { viewModel.characterList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn(contentPadding = PaddingValues(PaddingLarge)) {
        val itemCount = if (characterList.size % EVEN_CHECK == EVEN_RESULT) {
            characterList.size / MAX_ELEMENTS_PER_ROW
        } else {
            characterList.size / MAX_ELEMENTS_PER_ROW + MIN_ELEMENTS_PER_ROW
        }

        items(itemCount) {
            val validStates = !endReached && !isLoading
            if (it >= itemCount - ITEM_OFFSET && validStates) {
                viewModel.loadCharacters()
            }
            CharacterRow(rowIndex = it, entries = characterList, navController = navController)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadCharacters()
            }
        }
    }
}

@Composable
fun CharacterEntry(
    character: Character,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(ShadowElevation, RoundedCornerShape(RoundedCorner))
            .clip(RoundedCornerShape(RoundedCorner))
            .background(MaterialTheme.colors.surface)
            .semantics { testTag = "list_screen_box" }
            .clickable {
                navController.navigate(String.format(DETAIL_NAV, character.id))
            }
    ) {
        Column {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.image)
                    .crossfade(true)
                    .build(),
                loading = {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.scale(PROGRESS_BAR_SCALE)
                    )
                },
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(CharacterImageHeight)
                    .offset(y = CharacterImageOffset)
            )
            Text(
                text = character.name,
                fontWeight = FontWeight.Bold,
                fontSize = FontLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .semantics { testTag = character.name }
                    .fillMaxWidth()
                    .padding(horizontal = PaddingMedium),
                maxLines = MAX_LINES,
                overflow = TextOverflow.Ellipsis
            )
            CharacterStatus(character)
            Spacer(modifier = Modifier.height(SpacerMedium))
        }
    }
}

@Composable
fun CharacterStatus(
    character: Character
) {
    Row {
        Box(
            modifier = Modifier
                .padding(PaddingMedium, PaddingSmall, Zero, Zero)
                .size(CharacterStatusSize)
                .clip(CircleShape)
                .background(character.status.getStatusColor())
        )
        Text(
            text = String.format(
                stringResource(R.string.character_list_status_format),
                character.status,
                character.species
            ),
            fontSize = FontMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingMedium),
            maxLines = MAX_LINES,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CharacterRow(
    rowIndex: Int,
    entries: List<Character>,
    navController: NavController
) {
    Column {
        Row {
            CharacterEntry(
                character = entries[rowIndex * MAX_ELEMENTS_PER_ROW],
                navController = navController,
                modifier = Modifier.weight(WEIGHT_DEFAULT)
            )
            Spacer(modifier = Modifier.width(SpacerMedium))
            if (entries.size >= rowIndex * MAX_ELEMENTS_PER_ROW + MAX_ELEMENTS_PER_ROW) {
                CharacterEntry(
                    character = entries[rowIndex * MAX_ELEMENTS_PER_ROW + MIN_ELEMENTS_PER_ROW],
                    navController = navController,
                    modifier = Modifier.weight(WEIGHT_DEFAULT)
                )
            } else {
                Spacer(modifier = Modifier.weight(WEIGHT_DEFAULT))
            }
        }
        Spacer(modifier = Modifier.height(SpacerMedium))
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = FontLarge)
        Spacer(modifier = Modifier.height(SpacerSmall))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.character_list_retry))
        }
    }
}
