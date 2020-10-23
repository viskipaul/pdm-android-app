package com.viskipaul.laboratory.todo.data.remote

import com.google.gson.GsonBuilder
import com.viskipaul.laboratory.todo.data.Car
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object CarsApi{
    private const val URL = "http://192.168.0.163:3000/"

    interface Service{
        @GET("/car")
        suspend fun find(): List<Car>

        @GET("/car/{id}")
        suspend fun read(@Path("id") carId: String): Car;

        @Headers("Content-Type: application/json")
        @POST("/car")
        suspend fun create(@Body car: Car): Car

        @Headers("Content-Type: application/json")
        @PUT("/car/{id}")
        suspend fun update(@Path("id") carId: String, @Body car: Car): Car
    }

    private var client: OkHttpClient = OkHttpClient.Builder().build();

    private var gson = GsonBuilder().setLenient().create()

    private var retrofit = Retrofit.Builder().
            baseUrl(URL).addConverterFactory(GsonConverterFactory.create(gson)).client(client).build()

    val service: Service = retrofit.create(Service::class.java)
}