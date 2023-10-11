package rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.drini.rickandmorty.core.navigation.DETAIL_ID
import com.drini.rickandmorty.core.navigation.DETAIL_ROUTE
import com.drini.rickandmorty.core.navigation.LIST_SCREEN
import com.drini.rickandmorty.core.theme.RickAndMortyTheme
import com.drini.rickandmorty.features.characterdetail.presentation.DetailsScreen
import com.drini.rickandmorty.features.characterlist.presentation.CharacterListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickAndMortyTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController, startDestination = LIST_SCREEN
                ) {
                    composable(LIST_SCREEN) {
                        CharacterListScreen(navController = navController)
                    }
                    composable(
                        route = DETAIL_ROUTE,
                        arguments = listOf(navArgument(DETAIL_ID) {
                            type = NavType.IntType
                        })
                    )
                    {
                        val characterId = remember {
                            it.arguments?.getInt(DETAIL_ID) ?: DEFAULT_CHARACTER_ID
                        }

                        DetailsScreen(
                            characterId = characterId, navController = navController
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val DEFAULT_CHARACTER_ID = 1
    }
}