package com.example.tbsewaku.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tbsewaku.R

@Composable
fun Riwayat(navController: NavHostController = rememberNavController()){
    Scaffold (
        topBar = {
            TopBarRiwayat()
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)){
                LazyColumn { item {
                    PesananSection(navController)
                } }
            }
        },
        bottomBar = {
            BottomBarRiwayat(navController)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarRiwayat(){
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
                    painter = painterResource(R.drawable.logo3),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = "Riwayat",
                    color = Color.White,
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
fun PesananSection(navController: NavHostController = rememberNavController()){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .background(color = Color(0xFF7FC0BF)),
    ){
        Column{
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text(
                    text = "Pesanan Saya",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Lihat Riwayat Pesanan >",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ){
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent))
                {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            navController.navigate("proses")
                        }
                    ){
                        Image(
                            painter = painterResource(R.drawable.proses),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp),
                        )
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Text(
                            text = "Proses",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 30.dp))
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                            navController.navigate("done")
                        }
                    ){
                        Image(
                            painter = painterResource(R.drawable.finish),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Text(
                            text = "Finish",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 30.dp))
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                            navController.navigate("rate")}
                    ){
                        Image(
                            painter = painterResource(R.drawable.rating),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Text(
                            text = "Rating",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarRiwayat(navController: NavHostController){
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
                    painter = painterResource(R.drawable.riwayath),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RiwayatPreview(){
    Riwayat()
}