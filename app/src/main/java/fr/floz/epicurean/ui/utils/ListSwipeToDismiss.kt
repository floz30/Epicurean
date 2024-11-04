package fr.floz.epicurean.ui.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <E> ListSwipeToDismiss(
    items: List<E>,
    onDelete: (E) -> Unit,
    height: Dp = (items.size * 56).dp
) {
    LazyColumn(
        modifier = Modifier.height(height)
    ) {
        items(items) { item ->
            ListSwipeToDismissItem(item.toString()) { onDelete(item) }
        }
    }
}

@Composable
private fun <E> ListSwipeToDismissItem(
    value: E,
    onDelete: (E) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> return@rememberSwipeToDismissBoxState false
                SwipeToDismissBoxValue.EndToStart -> { onDelete(value) }
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * 0.25f }
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            DismissBackground(dismissState)
        }
    ) {
        OutlinedCard(shape = RectangleShape) {
            ListItem(
                headlineContent = { Text(value.toString()) }
            )
        }
    }
}

@Composable
private fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            SwipeToDismissBoxValue.Settled -> Color.LightGray
            SwipeToDismissBoxValue.StartToEnd -> Color.Green
            SwipeToDismissBoxValue.EndToStart -> Color.Red
        }
    )
    Row(
        modifier = Modifier.fillMaxSize().background(color).padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Icon(
            Icons.Default.Delete,
            "Supprimer la ligne"
        )
    }
}