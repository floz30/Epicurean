package fr.floz.epicurian.ui.restaurant

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import fr.floz.epicurian.R
import fr.floz.epicurian.ui.RestaurantEvent

// https://www.composables.com/tutorials/change-themes
// Pour changement en direct du thème de l'application

// https://developer.android.com/topic/libraries/architecture/datastore?hl=fr
// Stockage de données en local

// https://developer.android.com/guide/navigation/principles#fixed_start_destination
// https://medium.com/@KaushalVasava/navigation-in-jetpack-compose-full-guide-beginner-to-advanced-950c1133740
// Navigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantList(
    state: RestaurantListState,
    onEvent: (RestaurantEvent) -> Unit,
    innerPadding: PaddingValues
) {

    Surface {
        Column {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding,
                reverseLayout = true
            ) {
                items(items = state.restaurants) {restaurant ->
                    ListItem(
                        headlineContent = { Text(restaurant.name) },
                        overlineContent = { Text(restaurant.location) },
                        //supportingContent = { Text(restaurant.type) },
                        leadingContent = {
                            Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "Image du restaurant")
                        },
                        modifier = Modifier.clickable(onClick = { })
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
