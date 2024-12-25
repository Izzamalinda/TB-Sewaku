package com.example.tbsewaku.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.tbsewaku.R
import com.example.tbsewaku.data.api.RetrofitClient
import com.example.tbsewaku.data.preferences.SharedPrefsManager
import com.example.tbsewaku.data.repository.AuthRepository
import com.example.tbsewaku.utils.FileUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahBarangScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(1) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPrefsManager = SharedPrefsManager(context)
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
) { success ->
    if (success && tempImageUri != null) {
        selectedImageUri = tempImageUri
    }
}

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val uri = FileUtils.createImageUri(context)
            tempImageUri = uri
            cameraLauncher.launch(uri)
        }
    }


    Scaffold(
        topBar = { TopBarTambahBarang(navController) },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Image Upload Section
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .border(1.dp, Color(0xFF2A9797), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(selectedImageUri),
                                    contentDescription = "Selected Image",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
                                    onClick = { galleryLauncher.launch("image/*") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A9797))
                                ) {
                                    Text("Gallery", color = Color.White)
                                }


                                Button(
                                    onClick = {
                                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A9797))
                                ) {
                                    Text("Camera", color = Color.White)
                                }
                            }
                        }
                        // Form Fields with Custom Style
                        CustomTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Nama Barang"
                        )

                        CustomTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = "Deskripsi"
                        )

                        CustomTextField(
                            value = type,
                            onValueChange = { type = it },
                            label = "kategori"
                        )

                     

                        CustomTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = "Harga"
                        )

                        CustomTextField(
                            value = stock,
                            onValueChange = { stock = it },
                            label = "Stok"
                        )

                        // Submit Button
                        Button(
                            onClick = {
                                scope.launch {
                                    selectedImageUri?.let { uri ->
                                        val imageFile = FileUtils.getImageFile(uri, context)
                                        imageFile?.let {
                                            val token = sharedPrefsManager.getToken() ?: return@launch
                                            val success = authRepository.createProduct(
                                                token = token,
                                                name = name,
                                                description = description,
                                                type = type,
                                                price = price,
                                                stock = stock,
                                                imageFile = it
                                            )
                                            if (success) {
                                                navController.navigate("market")
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A9797)),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text("Simpan", fontSize = 16.sp, color = Color.White)
                        }
                    }
                }
            }
        },

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Color.Gray) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF007779), RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            cursorColor = Color(0xFF007A7A),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductTypeDropdown(
    selectedType: Int,
    onTypeSelected: (Int) -> Unit
) {


    var typeText by remember { mutableStateOf("") }

































    TextField(
        value = typeText,
        onValueChange = { 
            typeText = it
            // Convert text input to type ID
            when (it.lowercase()) {
                "gitar" -> onTypeSelected(1)
                "tas" -> onTypeSelected(2)
            }



        },
        label = { Text("Tipe Barang", color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF007779), RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            cursorColor = Color(0xFF007A7A),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarTambahBarang( navController: NavHostController = rememberNavController()) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF2A9797)
        ),
        title = {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = {navController.navigate("home")},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent))
                {
                    Image(
                        painter = painterResource(R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                    androidx.compose.material3.Text(
                        text = "Tambah Barang",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )
}

@Composable
fun BottomBarTambahBarang(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(5F),
        horizontalArrangement = Arrangement.Center
    ) {
        FloatingActionButton(
            onClick = { navController.navigate("market") },
            modifier = Modifier
                .size(80.dp)
                .absoluteOffset(y = (-30).dp),
            shape = RoundedCornerShape(100),
            containerColor = Color(0xFF2A9797)
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }
    BottomAppBar(
        containerColor = Color(0xFF2A9797),
        modifier = Modifier.zIndex(1F)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { navController.navigate("home") }) {
                Image(
                    painter = painterResource(R.drawable.homeh),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = { navController.navigate("notif") }) {
                Image(
                    painter = painterResource(R.drawable.notif),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 20.dp))
            IconButton(onClick = { navController.navigate("riwayat") }) {
                Image(
                    painter = painterResource(R.drawable.riwayat),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = { navController.navigate("profil") }) {
                Image(
                    painter = painterResource(R.drawable.orgh),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}
