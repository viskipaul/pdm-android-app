package com.viskipaul.laboratory.todo.items

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.viskipaul.laboratory.R
import com.viskipaul.laboratory.core.TAG
import com.viskipaul.laboratory.todo.data.Car
import com.viskipaul.laboratory.todo.item.CarEditFragment
import kotlinx.android.synthetic.main.view_car.view.*

class CarListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<CarListAdapter.ViewHolder>() {

    var cars = emptyList<Car>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onCarClick: View.OnClickListener;

    init {
        onCarClick = View.OnClickListener { view ->
            val car = view.tag as Car
            fragment.findNavController().navigate(R.id.CarEditFragment, Bundle().apply {
                putString(CarEditFragment.CAR_ID, car.id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_car, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val car = cars[position]
        holder.itemView.tag = car
        holder.textView.text = car.model
        holder.itemView.setOnClickListener(onCarClick)
    }

    override fun getItemCount() = cars.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.text
    }
}
