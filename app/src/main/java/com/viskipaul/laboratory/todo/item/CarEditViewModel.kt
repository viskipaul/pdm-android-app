package com.viskipaul.laboratory.todo.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.viskipaul.laboratory.core.TAG
import com.viskipaul.laboratory.todo.data.Car
import com.viskipaul.laboratory.todo.data.CarRepository

class CarEditViewModel : ViewModel() {
    private val mutableItem = MutableLiveData<Car>().apply { value = Car("", "", "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val car: LiveData<Car> = mutableItem
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadItem(carId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadItem...")
            mutableFetching.value = true
            mutableException.value = null
            try {
                mutableItem.value = CarRepository.load(carId)
                Log.i(TAG, "loadItem succeeded")
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadItem failed", e)
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }

    fun saveOrUpdateItem(model: String, year: String) {
        viewModelScope.launch {
            Log.i(TAG, "saveOrUpdateItem...");
            val car = mutableItem.value ?: return@launch
            car.model = model
            car.year = year
            mutableFetching.value = true
            mutableException.value = null
            try {
                if (car.id.isNotEmpty()) {
                    mutableItem.value = CarRepository.update(car)
                } else {
                    mutableItem.value = CarRepository.save(car)
                }
                Log.i(TAG, "saveOrUpdateItem succeeded");
                mutableCompleted.value = true
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "saveOrUpdateItem failed", e);
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }
}
