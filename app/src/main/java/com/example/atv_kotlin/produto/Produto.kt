package com.example.atv_kotlin.produto

data class Produto
    (var nome: String,
    var categoria: String,
    var preco: Float,
    var quantidade: Int) {

        companion object{
            val produtos: MutableList<Produto> = mutableListOf()
        }

}