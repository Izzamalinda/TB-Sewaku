package com.example.tbsewaku.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.tbsewaku.R


@Composable
fun DetailMarketScreen(
    navController: NavHostController = rememberNavController()
) {
    val product = navController.previousBackStackEntry?.savedStateHandle?.get<Map<String, Any>>("product")
    val user = product?.get("user") as? Map<String, Any>

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
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
                            onClick = {navController.navigate("market")},
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent))
                        {
                            Image(
                                painter = painterResource(R.drawable.back),
                                contentDescription = null,
                                modifier = Modifier.size(45.dp)
                            )
                            androidx.compose.material3.Text(
                                text = "",
                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                 AsyncImage(
                    model = "https://rp5bngfd-8081.asse.devtunnels.ms/uploads/${product?.get("image")}",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Product Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                Text(
                    text = "Rp ${product?.get("price")}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product?.get("name") as? String ?: "",
                    fontSize = 18.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Owner Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_profile),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = user?.get("username") as? String ?: "",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Lokasi: ${user?.get("address") as? String ?: ""}",
                            color = Color.Gray
                        )
                    }
                }


                }
                    }




            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2A9797))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    ) {
                        Text(
                            text="Description",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = product?.get("description") as? String ?: "",
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            color = Color.White
                        )
                    }}}
            // Bottom Buttons

        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailMarketPreview() {
    DetailMarketScreen()
}
