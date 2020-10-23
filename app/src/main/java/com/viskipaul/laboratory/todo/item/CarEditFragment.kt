package com.viskipaul.laboratory.todo.item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.viskipaul.laboratory.R
import com.viskipaul.laboratory.core.TAG
import kotlinx.android.synthetic.main.fragment_car_edit.*

class CarEditFragment : Fragment() {
    companion object {
        const val CAR_ID = "CAR_ID"
    }

    private lateinit var viewModel: CarEditViewModel
    private var carId: String? = null
    private var carModel: String? = null
    private var carYear: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(CAR_ID)) {
                carId = it.getString(CAR_ID).toString()
                carModel = "test"
                carYear = "test2"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_car_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v(TAG, "onViewCreated")
        editTextModel.setText(carModel)
        editTextYear.setText(carYear)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save item")
            viewModel.saveOrUpdateItem(editTextModel.text.toString(), editTextYear.text.toString())
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(CarEditViewModel::class.java)
        viewModel.car.observe(viewLifecycleOwner, { car ->
            Log.v(TAG, "update items")
            editTextModel.setText(car.model)
            editTextYear.setText(car.year)
        })
        viewModel.fetching.observe(viewLifecycleOwner, { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModel.fetchingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().navigateUp()
            }
        })
        val id = carId
        if (id != null) {
            viewModel.loadItem(id)
        }
    }
}
