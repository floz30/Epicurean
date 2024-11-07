package fr.floz.epicurean.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import fr.floz.epicurean.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <E> CuisineSearchDialog(
    items: List<E>,
    searchFieldContent: String,
    onSearch: (String) -> Unit,
    onItemClick: (E) -> Unit,
    onBack: () -> Unit
) {
    Dialog(
        onDismissRequest = onBack,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchFieldContent,
                            onSearch = {},
                            onQueryChange = onSearch,
                            expanded = true,
                            enabled = true,
                            onExpandedChange = {},
                            placeholder = {
                                Text(stringResource(R.string.form_search_placeholder))
                            },
                            leadingIcon = {
                                IconButton(
                                    onClick = onBack
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Default.ArrowBack,
                                        stringResource(R.string.app_navigation_back)
                                    )
                                }
                            },
                            trailingIcon = {
                                if (searchFieldContent.isNotEmpty()) {
                                    IconButton(
                                        onClick = { onSearch("") }
                                    ) {
                                        Icon(
                                            Icons.Default.Clear,
                                            stringResource(R.string.form_search_clear)
                                        )
                                    }
                                }

                            }
                        )
                    },
                    expanded = true,
                    onExpandedChange = {}
                ) {
                    if (searchFieldContent.isNotEmpty() && items.isNotEmpty()) {
                        LazyColumn(
                            Modifier.fillMaxSize().weight(1f),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(items) { item ->
                                ListItem(
                                    headlineContent = {
                                        Text(text = item.toString())
                                    },
                                    modifier = Modifier.clickable {
                                        onItemClick(item)
                                        onSearch("") // reset field
                                        onBack()
                                    },
                                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                                )
                                HorizontalDivider()
                            }
                        }
                    } else if (items.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                stringResource(R.string.form_search_noitems),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CuisineSearchDialogPreview() {
    CuisineSearchDialog(
        items = listOf<String>("Burger", "Italian", "French", "Regional"),
        searchFieldContent = "",
        onSearch = {_ -> },
        onItemClick = {_ -> },
        onBack = { }
    )
}