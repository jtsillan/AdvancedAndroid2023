package com.example.em_tuntiesimerkki1

import com.google.gson.annotations.SerializedName


data class User (

  @SerializedName("user_id"      ) var userId      : String? = null,
  @SerializedName("name"         ) var name        : String? = null,
  @SerializedName("age"          ) var age         : String? = null,
  @SerializedName("image"        ) var image       : String? = null,
  @SerializedName("date_created" ) var dateCreated : String? = null,
  @SerializedName("email"        ) var email       : String? = null

)