package com.example.projectilumina.data

data class User(
    val id: String? = null,
    val nome: String? = null,
    val email: String? = null,
    val cpf: String? = null,
    val telefone: String? = null,
    val rua: String? = null,
    val bairro: String? = null,
    val latitude: Double,
    val longitude: Double ,
    val token: String? = null
){
    constructor() : this(
        id = null,
        nome = null,
        email = null,
        cpf = null,
        telefone = null,
        rua = null,
        bairro = null,
        latitude = 0.0,
        longitude = 0.0,
        token = null
    )
    override fun toString(): String {
        return "Users(id=$id, nome='$nome', email='$email', cpf='$cpf', telefone='$telefone', rua='$rua',bairro=$bairro, latitude=$latitude, longitude=$longitude, token=$token)"
    }
}