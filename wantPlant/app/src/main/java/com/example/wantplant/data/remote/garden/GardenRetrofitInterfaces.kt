package com.example.wantplant.data.remote.garden

import com.example.wantplant.data.local.GardenData
import com.example.wantplant.data.local.GardenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GardenRetrofitInterfaces {
    @POST("/api/gardens") // 서버의 endpoint
    fun postData(
        @Header("Authorization") authorization: String,
        @Body gardenData: GardenData
    ): Call<GardenResponse>

    @GET("/api/gardens")
    fun getGardens(
        @Header("Authorization") authorization: String
    ): Call<GardenResponse>

    @DELETE("/api/gardens/{gardenId}")
    fun deleteGarden(@Header("Authorization") authorization: String, @Path("gardenId") gardenId: Int): Call<Void>

}
