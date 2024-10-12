package fr.floz.epicurian.ui.restaurant

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import org.osmdroid.util.GeoPoint

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RestaurantMapScreen() {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    //var geoPoint by remember { mutableStateOf(GeoPoint(48.858262,2.294526)) }

    val cameraState = rememberCameraState {
        geoPoint = GeoPoint(48.858262,2.294526)
        zoom = 15.0
    }

    val paris = rememberMarkerState(
        geoPoint = GeoPoint(48.858262,2.294526),
        rotation = 90f
    )


    OpenStreetMap(
        modifier = Modifier.fillMaxSize(),
        cameraState = cameraState
    ) {
        Marker(
            state = paris,
            title = "PARIS"
        ) {
            Text("Exemple")
        }
    }


//    AndroidView(
//        modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
//        factory = { context ->
//            // Création de la vue
//            MapView(context).apply {
//                setTileSource(TileSourceFactory.USGS_TOPO)
//                setMultiTouchControls(true)
//                setZoomLevel(8.0)
//            }
//        },
//        update = { view ->
//            val marker = CustomMarkerBuilder(view, geoPoint)
//            if (locationPermissionsState.allPermissionsGranted) {
//                view.overlays.add(marker)
//                view.invalidate()
//            }
//
//            view.overlays.add(marker)
//            view.invalidate()
//
//            view.controller.setCenter(geoPoint)
//        }
//    )
}
