package com.example.em_tuntiesimerkki1.datatypes.todoitem

import com.google.gson.annotations.SerializedName


data class TodoItem (

  @SerializedName("userId"    ) var userId    : Int?     = null,
  @SerializedName("id"        ) var id        : Int?     = null,
  @SerializedName("title"     ) var title     : String?  = null,
  @SerializedName("completed" ) var completed : Boolean? = null

)