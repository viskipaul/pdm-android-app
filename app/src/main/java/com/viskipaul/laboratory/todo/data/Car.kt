package com.viskipaul.laboratory.todo.data

data class Car(
    val id: String,
    var model: String,
    var year: String
){
    override fun toString(): String = model + ", an fabr.: " + year
}