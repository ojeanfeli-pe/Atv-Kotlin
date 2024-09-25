package com.example.atv_kotlin.classes

class Estoque {
    companion object {

        var instance = Estoque()
        private val productList = mutableListOf<Produto>()
    }

    fun adicionarProduto(produto: Produto) {
        productList.add(produto)
    }

    fun listarProdutos(): List<Produto> {
        return productList.toList()
    }

    fun calcularValorTotalEstoque(): Float {
        var total = 0f
        for (produto in productList) {
            total += produto.preco * produto.quantidade
        }
        return total
    }
}