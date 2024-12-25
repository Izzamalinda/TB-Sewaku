package com.example.tbsewaku.screens


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale


@Composable
fun MapScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                MapSubScreen(navController)
            }
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSubScreen(navController: NavHostController) {
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var address by remember { mutableStateOf("Mencari lokasi...") }
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissionGranted = permissions.values.all { it }
        if (permissionGranted) {
            updateLocation(fusedLocationClient, context) { location ->
                currentLocation = location
                address = getAddressFromLocation(context, location)
            }
        }
    }

    LaunchedEffect(Unit) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }) {
            permissionGranted = true
            updateLocation(fusedLocationClient, context) { location ->
                currentLocation = location
                address = getAddressFromLocation(context, location)
            }
        } else {
            launcher.launch(permissions)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                MapView(context).apply {
                    onCreate(Bundle())
                    getMapAsync { googleMap ->
                        googleMap.uiSettings.apply {
                            isMyLocationButtonEnabled = true
                            isZoomControlsEnabled = true
                            isCompassEnabled = true
                        }

                        currentLocation?.let { location ->
                            googleMap.clear()
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(location)
                                    .title("Lokasi Anda")
                            )
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(location, 15f)
                            )
                        }
                    }
                }
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            // Google Maps View
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    MapView(context).apply {
                        onCreate(Bundle())
                        getMapAsync { googleMap ->
                            // Map configuration
                        }
                    }
                }
            )

            // Bottom Sheet
            ModalBottomSheet (
                onDismissRequest = { /* Handle dismiss */ },
                sheetState = bottomSheetState,
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Lokasi Anda",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
            
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color(0xFFFF5722)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = address,
                            fontSize = 16.sp
                        )
                    }

                    Button(
                        onClick = {
                            currentLocation?.let { location ->
                                 println("DEBUG: Selected address: $address") // Add this debug line
                                navController.previousBackStackEntry?.savedStateHandle?.set("address", address)
                                navController.previousBackStackEntry?.savedStateHandle?.set("address", address)
                                navController.previousBackStackEntry?.savedStateHandle?.set("latitude", location.latitude.toString())
                                navController.previousBackStackEntry?.savedStateHandle?.set("longitude", location.longitude.toString())
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007A7A))
                    ) {
                        Text("Pilih Lokasi Ini", color = Color.White)
                    }


                    Button(
                        onClick = {
                            if (permissionGranted) {
                                updateLocation(fusedLocationClient, context) { location ->
                                    currentLocation = location
                                    address = getAddressFromLocation(context, location)
                                }
                            } else {
                                launcher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF5722)
                        )
                    ) {
                        Text("Perbarui Lokasi")
                    }
                }
            }
        }    
    }
}



private fun updateLocation(
    fusedLocationClient: FusedLocationProviderClient,
    context: Context,
    onLocationUpdated: (LatLng) -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                onLocationUpdated(LatLng(it.latitude, it.longitude))
            }
        }
    }
}

private fun getAddressFromLocation(context: Context, latLng: LatLng): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        addresses?.firstOrNull()?.getAddressLine(0) ?: "Alamat tidak ditemukan"
    } catch (e: Exception) {
        "Gagal mendapatkan alamat"
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MapsPreview(){
    MapScreen()
}