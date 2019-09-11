package com.corebuild.arlocation.demo.model

import com.google.gson.annotations.SerializedName

data class Venue(
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("lat")
    val lat: String,
    @SerializedName("long")
    val long: String,
    @SerializedName("icon")
    val iconURL: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("address1")
    val address1: String,
    @SerializedName("address2")
    val address2: String,
    @SerializedName("distance")
    val distance: String
)