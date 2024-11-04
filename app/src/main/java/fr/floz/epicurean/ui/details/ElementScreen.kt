package fr.floz.epicurean.ui.details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.floz.epicurean.R
import fr.floz.epicurean.domain.entities.Element
import fr.floz.epicurean.domain.entities.ElementType

/**
 *
 * @param element item to display
 */
@Composable
fun RestaurantScreen(
    element: Element,
    onBackPressed: () -> Unit,
    onMapClick: () -> Unit
) {
    Surface {
        LazyColumn {
            item { RestaurantHeader(onBackPressed) }
            item { Headline(element.name, "TODO", onMapClick) }
            item { RestaurantInfo(element.type, true, true) }
            item { RestaurantDetailItem("TODO") }
            item { RestaurantDetailItem("https://google.com") }
            item {
                Card (
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row {
                        Text(text = "Type de cuisine servie", style = MaterialTheme.typography.titleMedium)
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        element.cuisines.forEach {
                            Card(Modifier.clip(RectangleShape)) {
                                Text(it.label, Modifier.padding(vertical = 10.dp, horizontal = 16.dp))
                            }
                            Spacer(Modifier.width(8.dp))
                        }
                    }


                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RestaurantHeader(
    onBackPressed: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour en arrière"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Menu"
                )
            }
        }
    )
}

@Composable
private fun Headline(
    name: String,
    address: String,
    onLocationClick: () -> Unit
) {
    Card (
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = address)
            IconButton(onClick = onLocationClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_location_pin_filled),
                    contentDescription = stringResource(R.string.action_locate_restaurant),
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

    }
}

@Composable
private fun RestaurantInfo(
    restaurantType: ElementType,
    isOpen: Boolean,
    isWithDisabledAccess: Boolean
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {


        item {
            CircleInfo(
                icon = restaurantType.icon,
                label = restaurantType.label,
                iconDescription = R.string.restaurant_info_type
            )
        }
        item {
            CircleInfo(
                icon = if (isOpen) R.drawable.ic_shop_open else R.drawable.ic_shop_closed,
                label = if (isOpen) R.string.restaurant_info_open else R.string.restaurant_info_closed,
                iconDescription = R.string.restaurant_info_opening_hours
            )
        }
        item {
            CircleInfo(
                icon = R.drawable.ic_wheelchair,
                label = R.string.restaurant_info_wheelchair_label,
                iconDescription = R.string.restaurant_info_wheelchair_description
            )
        }
    }
}


@Composable
private fun CircleInfo(
    @DrawableRes icon: Int,
    @StringRes label: Int,
    @StringRes iconDescription: Int
) {
    Surface(
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.clip(CircleShape),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = stringResource(iconDescription),
                    modifier = Modifier.padding(16.dp).size(30.dp)
                )
            }
            Text(
                text = stringResource(label),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}


@Composable
private fun RestaurantDetailItem(
    label: String
) {
    Card (
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(text = "Nom", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp))
        Text(text = label, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp))
    }
}



//@Preview(showBackground = true)
//@Composable
//fun DetailPreview() {
//    RestaurantScreen(Element(
//        name = "La pause libanaise",
//        location = Gps(0.0, 0.0),
//        createdAt = System.currentTimeMillis(),
//        id = 1,
//        type = ElementType.BAR,
//        lastEditedAt = System.currentTimeMillis(),
//        cuisines = listOf(Cuisine(0, "Thaï"), Cuisine(1, "Vietnamese"))
//    ), { }, { })
//}