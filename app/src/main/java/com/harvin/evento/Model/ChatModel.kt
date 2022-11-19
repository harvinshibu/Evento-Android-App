package com.harvin.evento.Model

data class ChatModel(val sender: String?,
                     val receiver: String?,
                     val msg: String?,
                     val isseen: Boolean?){
    constructor() : this(null, null, null,null)
}
