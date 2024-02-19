package com.example.wantplant.data.remote.garden

import com.example.wantplant.data.local.GardenData
import com.example.wantplant.data.local.GardenResponse
import com.example.wantplant.data.remote.garden.request.GardenPostRequest
import com.example.wantplant.data.remote.garden.request.GardenPutRequest
import com.example.wantplant.data.remote.garden.response.GardenDeleteResponse
import com.example.wantplant.data.remote.garden.response.GardenGetResponse
import com.example.wantplant.data.remote.garden.response.GardenPutList
import com.example.wantplant.data.remote.garden.response.GardenPutResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GardenRetrofitInterfaces {

    @GET("/api/gardens")
    fun getGarden(
        @Header("Authorization") authorization: String
    ): Call<GardenGetResponse>

    @PUT("/api/gardens")
    fun putGarden(@Header("Authorization") authorization: String, @Body gardenPutRequest: GardenPutRequest): Call<GardenPutResponse>

    @POST("/api/gardens") // 서버의 endpoint
    fun postData(
        @Header("Authorization") authorization: String,
        @Body gardenPostRequest: GardenPostRequest
    ): Call<GardenResponse>

    @GET("/api/gardens")
    fun getGardens(
        @Header("Authorization") authorization: String
    ): Call<GardenResponse>

    @DELETE("/api/gardens/{gardenId}")
    fun deleteGarden(@Header("Authorization") authorization: String, @Path("gardenId") gardenId: Long): Call<GardenDeleteResponse>

}
