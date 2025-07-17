package com.ariftuncer.realtime_chatapp.data.model

data class Chat (
    val uid : String = "",
    val profile : String?,
    val friendName : String = "",
    val timeStamp : Long = 0L


){
    constructor() : this("", null, "", 0L)

}