package com.example.tbsewaku.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tbsewaku.R
import com.example.tbsewaku.data.api.RetrofitClient
import com.example.tbsewaku.data.preferences.SharedPrefsManager
import com.example.tbsewaku.data.repository.AuthRepository

@Composable
fun UserProfileScreen( navController: NavHostController = rememberNavController()){
    Scaffold (
        topBar = {
            TopBarUserProfileScreen()
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)){
                LazyColumn { item {
                    ContentUserProfileScreen(navController)
                } }
            }
        },
        bottomBar = {
            BottomBarUserProfileScreen(navController)
        }

    )
}

@Composable
fun BottomBarUserProfileScreen(navController: NavHostController) {
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
                    painter = painterResource(R.drawable.orgh),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarUserProfileScreen(){
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
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = "Profile",
                    color = Color.White,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}
@Composable
fun ContentUserProfileScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    val sharedPrefsManager = SharedPrefsManager(context)

    LaunchedEffect(key1 = true) {
        val token = sharedPrefsManager.getToken()
        if (token != null) {
            val userData = AuthRepository(RetrofitClient.apiService, sharedPrefsManager).getUserSelf(token)
            userData?.let { data ->
                username = (data["username"] as? String) ?: ""
                email = (data["email"] as? String) ?: ""
                alamat = (data["address"] as? String) ?: ""
            }
        }
        isLoading = false
    }
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF2A9797),
                modifier = Modifier.size(50.dp)
            )
        }
    } else {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {


        // Profile Picture
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF007A7A)), // Lingkaran hijau
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(80.dp)
                )


            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form Fields
        CustomTextField(value = username, onValueChange = { username = it }, label = "Username")
        CustomTextField(value = email, onValueChange = { email = it }, label = "E-mail")
       CustomTextField(
            value = alamat,
            onValueChange = { alamat = it },
            label = "Alamat",

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Log Out Button
        Button(
            onClick = { navController.navigate("edit_profil") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007A7A)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Edit", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Log Out Button
        Button(
             onClick = { 
        sharedPrefsManager.clear() // Clear all saved data
        navController.navigate("login") {
            popUpTo(0) { inclusive = true } // Clear back stack
        }
    },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFFF9800) ),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Keluar", color = Color.Black, fontSize = 16.sp)
        }
    }}
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        enabled = false,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = Color.Gray
            )
        },
        leadingIcon = leadingIcon, // Tambahkan ikon jika diperlukan
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp) // Padding lebih kecil agar lebih proporsional
            .background(Color.White, RoundedCornerShape(8.dp)) // Warna background putih dan sudut membulat
            .border(1.dp, Color(0xFF007779), RoundedCornerShape(8.dp)), // Tambahkan border LightGray
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            cursorColor = Color(0xFF007A7A),
            focusedIndicatorColor = Color.Transparent, // Hilangkan garis bawah
            unfocusedIndicatorColor = Color.Transparent // Hilangkan garis bawah
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserProfileScreenPreview() {
    UserProfileScreen()
}
