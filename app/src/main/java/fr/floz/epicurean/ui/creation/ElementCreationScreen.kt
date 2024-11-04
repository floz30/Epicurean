package fr.floz.epicurean.ui.creation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.westnordost.osm_opening_hours.model.OpeningHours
import fr.floz.epicurean.R
import fr.floz.epicurean.domain.entities.Address
import fr.floz.epicurean.domain.entities.Element
import fr.floz.epicurean.domain.entities.ElementType
import fr.floz.epicurean.domain.entities.coordinates.Coordinates
import fr.floz.epicurean.getActivity
import fr.floz.epicurean.openAppSettings
import fr.floz.epicurean.ui.utils.CuisineSearchDialog
import fr.floz.epicurean.ui.utils.ListSwipeToDismiss
import fr.floz.epicurean.ui.utils.LocationPermissionTextProvider
import fr.floz.epicurean.ui.utils.PermissionDialog
import kotlinx.coroutines.launch
import ovh.plrapps.mapcompose.ui.MapUI
import ovh.plrapps.mapcompose.ui.state.MapState

@Composable
fun CreationHomeScreen(
    viewModel: ElementCreationViewModel,
    onEvent: (ElementCreationEvent) -> Unit,
    onBackPressed: () -> Unit,
    onConfirmPressed: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLocationSelected = viewModel.isLocationSelected
    val currentActivity = LocalContext.current.getActivity()
    // Permissions
    val dialogQueue = viewModel.visiblePermissionDialogQueue
    val locationPermissionResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        viewModel.onPermissionResult(Manifest.permission.ACCESS_COARSE_LOCATION, isGranted) { viewModel.resolveLocation() }
    }
    if (viewModel.isLocationPermissionAlreadyGranted()) {
        viewModel.resolveLocation()
    }
    dialogQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        LocationPermissionTextProvider()
                    }
                    else -> return@forEach
                },
                isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                    currentActivity!!,
                    permission
                ),
                onDismiss = viewModel::dismissDialog,
                onConfirm = {
                    viewModel.dismissDialog()
                },
                onGoToAppSettingsClick = { currentActivity.openAppSettings() }
            )
        }

    Scaffold(
        topBar = {
            if (!isLocationSelected) TopBar(title = "Emplacement", confirmLabel = "Skip", onBackPressed = onBackPressed, onConfirmPressed = onConfirmPressed)
            else TopBar(title = "Informations", confirmLabel = "Sauvegarder", onBackPressed = onBackPressed, onConfirmPressed = {
                onEvent(ElementCreationEvent.SaveElement)
                onConfirmPressed()
            })
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (!isLocationSelected) {
                FloatingActionButton(
                    onClick = {
                        locationPermissionResultLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    },
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_my_location_24),
                        contentDescription = "Me localiser"
                    )
                }
            }
        }
    ) { padding ->
        if (isLocationSelected) {
            FormContent(padding, onEvent, state, viewModel)
        } else {
            MapContent(padding, onEvent, viewModel.mapState, state) {
                viewModel.importData()
                onConfirmPressed()
            }
        }
    }
}

@Composable
fun MapContent(
    innerPadding: PaddingValues,
    onEvent: (ElementCreationEvent) -> Unit,
    mapState: MapState,
    state: ElementCreationUiState,
    onImportClick: () -> Unit
) {
    Box(
        modifier = Modifier.padding(innerPadding).fillMaxSize()
    ) {
        MapUI(
            state = mapState
        )
    }

    if (state.showOsmDialog) {
        OsmDialog(onEvent, state, onImportClick)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun OsmDialog(
    onEvent: (ElementCreationEvent) -> Unit,
    state: ElementCreationUiState,
    onImportClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.hide() }.invokeOnCompletion { onEvent(ElementCreationEvent.ShowOsmDialog) } },
        sheetState = sheetState
    ) {
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Importation de données",
                    style = MaterialTheme.typography.headlineSmall
                )
                FilledIconButton(
                    onClick = { scope.launch { sheetState.hide() }.invokeOnCompletion { onEvent(ElementCreationEvent.ShowOsmDialog) } },
                    modifier = Modifier.size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Icon(Icons.Default.Close, "Fermer la boîte de dialogue", Modifier.size(18.dp))
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            LazyColumn(Modifier.selectableGroup()) {
                items(state.osmElements) {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        supportingContent = {
                            Text(
                                text = stringResource(it.type.label),
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        trailingContent = {
                            RadioButton(
                                selected = (state.selectedElement?.id == it.id),
                                onClick = null
                            )
                        },
                        leadingContent = {
                            Icon(painterResource(it.type.icon), "Icone représentant le type de l'élément")
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp)
                            .selectable(
                                selected = (state.selectedElement?.id == it.id),
                                onClick = { onEvent(ElementCreationEvent.SelectOsmElement(it)) },
                                role = Role.RadioButton
                            )
                    )
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Button(
                onClick = { onImportClick() },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                enabled = ((state.selectedElement?.id ?: 0L) != 0L)
            ) {
                Text("Importer")
            }
        }
    }
}


@Composable
fun FormContent(
    innerPadding: PaddingValues,
    onEvent: (ElementCreationEvent) -> Unit,
    state: ElementCreationUiState,
    viewModel: ElementCreationViewModel
) {
    val optionsType = ElementType.entries.toList()
    val scrollState = rememberScrollState()
    val (checkedState, onStateChange) = remember { mutableStateOf(false) }

    val searchText by viewModel.searchCuisineText.collectAsState()
    val cuisines by viewModel.cuisines.collectAsState()
    if (state.showSearchDialog) {
        CuisineSearchDialog(
            items = cuisines,
            searchFieldContent = searchText,
            onSearch = { text -> viewModel.tempFun2(text) },
            onItemClick = { item -> onEvent(ElementCreationEvent.AddCuisine(item)) },
            onBack = { onEvent(ElementCreationEvent.ShowCuisineDialog) }
        )
    }

    Surface(
        modifier = Modifier.padding(innerPadding)
    ) {
        Column(
            modifier = Modifier.verticalScroll(scrollState).padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {
            BasicInfo(state, onEvent, optionsType)
            HorizontalDivider(Modifier.padding(vertical = 15.dp))
            ContactInfo(state, onEvent, checkedState, onStateChange)
            HorizontalDivider(Modifier.padding(vertical = 15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Type de cuisine servie",
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(
                    onClick = { onEvent(ElementCreationEvent.ShowCuisineDialog) }
                ) {
                    Icon(Icons.Default.Add, "Ajouter un type de cuisine")
                }
            }

            Spacer(Modifier.height(10.dp))
            ListSwipeToDismiss(
                items = state.cuisine,
                height = (state.cuisine.size * 56).dp,
                onDelete = { item -> onEvent(ElementCreationEvent.RemoveCuisine(item)) }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BasicInfo(
    state: ElementCreationUiState,
    onEvent: (ElementCreationEvent) -> Unit,
    optionsType: List<ElementType>
) {
    var elementTypeDropdownExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(optionsType[0]) }
    OutlinedTextField(
        value = state.name,
        onValueChange = { onEvent(ElementCreationEvent.SetName(it)) },
        label = { Text("Nom") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(10.dp))

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = elementTypeDropdownExpanded,
        onExpandedChange = { elementTypeDropdownExpanded = !elementTypeDropdownExpanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            label = { Text("Type") },
            value = stringResource(selectedOption.label),
            leadingIcon = { Icon(painterResource(selectedOption.icon), contentDescription = "") },
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(elementTypeDropdownExpanded)
            },
            //colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = elementTypeDropdownExpanded,
            onDismissRequest = { elementTypeDropdownExpanded = false }
        ) {
            optionsType.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(option.label)) },
                    leadingIcon = { Icon(painterResource(option.icon), contentDescription = "") },
                    onClick = {
                        selectedOption = option
                        elementTypeDropdownExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ContactInfo(
    state: ElementCreationUiState,
    onEvent: (ElementCreationEvent) -> Unit,
    checkedState: Boolean,
    onStateChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = state.location.longitude.toString(),
            onValueChange = { },
            readOnly = true,
            label = { Text("Longitude") }
        )
        Spacer(Modifier.width(10.dp))
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = state.location.latitude.toString(),
            onValueChange = { },
            label = { Text("Latitude") }
        )
    }

    Spacer(Modifier.height(10.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            label = { Text("N°") },
            value = state.houseNumber,
            onValueChange = { onEvent(ElementCreationEvent.SetHouseNumber(it)) },
            modifier = Modifier.weight(0.2f)
        )
        Spacer(Modifier.width(10.dp))
        OutlinedTextField(
            label = { Text("Nom de rue") },
            value = state.streetName,
            onValueChange = { onEvent(ElementCreationEvent.SetStreetName(it)) },
            modifier = Modifier.weight(0.8f)
        )
    }

    Spacer(Modifier.height(10.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            label = { Text("Code") },
            value = state.postCode,
            onValueChange = { onEvent(ElementCreationEvent.SetPostCode(it)) },
            modifier = Modifier.weight(0.4f)
        )
        Spacer(Modifier.width(10.dp))
        OutlinedTextField(
            label = { Text("Ville") },
            value = state.city,
            onValueChange = { onEvent(ElementCreationEvent.SetCity(it)) },
            modifier = Modifier.weight(0.6f)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = checkedState,
                onValueChange = { onStateChange(!checkedState) },
                role = Role.Checkbox
            )
            .padding(top = 10.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Accessible en chaine roulante ?",
            style = MaterialTheme.typography.bodyLarge
        )
        Checkbox(
            checked = checkedState,
            onCheckedChange = null,
            modifier = Modifier.padding(end = 16.dp)
        )
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.phone,
        onValueChange = { onEvent(ElementCreationEvent.SetPhone(it)) },
        label = { Text("Télephone") },
        prefix = { Text("+33") },
        leadingIcon = { Icon(Icons.Default.Phone, "") }
    )

    Spacer(Modifier.height(10.dp))

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.website,
        onValueChange = { onEvent(ElementCreationEvent.SetWebsite(it)) },
        label = { Text("Site internet") },
        leadingIcon = { Icon(Icons.AutoMirrored.Default.ExitToApp, "") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    title: String,
    confirmLabel: String,
    onBackPressed: () -> Unit,
    onConfirmPressed: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.app_navigation_back)
                )
            }
        },
        actions = {
            TextButton(
                onClick = onConfirmPressed
            ) {
                Text(text = confirmLabel)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Alouette() {

    val data = listOf(
        Element(1, "La pause libanaise", ElementType.BAR, Coordinates.defaultGps, OpeningHours(), "", "", true, Address(), System.currentTimeMillis(), emptyList()),
        Element(2, "Mc Donalds", ElementType.FAST_FOOD, Coordinates.defaultGps, OpeningHours(), "", "", true, Address(), System.currentTimeMillis(), emptyList()),
        Element(3, "Allouette", ElementType.ICE_CREAM, Coordinates.defaultGps, OpeningHours(), "", "", true, Address(), System.currentTimeMillis(), emptyList()),
        Element(4, "Babilou", ElementType.UNKNOWN, Coordinates.defaultGps, OpeningHours(), "", "", true, Address(), System.currentTimeMillis(), emptyList()),
    )

    ModalBottomSheet(
        onDismissRequest = {  },
        sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
    ) {
        Column(Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Importation de données",
                    style = MaterialTheme.typography.headlineSmall
                )
                FilledIconButton(
                    onClick = { },
                    modifier = Modifier.size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Icon(Icons.Default.Close, "", Modifier.size(18.dp))
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            LazyColumn(Modifier.selectableGroup()) {
                items(data) { e ->
                    val name = e.name
                    val type = e.type

                    ListItem(
                        headlineContent = {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyLarge,
                                //modifier = Modifier.padding(start = 16.dp)
                            )
                        },
                        supportingContent = {
                            Text(
                                text = stringResource(type.label),
                                style = MaterialTheme.typography.bodySmall,
                                //modifier = Modifier.padding(start = 16.dp)
                            )
                        },
                        trailingContent = {
                            RadioButton(
                                selected = false,
                                onClick = null
                            )
                        },
                        leadingContent = {
                            Icon(painterResource(type.icon), "")
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp)
                            .selectable(
                                selected = false,
                                onClick = {  },
                                role = Role.RadioButton
                            )
                    )
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Button(
                onClick = {  },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                enabled = true
            ) {
                Text("Importer")
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun FormPreview() {
    //FormContent(PaddingValues(5.dp), { _ -> }, ElementCreationUiState())
}