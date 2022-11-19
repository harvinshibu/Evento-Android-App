package com.harvin.evento.Model

data class UserModel( val userID: String?,
                      val cname: String?,
                      val email: String?){
    constructor() : this(null, null, null)
}
