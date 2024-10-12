package fr.floz.epicurian.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.floz.epicurian.R
import fr.floz.epicurian.ui.home.HomeViewModel
import fr.floz.epicurian.ui.home.SettingsScreen
import fr.floz.epicurian.ui.restaurant.RestaurantList
import fr.floz.epicurian.ui.restaurant.RestaurantMapScreen
import fr.floz.epicurian.ui.theme.EpicurianTheme
import kotlin.enums.EnumEntries


@Composable
fun MainContent() {

    EpicurianTheme {
        val navController = rememberNavController()
        var currentDestination by rememberSaveable { mutableStateOf(EpicureanTabs.LIST) }

        Scaffold (
            // ajout d'une barre de navigation en bas de l'écran
            bottomBar = { EpicureanBottomBar(navController, EpicureanTabs.entries) },
            topBar = { HomeAppBar(title = stringResource(currentDestination.label)) },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
//            FloatingActionButton(onClick = onRestaurantClick /*{
//                showBottomSheet = true
//                //restaurantUiState.addRestaurant("Nouveau restaurant", "Toto", "Paris")
//            }*/) {
//                Icon(Icons.Filled.Add, "Ajout d'un restaurant")
//            }
            }
        ) { innerPadding ->

            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = EpicureanNavigation.LIST_ROUTE
            ) {

                composable(EpicureanNavigation.SETTINGS_ROUTE) {
                    SettingsScreen(
                        contentPadding = innerPadding,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // home
                composable(EpicureanNavigation.LIST_ROUTE) {
                    val viewModel = hiltViewModel<HomeViewModel>()
                    val state by viewModel.state.collectAsState()
                    RestaurantList(
                        state = state,
                        onEvent = viewModel::onEvent,
                        innerPadding = innerPadding
//                onRestaurantClick = { restaurantId ->
//                    navController.navigate("${EpicureanNavigation.DETAIL_ROUTE}/{$restaurantId}")
//                }
                    )
                }

                composable(EpicureanNavigation.MAP_ROUTE) {
                    RestaurantMapScreen()
                }

                // détails d'un restaurant
//                composable(
//                    route = "${EpicureanNavigation.DETAIL_ROUTE}/{${EpicureanNavigation.RESTAURANT_ID_KEY}}",
//                    arguments = listOf(navArgument(EpicureanNavigation.RESTAURANT_ID_KEY) { NavType.LongType })
//                ) { backStackEntry ->
//                    val restaurantId: Long = backStackEntry.arguments?.getLong(EpicureanNavigation.RESTAURANT_ID_KEY) ?: 0L
//
//                    RestaurantScreen(
//                        restaurantId = restaurantId,
//                        onMapClick = { /* TODO */ }
//                    )
//                }
            }
        }
    }

}

@Composable
fun EpicureanBottomBar(
    navController: NavController,
    tabs: EnumEntries<EpicureanTabs>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?: EpicureanNavigation.LIST_ROUTE

    NavigationBar {
        tabs.forEach { tab ->
            // affichage d'icone pour chaque écran dans la barre de navigation
            NavigationBarItem(
                selected = currentRoute == tab.route,
                icon = { Icon(tab.icon, contentDescription = null) },
                label = { Text(stringResource(id = tab.label)) },
                onClick = {
                    if (tab.route != currentRoute) {
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(title) }
    )
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

object EpicureanNavigation {
    const val LIST_ROUTE    = "list"
    const val DETAIL_ROUTE  = "detail"
    const val MAP_ROUTE     = "map"
    const val SETTINGS_ROUTE   = "about"

    const val RESTAURANT_ID_KEY = "restaurantId"
}

enum class EpicureanTabs(
    @StringRes val label: Int,
    val icon: ImageVector,
    val route: String
) {
    LIST(R.string.restaurants, Icons.AutoMirrored.Filled.List, EpicureanNavigation.LIST_ROUTE),
    MAP(R.string.map, Icons.Filled.LocationOn, EpicureanNavigation.MAP_ROUTE),
    SETTINGS(R.string.settings, Icons.Filled.Settings, EpicureanNavigation.SETTINGS_ROUTE)
}