package fr.floz.epicurean.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fr.floz.epicurean.R
import fr.floz.epicurean.ui.creation.CreationHomeScreen
import fr.floz.epicurean.ui.creation.ElementCreationViewModel
import fr.floz.epicurean.ui.details.RestaurantScreen
import fr.floz.epicurean.ui.list.ElementsListViewModel
import fr.floz.epicurean.ui.list.RestaurantList
import fr.floz.epicurean.ui.settings.SettingsEvent
import fr.floz.epicurean.ui.settings.SettingsScreen
import fr.floz.epicurean.ui.settings.SettingsViewModel
import fr.floz.epicurean.ui.theme.EpicureanTheme
import kotlinx.serialization.Serializable

@Serializable
object ScreenList

@Serializable
data class ScreenDetail(
    val id: Long
)

@Serializable object CreationNavigation
@Serializable object CreationMap
@Serializable object CreationScreen

@Serializable
object ScreenSettings

@Serializable
data class ScreenSettingsHome(
    val settings: List<String>
)
@Serializable object ScreenSettingsAppearance
@Serializable object ScreenSettingsStorage
@Serializable object ScreenSettingsAbout

data class SettingsEntry(
    @StringRes val label: Int,
    val icon: ImageVector,
    val onClick: () -> Unit
)


@Composable
fun MainContent() {

    EpicureanTheme {
        val navController = rememberNavController()

        val settingsHomeContent by lazy { listOf(
            SettingsEntry(R.string.settings_appearance, Icons.Outlined.AccountCircle, onClick = {
                navController.navigate(ScreenSettingsAppearance)
            }),
            SettingsEntry(R.string.settings_data, Icons.Outlined.AccountCircle, onClick = {
                navController.navigate(ScreenSettingsStorage)
            }),
            SettingsEntry(R.string.settings_about, Icons.Outlined.Info, onClick = {
                navController.navigate(ScreenSettingsAbout)
            })
        )}



        NavHost(
            navController = navController,
            startDestination = ScreenList
        ) {

            composable<ScreenList> {
                val viewModel = hiltViewModel<ElementsListViewModel>()
                val state by viewModel.state.collectAsState()
                RestaurantList(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onAddRestaurantClicked = {
                        navController.navigate(CreationNavigation)
                    },
                    onSettingsClick = {
                        navController.navigate(ScreenSettings)
                    },
                    onRestaurantSelected = { id ->
                        navController.navigate(ScreenDetail(id))
                    },
                    onMapClick = {
                        /*navController.navigate(ScreenMap)*/
                    }
                )
            }

            composable<ScreenDetail> {
                val args = it.toRoute<ScreenDetail>()
                val viewModel = hiltViewModel<ElementsListViewModel>()
                val state by viewModel.state.collectAsState()
                val restaurant = state.elements.find { r ->
                    r.id == args.id
                }
                if (restaurant != null) {
                    RestaurantScreen(
                        element = restaurant,
                        onBackPressed = {
                            navController.popBackStack()
                        },
                        onMapClick = {
                            /*viewModel.onEvent(RestaurantEvent.ShowOnMap(args.id))
                            navController.navigate(ScreenMap)*/
                        }
                    )
                }

            }

            navigation<CreationNavigation>(
                startDestination = CreationMap
            ) {

                composable<CreationMap> {
                    // Affichage de la carte afin de sélectionner l'emplacement
                    val viewModel = it.sharedViewModel<ElementCreationViewModel>(navController)

                    CreationHomeScreen(
                        onEvent = viewModel::onEvent,
                        viewModel = viewModel,
                        onConfirmPressed = {
                            viewModel.setLocationSelectedOrSkipped(true)
                            navController.navigate(CreationScreen)
                        },
                        onBackPressed = {
                            navController.navigateUp()
                        }
                    )
                }
                composable<CreationScreen> {
                    // Remplissage des informations du restaurants
                    val viewModel = it.sharedViewModel<ElementCreationViewModel>(navController)

                    CreationHomeScreen(
                        onEvent = viewModel::onEvent,
                        viewModel = viewModel,
                        onConfirmPressed = {
                            viewModel.setLocationSelectedOrSkipped(false) // reset screen for future creation
                            navController.navigate(ScreenList)
                            // PROBLEME : le modal bottom sheet s'affiche sur l'écran de liste pendant 2 secondes
                            },
                        onBackPressed = {
                            viewModel.setLocationSelectedOrSkipped(false)
                            navController.navigateUp()
                        }
                    )
                }
            }

            navigation<ScreenSettings>(
                startDestination = ScreenSettingsHome(emptyList())
            ) {

                composable<ScreenSettingsHome> {
                    val viewModel = it.sharedViewModel<SettingsViewModel>(navController)
                    val state by viewModel.state.collectAsState()
                    SettingsScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        onBackPressed = { navController.popBackStack() },
                        content = settingsHomeContent
                    )
                }
                composable<ScreenSettingsAppearance> {
                    val viewModel = it.sharedViewModel<SettingsViewModel>(navController)
                    val state by viewModel.state.collectAsState()
                    SettingsScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                        onBackPressed = { navController.popBackStack() },
                        content = listOf(
                            SettingsEntry(R.string.settings_appearance_language, Icons.Outlined.AccountCircle, onClick = {
                                viewModel.onEvent(SettingsEvent.ShowLanguageDialog)
                            }),
                            SettingsEntry(R.string.settings_appearance_theme, Icons.Outlined.Build, onClick = {

                            })
                        )
                    )
                }

                composable<ScreenSettingsStorage> {
                    //val viewModel = it.sharedViewModel<SettingsViewModel>(navController)
                    Text("Storage")
                }

                composable<ScreenSettingsAbout> {
                    //val viewModel = it.sharedViewModel<SettingsViewModel>(navController)
                    Text("About")
                }
            }

        }
    }

}


@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}
