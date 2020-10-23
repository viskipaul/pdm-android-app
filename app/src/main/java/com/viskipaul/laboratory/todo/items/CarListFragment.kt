package com.viskipaul.laboratory.todo.items

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.viskipaul.laboratory.R
import com.viskipaul.laboratory.core.TAG
import kotlinx.android.synthetic.main.fragment_car_list.*

class CarListFragment : Fragment() {
    private lateinit var carListAdapter: CarListAdapter
    private lateinit var carsModel: CarListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupItemList()
        fab.setOnClickListener {
            Log.v(TAG, "add new item")
            findNavController().navigate(R.id.CarEditFragment)
        }
    }

    private fun setupItemList() {
        carListAdapter = CarListAdapter(this)
        car_list.adapter = carListAdapter
        carsModel = ViewModelProvider(this).get(CarListViewModel::class.java)
        carsModel.cars.observe(viewLifecycleOwner, { cars ->
            Log.v(TAG, "update items")
            carListAdapter.cars = cars
        })
        carsModel.loading.observe(viewLifecycleOwner, { loading ->
            Log.i(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        carsModel.loadingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.i(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        })
        carsModel.loadItems()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(TAG, "onDestroy")
    }
}