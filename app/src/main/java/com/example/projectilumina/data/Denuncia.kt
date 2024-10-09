package com.example.projectilumina.data
data class Denuncia(
    var id: String? = null,
    var cidade: String = "",
    var bairro: String = "",
    var tipoManutencao: String = "",
    var dataHora: String = "",
    var descricao: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var imagemUrl: String? = null // Adicionando um parâmetro para a URL da imagem
)
{
    override fun toString(): String {
        return "Denuncia(id=$id, cidade='$cidade', bairro='$bairro', tipoManutencao='$tipoManutencao', dataHora='$dataHora', descricao='$descricao', latitude=$latitude, longitude=$longitude)"
    }
}