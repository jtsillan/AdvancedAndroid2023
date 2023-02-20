package com.example.em_tuntiesimerkki1.datatypes.feedback

import com.google.gson.annotations.SerializedName


data class Feedback (

  @SerializedName("id"       ) var id       : Int?    = null,
  @SerializedName("name"     ) var name     : String? = null,
  @SerializedName("location" ) var location : String? = null,
  @SerializedName("value"    ) var value    : String? = null

)
{
  override fun toString(): String {
    val text = name.toString() + ": " + value.toString()
    return  text
  }
}