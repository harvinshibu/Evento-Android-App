package com.harvin.evento.Model

data class BookingModel(val bookID: String?,
                        val packID: String?,
                        val cmpID: String?,
                        val userID: String?,
                        val pkgName: String?,
                        val pkgPrice: String?,
                        val pkgDesc: String?,
                        val status: String?){
    constructor() : this(null, null, null,null,null, null,null,null)
}
