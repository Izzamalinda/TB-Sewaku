package com.example.tbsewaku.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.tbsewaku.R
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.tbsewaku.data.api.RetrofitClient
import com.example.tbsewaku.data.preferences.SharedPrefsManager
import com.example.tbsewaku.data.repository.AuthRepository
import com.example.tbsewaku.utils.FileUtils
import kotlinx.coroutines.launch

@Composable
fun Rate(navController: NavHostController = rememberNavController(),){
      val context = LocalContext.current
    val sharedPrefsManager = SharedPrefsManager(context)
    val token = sharedPrefsManager.getToken() ?: ""
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)
    val orders = remember { mutableStateOf<List<Map<String, Any>>?>(null) }

    LaunchedEffect(Unit) {
        orders.value = authRepository.getOrders(token, 1)
    }
    Scaffold (
        topBar = {
            TopBarRate(navController)
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)){
              LazyColumn {
                items(orders.value?.size ?: 0) { index ->
                    orders.value?.get(index)?.let { order ->
                        ContentRate(
                            namaPeminjam = (order["user"] as? Map<*, *>)?.get("username") as? String ?: "",
                            namaBarang = (order["product"] as? Map<*, *>)?.get("name") as? String ?: "",
                            jumlahBarang = (order["quantity"] as? Number)?.toInt() ?: 0,
                            tanggalPeminjaman = FileUtils.formatDate(order["loan_date"] as? String ?: ""),
                            tanggalPengembalian = FileUtils.formatDate(order["return_date"] as? String ?: ""),
                            imageRes = "https://rp5bngfd-8081.asse.devtunnels.ms/uploads/${(order["product"] as? Map<*, *>)?.get("image") as? String ?: ""}",
                            orderId=(order["id"] as? Number)?.toInt() ?: 0,
                            initialRate = (order["rate"] as? Number)?.toInt()

                            )
                    }
                }
            }
            }
        },

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarRate(navController: NavHostController = rememberNavController()){
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
                    onClick = {navController.navigate("riwayat")},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent))
                {
                    Image(
                        painter = painterResource(R.drawable.back),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                    Text(
                        text = "Rating",
                        color = Color.White,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )
}

@Composable
fun ContentRate(
    namaPeminjam: String,
    namaBarang: String,
    jumlahBarang: Int,
    tanggalPeminjaman: String,
    tanggalPengembalian: String,
    imageRes: String,
    orderId: Int,
    initialRate: Int? // Add initial rate parameter
){
    var rating by remember { mutableStateOf(initialRate ?: 0) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPrefsManager = SharedPrefsManager(context)
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)
     var showSuccessDialog by remember { mutableStateOf(false) }

if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Berhasil", fontWeight = FontWeight.Bold) },
            text = { Text("Rating berhasil dikirim!") },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A9797))
                ) {
                    Text("OK", color = Color.White)
                }
            },
            containerColor = Color.White,
            titleContentColor = Color(0xFF2A9797),
            textContentColor = Color.Black
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A9797)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = namaPeminjam,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                       AsyncImage(
                        contentDescription = namaBarang,
                        model =imageRes,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = namaBarang,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Jumlah: $jumlahBarang",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Tanggal Peminjaman: $tanggalPeminjaman",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Tanggal Pengembalian: $tanggalPengembalian",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Rating Stars
              Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                repeat(5) { index ->
                    IconButton(onClick = { rating = index + 1 }) {
                        Icon(
                            imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Star ${index + 1}",
                            tint = if (index < rating) Color.Yellow else Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        val token = sharedPrefsManager.getToken() ?: return@launch
                        val success = authRepository.updateOrderRate(token, orderId, rating)
                       if (success) {
                            showSuccessDialog = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
            ) {
                Text("Kirim", color = Color.White)
            }
        }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Beri Rating",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


@Composable
fun BottomBarRate(){
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
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            IconButton(onClick = {}){
                Image(
                    painter = painterResource(R.drawable.homeh),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = {}){
                Image(
                    painter = painterResource(R.drawable.notif),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 20.dp))
            IconButton(onClick = {}){
                Image(
                    painter = painterResource(R.drawable.riwayath),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp)
                )
            }
            IconButton(onClick = {}){
                Image(
                    painter = painterResource(R.drawable.profil),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RatePreview(){
    Rate()
}