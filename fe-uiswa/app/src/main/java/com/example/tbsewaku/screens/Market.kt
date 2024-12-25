package com.example.tbsewaku.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.tbsewaku.R
import com.example.tbsewaku.data.api.RetrofitClient
import com.example.tbsewaku.data.preferences.SharedPrefsManager
import com.example.tbsewaku.data.repository.AuthRepository
import kotlinx.coroutines.launch
import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.clickable
import androidx.navigation.NavController

// Tambahkan anotasi @OptIn di sini
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
      var searchQuery by remember { mutableStateOf("") }
        var tempSearchQuery by remember { mutableStateOf("") }
    var selectedSort by remember { mutableStateOf<String?>(null) }
    var products by remember { mutableStateOf<List<Map<String, Any>>?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPrefsManager = SharedPrefsManager(context)
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)

    // Load products
    LaunchedEffect(searchQuery, selectedSort) {
        val token = sharedPrefsManager.getToken()
        if (token != null) {
            products = authRepository.getProducts(token, searchQuery, selectedSort)
        }
    }
    Scaffold(
        bottomBar = {
            Box { HomeBottomNavigationBar(navController) } // Navigasi bawah dengan nama baru
        }
    ) { paddingValues ->
         Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            SearchBar(
                value = tempSearchQuery,
                onValueChange = { tempSearchQuery = it },
                onSearch = { searchQuery = tempSearchQuery }
            )
            
            FilterButtons(
                onFilterSelected = { sort ->
                    selectedSort = when(sort) {
                        "Terdekat" -> "nearest"
                        "Terfavorit" -> "most_ordered"
                        "Termurah" -> "price_asc"
                        else -> null
                    }
                }
            )

            ProductGrid(products = products ?: emptyList(), navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFFF0F0F0), RoundedCornerShape(24.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Search...") },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
      IconButton(onClick = { 
            onSearch()
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow((context as Activity).currentFocus?.windowToken, 0)
        }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF009688),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Composable
fun FilterButtons(onFilterSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilterButton("Terdekat") { onFilterSelected("Terdekat") }
        FilterButton("Terfavorit") { onFilterSelected("Terfavorit") }
        FilterButton("Termurah") { onFilterSelected("Termurah") }
    }
}

@Composable
fun FilterButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.border(1.dp, Color(0xFF009688), RoundedCornerShape(24.dp))
    ) {
        Text(text = label, color = Color(0xFF009688))
    }
}

@Composable
fun ProductGrid(products: List<Map<String, Any>>, navController: NavController = rememberNavController()) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(16.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            val user = product["user"] as? Map<String, Any>
            Card(
                modifier = Modifier.clickable{
                    navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
                    navController.navigate("detail_market")
                }
            ) {
                ProductCard(
                    name = product["name"] as? String ?: "",
                    price = "Rp ${product["price"]}",
                    rentInfo = "${product["orderCount"]}+ disewa",
                    imageUrl = product["image"] as? String,
                    username = user?.get("username") as? String ?: "",
                    distance = product["distance"] as? Double
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    name: String,
    price: String,
    rentInfo: String,
    imageUrl: String?,
    username: String,
    distance: Double?
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            // Use Coil for image loading
            AsyncImage(
                model = "uploads/${imageUrl}",
                contentDescription = name,
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.logo)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(price, color = Color(0xFF009688))
            Text(rentInfo, fontSize = 12.sp, color = Color.Gray)
            Text("Pemilik: $username", fontSize = 12.sp)
            if (distance != null) {
                Text("${String.format("%.1f", distance)} km", fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun HomeBottomNavigationBar(navController: NavHostController) { // Nama fungsi diubah untuk menghindari konflik
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(5F),
        horizontalArrangement = Arrangement.Center
    ) {
        FloatingActionButton(
            onClick = {},
            modifier = Modifier
                .size(80.dp)
                .absoluteOffset(y = (-30).dp),
            shape = RoundedCornerShape(100),
            containerColor = Color(0xFF2A9797)
        ) {
            Image(
                painter = painterResource(R.drawable.logo4),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }
    BottomAppBar(
        containerColor = Color(0xFF2A9797),
        modifier = Modifier.zIndex(1F)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            IconButton(onClick = {navController.navigate("home") }){
                Image(
                    painter = painterResource(R.drawable.homeh),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = {navController.navigate("notif")}){
                Image(
                    painter = painterResource(R.drawable.notif),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 20.dp))
            IconButton(onClick = {navController.navigate("riwayat")}){
                Image(
                    painter = painterResource(R.drawable.riwayat),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = {navController.navigate("profil")}){
                Image(
                    painter = painterResource(R.drawable.profil),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

data class Product(val name: String, val price: String, val rentInfo: String, val imageRes: Int)

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
