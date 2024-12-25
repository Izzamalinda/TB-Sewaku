package com.example.tbsewaku.navigation

import androidx.compose.runtime.Composable  // Pastikan impor ini ada
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tbsewaku.screens.DetailMarketScreen
import com.example.tbsewaku.screens.EditProfile
import com.example.tbsewaku.screens.HomeScreen
import com.example.tbsewaku.screens.SplashScreen
import com.example.tbsewaku.screens.Login
import com.example.tbsewaku.screens.Homepage
import com.example.tbsewaku.screens.MapScreen
import com.example.tbsewaku.screens.NotificationScreen
import com.example.tbsewaku.screens.Proses
import com.example.tbsewaku.screens.Rate
import com.example.tbsewaku.screens.RatePreview
import com.example.tbsewaku.screens.RegisterScreen
import com.example.tbsewaku.screens.Riwayat
import com.example.tbsewaku.screens.Selesai
import com.example.tbsewaku.screens.SelesaiPreview
import com.example.tbsewaku.screens.TambahBarangScreen
import com.example.tbsewaku.screens.UserProfileScreen

// Menambahkan anotasi @Composable pada fungsi NavGraph
@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "splash", modifier = modifier) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { Login(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { Homepage(navController) }
        composable("notif") { NotificationScreen(navController) }
        composable("profil") { UserProfileScreen(navController) }
        composable("riwayat") { Riwayat(navController) }
        composable("edit_profil") { EditProfile(navController) }
        composable("market") { HomeScreen(navController) }
        composable("detail_market") { DetailMarketScreen(navController) }
        composable("create_product") { TambahBarangScreen(navController) }
        composable("done") { Selesai(navController) }
        composable("rate") {  Rate(navController) }
        composable("proses") {  Proses(navController) }
        composable("maps") { MapScreen(navController) }
    }
}
