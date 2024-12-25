package com.example.tbsewaku.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NotificationScreen(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val sharedPrefsManager = SharedPrefsManager(context)
    val token = sharedPrefsManager.getToken() ?: ""
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)
    var orders = remember { mutableStateOf<List<Map<String, Any>>?>(null) }
    var refreshTrigger = remember { mutableStateOf(0) }

    LaunchedEffect(refreshTrigger.value) {
        orders.value = authRepository.getOrders(token)
    }

    Scaffold(
        bottomBar = {
            Box { NotificationBottomNavigationBar(navController) }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFEFFBF8)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarNotification()
            Spacer(modifier = Modifier.height(16.dp))
            orders.value?.forEach { order ->
                NotificationCard(
                    order = order,
                    refreshTrigger = refreshTrigger.value
                ) {
                    refreshTrigger.value = refreshTrigger.value + 1
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TopBarNotification() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A9797))
            .padding(vertical = 30.dp, horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.contoh), // Ganti dengan logo yang sesuai
                contentDescription = "Logo",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row (modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Notifikasi",
                    fontSize = 30.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun NotificationCard(order: Map<String, Any>, refreshTrigger: Int,
    onRefresh: () -> Unit) {
    val user = order["user"] as? Map<String, Any>
    val product = order["product"] as? Map<String, Any>
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedPrefsManager = SharedPrefsManager(context)
    val authRepository = AuthRepository(RetrofitClient.apiService, sharedPrefsManager)
    
    val status = (order["status"] as? Double)?.toInt() ?: 0
    val orderId = (order["id"] as? Double)?.toInt() ?: 0
      val formatDate = { dateString: String ->
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id"))
        val date = inputFormat.parse(dateString)
        outputFormat.format(date)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A9797))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://rp5bngfd-8081.asse.devtunnels.ms/uploads/${product?.get("image")}",
                contentDescription = product?.get("name") as? String,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 16.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 15.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                    ) {
                        Text(
                            text = user?.get("username") as? String ?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2A9797),
                        )
                    }
                }
                
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                
                Text(
                    text = product?.get("name") as? String ?: "",
                    fontSize = 17.sp,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                
                  Text(
        text = "Jumlah: ${order["quantity"]}\n" +
               "Peminjaman: ${formatDate(order["loan_date"] as String)}\n" +
               "Pengembalian: ${formatDate(order["return_date"] as String)}",
        fontSize = 14.sp,
        color = Color.White
    )
     Spacer(modifier = Modifier.padding(vertical = 2.dp))
                
                        Text(
                text = when(status) {
                    0 -> "Menunggu"
                    1 -> "Disetujui"
                    2 -> "Ditolak"
                    3 -> "Selesai"
                    else -> ""
                },
                color = when(status) {
                    0 -> Color.DarkGray
                    1 -> Color.DarkGray
                    2 -> Color.DarkGray
                    3 -> Color.DarkGray
                    else -> Color.DarkGray
                }
            )

            }
        }

        if (status == 0) {
            Row(modifier = Modifier.padding(10.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            val token = sharedPrefsManager.getToken() ?: return@launch
                           val success = authRepository.updateOrderStatus(token, orderId, 2)
                            if (success) {
                                 onRefresh()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Text("Ditolak", color = Color.White)
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = {
                        scope.launch {
                            val token = sharedPrefsManager.getToken() ?: return@launch
                            val success = authRepository.updateOrderStatus(token, orderId, 1)
                            if (success) {
                                 onRefresh()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Text("Disetujui", color = Color.White)
                }
            }
        }
    }
}


// Ganti nama fungsi untuk menghindari konflik
@Composable
private fun NotificationBottomNavigationBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(5F),
        horizontalArrangement = Arrangement.Center
    ) {
        FloatingActionButton(
            onClick = {navController.navigate("market")},
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
            IconButton(onClick = {navController.navigate("home") }){
                Image(
                    painter = painterResource(R.drawable.homeh),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            IconButton(onClick = {navController.navigate("notif")}){
                Image(
                    painter = painterResource(R.drawable.notifh),
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

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NotificationScreen()
}