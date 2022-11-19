package com.harvin.evento.Model

data class CompanyModel(
    val cmpID: String?,
    val phone: String?,
    val cname: String?,
    val email: String?,
    val yoe: String?,
    val cin: String?,
    val desc: String?,
    val service: String?,
    val location: String?
){
    constructor() : this(null, null, null,null,null,null,null,null,null)
}