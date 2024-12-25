package com.example.tbsewaku.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.tbsewaku.data.api.RetrofitClient
import com.example.tbsewaku.data.preferences.SharedPrefsManager
import com.example.tbsewaku.data.repository.AuthRepository


@Composable
fun Login(
    navController: NavHostController = rememberNavController(),
    authRepository: AuthRepository = AuthRepository(RetrofitClient.apiService, SharedPrefsManager(LocalContext.current))
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    // Add validation states
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Validation functions
    fun validateUsername(): Boolean {
        return when {
            username.isEmpty() -> {
                usernameError = "Username tidak boleh kosong"
                false
            }
            username.length < 2 -> {
                usernameError = "Username minimal 2 karakter"
                false
            }
            else -> {
                usernameError = null
                true
            }
        }
    }

    fun validatePassword(): Boolean {
        return when {
            password.isEmpty() -> {
                passwordError = "Password tidak boleh kosong"
                false
            }
            password.length < 6 -> {
                passwordError = "Password minimal 6 karakter"
                false
            }
            else -> {
                passwordError = null
                true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF2A9797)),
        contentAlignment = Alignment.Center,
        ){
       Column(
           modifier = Modifier.padding(horizontal = 40.dp)
       ) {
           Row (
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.Center
           ){
               Text(
                   text = "Login",
                   fontSize = 35.sp,
                   fontWeight = FontWeight.Bold,
                   color = Color.White
                   )
           }
           Spacer(modifier = Modifier.padding(vertical = 30.dp))
           Row (
               modifier = Modifier.fillMaxWidth()
           ){
               Column {
                   Text(
                       text = "Username",
                       fontWeight = FontWeight.SemiBold,
                       color = Color.White
                   )
                   Spacer(modifier = Modifier.padding(vertical = 5.dp))
                   TextField(
                       value = username,
                       onValueChange = {
                           username = it
                           validateUsername()
                       },
                       modifier = Modifier.fillMaxWidth(),
                       shape = RoundedCornerShape(50.dp),
                       isError = usernameError != null,
                       supportingText = {
                           if (usernameError != null) {
                               Text(
                                   text = usernameError!!,
                                   color = Color.Red
                               )
                           }
                       }
                   )
               }
           }
           Spacer(modifier = Modifier.padding(vertical = 10.dp))
           Row (
               modifier = Modifier.fillMaxWidth()
           ){
               Column {
                   Text(
                       text = "Password",
                       fontWeight = FontWeight.SemiBold,
                       color = Color.White
                   )
                   Spacer(modifier = Modifier.padding(vertical = 5.dp))
                   TextField(
                       value = password,
                       onValueChange = {
                           password = it
                           validatePassword()
                       },
                       modifier = Modifier.fillMaxWidth(),
                       shape = RoundedCornerShape(50.dp),
                       isError = passwordError != null,
                       supportingText = {
                           if (passwordError != null) {
                               Text(
                                   text = passwordError!!,
                                   color = Color.Red
                               )
                           }
                       }
                   )
               }
           }
           Spacer(modifier = Modifier.padding(vertical = 20.dp))
           Row {
               Button(
                   onClick = {
                       if (validateUsername() && validatePassword()) {
                           scope.launch {
                               isLoading = true
                               try {
                                   val success = authRepository.login(username, password)
                                   if (success) {
                                       errorMessage=""
                                       navController.navigate("home") {
                                           popUpTo("login") { inclusive = true }
                                       }
                                   } else {
                                       errorMessage = "Login gagal. Periksa kembali username dan password Anda."
                                   }
                               } catch (e: Exception) {
                                   errorMessage = e.message ?: "Terjadi kesalahan"
                               } finally {
                                   isLoading = false
                               }
                           }
                       }
                   },
                   modifier = Modifier.fillMaxWidth(),
                   colors = ButtonDefaults.buttonColors(
                       containerColor = Color(0xFF007779)
                   ),
                   enabled = !isLoading
               ) {
                   if (isLoading) {
                       CircularProgressIndicator(color = Color.White)
                   } else {
                       Text(
                           text = "Login",
                           fontSize = 20.sp,
                           fontWeight = FontWeight.Bold,
                           modifier = Modifier.padding(10.dp)
                       )
                   }
               }
           }
           errorMessage?.let { error ->
               Text(
                   text = error,
                   color = Color.Red,
                   modifier = Modifier.padding(top = 8.dp)
               )
           }
           Spacer(modifier = Modifier.padding(vertical = 10.dp))
           Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.Center,
               verticalAlignment = Alignment.CenterVertically
           ) {
               Text(
                   text = "Belum punya akun?",
                   color = Color.White
               )
               TextButton(onClick = {
                navController.navigate("register") 

               }) {
                   Text(
                       text = "Daftar",
                       color = Color.White,
                       fontWeight = FontWeight.Bold
                   )
               }
           }
       }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview(){
    Login()
}
