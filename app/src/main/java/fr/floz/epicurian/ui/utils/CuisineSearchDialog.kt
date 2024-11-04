package fr.floz.epicurian.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import fr.floz.epicurian.R


@Composable
fun <E> CuisineSearchDialog(
    items: List<E>,
    searchFieldContent: String,
    onSearch: (String) -> Unit,
    onItemClick: (E) -> Unit,
    onBack: () -> Unit
) {

    Dialog(
        onDismissRequest = {  },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {

        Surface(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Row {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            stringResource(R.string.app_navigation_back)
                        )
                    }
                    TextField(
                        value = searchFieldContent,
                        onValueChange = { onSearch(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search...") },
                        singleLine = true
                    )
                    HorizontalDivider()
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    Modifier.fillMaxSize().weight(1f)
                ) {
                    items(items) { item ->
                        Text(
                            text = item.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                                .clickable {
                                    onItemClick(item)
                                }
                        )
                    }
                }
            }


        }
    }
}


@Preview(showBackground = true)
@Composable
fun CuisineDialogPreview() {
//    CuisineSearchDialog(
//        listOf("Tomate", "Pomme", "Poire", "Riz"),
//        "",
//        { _ -> }
//    )
}