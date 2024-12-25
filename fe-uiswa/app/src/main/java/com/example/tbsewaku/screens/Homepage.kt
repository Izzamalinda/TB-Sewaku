package com.example.tbsewaku.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
fun Homepage(
    navController: NavHostController = rememberNavController()
){

    Scaffold (
        topBar = {
            TopBar(navController)
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)){
                LazyColumn { item {
                    ContentHome(navController)
                } }
            }
        },
        bottomBar = {
            BottomBar(navController)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController){
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.logo2),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                    )
                Text(
                    text = "Selamat Datang",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold
                    )
                Button(
                    onClick = {navController.navigate("create_product")},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Image(
                        painter = painterResource(R.drawable.add),
                        contentDescription = null,
                        modifier = Modifier.size(35.dp),
                    )
                }
            }
        }
    )
}

@Composable
fun ContentHome(navController: NavHostController){
    Card(
        onClick = {navController.navigate("market")},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .height(150.dp)
    ) {
        Box(){
            Image(painter = painterResource(R.drawable.kuliah),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "Kuliah",
                    modifier = Modifier.padding(10.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }
        }
    }
    Card(
        onClick = {navController.navigate("market")},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .height(150.dp)
    ) {
        Box() {
            Image(painter = painterResource(R.drawable.elektronik),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.5f)
            )
            Text(
                text = "Elektronik",
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
                )
        }
    }
    Card(
        onClick = {navController.navigate("market")},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .height(150.dp)
    ) {
       Box(){
           Image(painter = painterResource(R.drawable.event),
               contentDescription = null,
               modifier = Modifier.fillMaxSize()
           )
           Text(
               text = "Event",
               modifier = Modifier.padding(10.dp),
               color = Color.White,
               fontWeight = FontWeight.Bold,
               fontSize = 30.sp
           )
       }
    }
    Card(
        onClick = {navController.navigate("market")},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .height(150.dp)
    ) {
       Box(){
           Image(painter = painterResource(R.drawable.hiburan),
               contentDescription = null,
               modifier = Modifier.fillMaxSize()
           )
           Text(
               text = "Hiburan",
               modifier = Modifier.padding(10.dp),
               color = Color.White,
               fontWeight = FontWeight.Bold,
               fontSize = 30.sp
           )
       }
    }
    Card(
        onClick = {navController.navigate("market")},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .height(150.dp)
    ) {
       Box(){
           Image(painter = painterResource(R.drawable.fashion),
               contentDescription = null,
               modifier = Modifier.fillMaxSize()
           )
           Text(
               text = "Fashion",
               modifier = Modifier.padding(10.dp),
               color = Color.White,
               fontWeight = FontWeight.Bold,
               fontSize = 30.sp
           )
       }
    }
    Card(
        onClick = {navController.navigate("market")},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 10.dp)
            .height(150.dp)
    ) {
        Box(){
            Image(painter = painterResource(R.drawable.olahraga),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Olahraga",
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController){
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
                    painter = painterResource(R.drawable.home),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomepagePreview(){
    Homepage()
}