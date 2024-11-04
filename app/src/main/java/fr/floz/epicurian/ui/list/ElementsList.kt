package fr.floz.epicurian.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import fr.floz.epicurian.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantList(
    state: ElementsListUiState,
    onEvent: (ElementsListEvent) -> Unit,
    onAddRestaurantClicked: () -> Unit,
    onSettingsClick: () -> Unit,
    onRestaurantSelected: (Long) -> Unit,
    onMapClick: () -> Unit
) {
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRestaurantClicked,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Icon(Icons.Filled.Add, "Ajout d'un restaurant")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Recherche"
                        )
                    }

                    IconButton(onClick = onMapClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_map),
                            contentDescription = "Afficher la carte"
                        )
                    }

                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Menu"
                        )
                    }
                }
            )
        }
    ) {

        LazyColumn(
            contentPadding = it,
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = state.elements) { restaurant ->
                ListItem(
                    headlineContent = { Text(restaurant.name) },
                    overlineContent = { Text("TODO") },
                    supportingContent = { Text(text = ""+restaurant.id) },
                    leadingContent = {
                        Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "Image du restaurant")
                    },
                    modifier = Modifier.clickable(onClick = { onRestaurantSelected(restaurant.id) } )
                )
                HorizontalDivider()
            }
        }
    }

}
