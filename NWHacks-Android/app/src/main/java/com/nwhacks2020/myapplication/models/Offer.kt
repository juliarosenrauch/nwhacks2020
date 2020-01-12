package com.nwhacks2020.myapplication.models

data class Offer(
    val text: String,
    val type: String,
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        const val foodType = "Food"
        const val shelterType = "Shelter"
        const val suppliesType = "Supplies"
        val allTypes = arrayListOf(foodType, shelterType, suppliesType)
    }
}