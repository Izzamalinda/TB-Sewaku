package com.example.tbsewaku.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Endpoint untuk login
    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): Response<Map<String, Any>>

    @POST("auth/register")
    suspend fun register(@Body body: Map<String, String>): Response<Map<String, Any>>

    @GET("users/self")
    suspend fun getUserSelf(@Header("Authorization") token: String): Response<Map<String, Any>>

      @PUT("users")
    suspend fun updateUser(
        @Body body: Map<String, String>,  // Change from Map<String, Any?> to Map<String, String>
        @Header("Authorization") token: String
    ): Response<Map<String, Any>>

@Multipart
@POST("products")
suspend fun createProduct(
    @Part("name") name: RequestBody,
    @Part("description") description: RequestBody,
    @Part("type") type: RequestBody,
    @Part("price") price: RequestBody,
    @Part("stock") stock: RequestBody,
    @Part image: MultipartBody.Part,
    @Header("Authorization") token: String
): Response<Map<String, Any>>

@GET("orders")
suspend fun getOrders(
    @Header("Authorization") token: String,
    @Query("status") status: Int? = null
): Response<Map<String, Any>>

@GET("products")
suspend fun getProducts(
    @Query("name") name: String? = null,
    @Query("sort") sort: String? = null,
    @Header("Authorization") token: String
): Response<Map<String, Any>>

@PUT("orders/{id}")
suspend fun updateOrder(
    @Path("id") orderId: Int,
    @Body body: Map<String, Int>,
    @Header("Authorization") token: String
): Response<Map<String, Any>>


    // Endpoint untuk upload gambar
    @Multipart
    @POST("upload_image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?,
        @Header("Authorization") authorization: String
    ): Response<Map<String, Any>>
}
