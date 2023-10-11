package com.drini.rickandmorty.features.characterdetail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.drini.details.R
import com.drini.rickandmorty.core.extensions.getStatusColor
import com.drini.rickandmorty.core.theme.ArrowSize
import com.drini.rickandmorty.core.theme.CharacterDetailLoadingSize
import com.drini.rickandmorty.core.theme.CharacterDetailSectionOffset
import com.drini.rickandmorty.core.theme.CharacterStatusSize
import com.drini.rickandmorty.core.theme.DarkBrown
import com.drini.rickandmorty.core.theme.FontDetailLarge
import com.drini.rickandmorty.core.theme.FontLarge
import com.drini.rickandmorty.core.theme.FontMedium
import com.drini.rickandmorty.core.theme.LightGreen
import com.drini.rickandmorty.core.theme.LightYellow
import com.drini.rickandmorty.core.theme.PROGRESS_BAR_SCALE
import com.drini.rickandmorty.core.theme.PaddingLarge
import com.drini.rickandmorty.core.theme.PaddingMedium
import com.drini.rickandmorty.core.theme.PaddingSmall
import com.drini.rickandmorty.core.theme.RoundedCorner
import com.drini.rickandmorty.core.theme.ShadowElevation
import com.drini.rickandmorty.core.theme.SpacerMedium
import com.drini.rickandmorty.core.theme.SpacerSmall
import com.drini.rickandmorty.core.theme.Zero
import com.drini.rickandmorty.features.characterdetail.data.model.Character
import java.util.Locale

@Composable
fun DetailsScreen(
    characterId: Int, navController: NavController, viewModel: DetailsViewModel = hiltViewModel()
) {
    val character by remember { viewModel.character }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    character ?: viewModel.getCharacterDetail(characterId)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen)
            .padding(bottom = PaddingLarge)
            .semantics { testTag = "details_screen_box" }
    ) {
        CharacterDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(DETAIL_TOP_FRACTION)
                .align(Alignment.TopCenter)
        )
        CharacterDetailStateWrapper(
            character = character,
            loadError = loadError,
            isLoading = isLoading,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = TOP_PADDING.dp + CHARACTER_IMAGE_SIZE.dp / OFFSET_DIVIDER,
                    start = PaddingLarge,
                    end = PaddingLarge,
                    bottom = PaddingLarge
                )
                .shadow(ShadowElevation, RoundedCornerShape(RoundedCorner))
                .clip(RoundedCornerShape(RoundedCorner))
                .background(MaterialTheme.colors.surface)
                .padding(PaddingLarge)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(CharacterDetailLoadingSize)
                .align(Alignment.Center)
                .padding(
                    top = TOP_PADDING.dp + CHARACTER_IMAGE_SIZE.dp / OFFSET_DIVIDER,
                    start = PaddingLarge,
                    end = PaddingLarge,
                    bottom = PaddingLarge
                )
        )
        Box(
            contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()
        ) {
            CharacterImage(character)
        }
    }
}

@Composable
fun CharacterImage(character: Character?) {
    character?.image?.let {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(it).crossfade(true).build(),
            loading = {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(PROGRESS_BAR_SCALE)
                )
            },
            contentDescription = character.name,
            modifier = Modifier
                .size(CHARACTER_IMAGE_SIZE.dp)
                .offset(y = TOP_PADDING.dp)
                .shadow(ShadowElevation, RoundedCornerShape(RoundedCorner))
                .clip(RoundedCornerShape(RoundedCorner))
        )
    }
}

@Composable
fun CharacterDetailTopSection(
    navController: NavController, modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart, modifier = modifier.background(
            Brush.verticalGradient(
                listOf(
                    if (isSystemInDarkTheme()) DarkBrown else LightYellow, Color.Transparent
                )
            )
        )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
            modifier = Modifier
                .size(ArrowSize)
                .offset(SpacerMedium, SpacerMedium)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun CharacterDetailStateWrapper(
    character: Character?,
    loadError: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when {
        character != null -> CharacterDetailSection(character = character, modifier = modifier)
        loadError.isNotEmpty() -> Text(
            text = loadError,
            color = Color.Red,
            modifier = modifier
        )

        isLoading -> CircularProgressIndicator(
            color = MaterialTheme.colors.primary,
            modifier = loadingModifier
        )
    }
}

@Composable
fun CharacterDetailSection(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel(),
    character: Character,
) {
    val episodesNameList by remember { viewModel.episodeNameList }
    val loadError by remember { viewModel.loadError }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = CharacterDetailSectionOffset)
    ) {
        item {
            CharacterDetailName(character)
            Spacer(modifier = Modifier.height(SpacerSmall))
            CharacterDetailItem(
                label = stringResource(id = R.string.character_detail_species),
                value = character.species
            )
            Spacer(modifier = Modifier.height(SpacerSmall))
            CharacterDetailStatus(character)
            Spacer(modifier = Modifier.height(SpacerSmall))
            CharacterDetailItem(
                label = stringResource(id = R.string.character_detail_origin),
                value = character.origin.name
            )
            Spacer(modifier = Modifier.height(SpacerSmall))
            CharacterDetailItem(
                label = stringResource(id = R.string.character_detail_location),
                value = character.location.name
            )
            Spacer(modifier = Modifier.height(SpacerSmall))
            CharacterEpisodesLabelItem()
        }
        episodesNameList?.takeIf { loadError.isEmpty() }?.let { episodes ->
            items(episodes) { episodeName ->
                CharacterDetailEpisodesList(episodeName)
            }
        }
    }
}

@Composable
private fun CharacterDetailName(character: Character) {
    Text(
        text = character.name.replaceFirstChar { it.uppercase(Locale.ROOT) },
        fontWeight = FontWeight.Bold,
        fontSize = FontDetailLarge,
        textAlign = TextAlign.Center,
        color = if (isSystemInDarkTheme()) Color.White else Color.Black
    )
}

@Composable
private fun CharacterDetailStatus(character: Character) {
    Row {
        Box(
            modifier = Modifier
                .padding(PaddingMedium, PaddingSmall, Zero, Zero)
                .size(CharacterStatusSize)
                .clip(CircleShape)
                .background(character.status.getStatusColor())
        )
        Text(
            text = stringResource(R.string.character_detail_status),
            fontWeight = FontWeight.Bold,
            fontSize = FontMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingMedium),
            color = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }
    Text(
        text = character.status,
        fontSize = FontLarge,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingMedium),
        maxLines = MAX_LINES,
        overflow = TextOverflow.Ellipsis,
        color = if (isSystemInDarkTheme()) Color.White else Color.Black
    )
}

@Composable
private fun CharacterDetailItem(label: String, value: String) {
    Text(
        text = label,
        fontWeight = FontWeight.Bold,
        fontSize = FontMedium,
        textAlign = TextAlign.Start,
        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SpacerSmall)
    )
    Text(
        text = value,
        fontSize = FontLarge,
        textAlign = TextAlign.Start,
        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SpacerSmall),
        maxLines = MAX_LINES,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun CharacterEpisodesLabelItem() {
    Text(
        text = stringResource(id = R.string.character_detail_episodes),
        fontWeight = FontWeight.Bold,
        fontSize = FontMedium,
        textAlign = TextAlign.Start,
        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SpacerSmall)
    )
}

@Composable
private fun CharacterDetailEpisodesList(
    episodeName: String
) {
    Text(
        text = episodeName,
        fontWeight = FontWeight.Normal,
        fontSize = FontMedium,
        textAlign = TextAlign.Start,
        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
        modifier = Modifier
            .padding(vertical = PaddingSmall)
            .fillMaxWidth()
    )
}