package com.viskipaul.laboratory.todo.data

import android.util.Log
import com.viskipaul.laboratory.core.TAG
import com.viskipaul.laboratory.todo.data.remote.CarsApi

object CarRepository{
    private var cachedItems: MutableList<Car>? = null;

    suspend fun loadAll() : List<Car> {
        Log.i(TAG, "loadAll")
        if (cachedItems != null){
            return cachedItems as List<Car>
        }
        cachedItems = mutableListOf()
        val cars = CarsApi.service.find()
        cachedItems?.addAll(cars)
        return cachedItems as List<Car>
    }

    suspend fun load(carId: String): Car{
        Log.i(TAG, "load")
        val car = cachedItems?.find{ it.id == carId }
        if(car != null){
            return car
        }
        return CarsApi.service.read(carId)
    }

    suspend fun save(car: Car): Car{
        Log.i(TAG, "save")
        val createdCar = CarsApi.service.create(car)
        cachedItems?.add(createdCar)
        return createdCar
    }

    suspend fun update(car: Car): Car{
        Log.i(TAG, "update")
        val updatedCar = CarsApi.service.update(car.id, car)
        val index = cachedItems?.indexOfFirst { it.id == car.id }
        if(index != null){
            cachedItems?.set(index, updatedCar)
        }
        return updatedCar
    }

}