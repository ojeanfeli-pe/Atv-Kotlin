package com.example.atv_kotlin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atv_kotlin.classes.Estoque
import com.example.atv_kotlin.classes.Produto
import com.google.gson.Gson
import androidx.compose.material3.ListItem as ListItem

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           LayoutMain(Estoque())
        }
    }
}
@Composable
fun LayoutMain(estoque: Estoque) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "cadastro") {
        composable("cadastro") { CadastroProdutos(navController, estoque) }
        composable("lista") { ListaProdutos(navController, estoque) }
        composable("detalhes/{produtoJson}") { backStackEntry ->
            val produtoJson = backStackEntry.arguments?.getString("produtoJson")
            if (produtoJson != null) {
                val produto = Gson().fromJson(produtoJson, Produto::class.java)
                DetalhesProduto(navController, produto)
            } else {
                Toast.makeText(LocalContext.current, "Erro ao carregar detalhes do produto", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
        composable("estatisticas") { EstatisticasEstoque(navController, estoque) }
    }
}

@Composable
fun CadastroProdutos(navController: NavHostController, estoque: Estoque) {
    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var produtoCadastrado by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "CADASTRO DE PRODUTOS", fontSize = 20.sp)

        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do Produto") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = preco,
            onValueChange = { preco = it },
            label = { Text("Preço") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = quantidade,
            onValueChange = { quantidade = it },
            label = { Text("Quantidade em Estoque") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(onClick = {
            if (nome.isNotEmpty() && categoria.isNotEmpty() && preco.isNotEmpty() && quantidade.isNotEmpty()) {
                val precoFloat = preco.toFloatOrNull()
                val quantidadeInt = quantidade.toIntOrNull()

                if (precoFloat != null && precoFloat >= 0 && quantidadeInt != null && quantidadeInt >= 1) {
                    estoque.adicionarProduto(Produto(nome, categoria, precoFloat, quantidadeInt))
                    produtoCadastrado = true
                    Toast.makeText(context, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    if (precoFloat == null || precoFloat < 0) {
                        Toast.makeText(context, "Preço deve ser maior ou igual a zero.", Toast.LENGTH_SHORT).show()
                    }
                    if (quantidadeInt == null || quantidadeInt < 1) {
                        Toast.makeText(context, "Quantidade deve ser maior ou igual a 1.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Cadastrar Produto")
        }

        if (produtoCadastrado) {
            Button(onClick = { navController.navigate("lista") }) {
                Text("Lista de Produtos")
            }
        }
    }
}

@Composable
fun ListaProdutos(navController: NavHostController, estoque: Estoque) {
    val produtos = estoque.listarProdutos()

    Column(modifier = Modifier.fillMaxSize().padding(15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        if (produtos.isEmpty()) {
            Text(text = "Nenhum produto cadastrado.", fontSize = 18.sp)
        } else {
            LazyColumn {
                items(Estoque.instance.listarProdutos()) { produto ->

                    Text(text = "${produto.nome} (${produto.quantidade} unidades)")
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {
                        val produtoJson = Gson().toJson(produto)
                        navController.navigate("detalhes/$produtoJson")
                    }) {
                        Text("Detalhes")
                    }
                }

                item {
                    Button(onClick = { navController.navigate("estatisticas") }) {
                        Text("Estatísticas")
                    }
                }
            }
        }
    }
        Spacer(modifier = Modifier.width(16.dp).padding(15.dp))
    }


@Composable
fun DetalhesProduto(navController: NavHostController, produto: Produto) {
    Column(
        modifier = Modifier.fillMaxSize().padding(15.dp), verticalArrangement = Arrangement.Center
    ) {
        Text(text = "DETALHES DO PRODUTO", fontSize = 22.sp)

        Text("Nome: ${produto.nome}", fontSize = 18.sp)
        Text("Categoria: ${produto.categoria}", fontSize = 18.sp)
        Text("Preço: R$ ${String.format("%.2f", produto.preco)}", fontSize = 18.sp)
        Text("Quantidade: ${produto.quantidade}", fontSize = 18.sp)

        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}


@Composable
fun EstatisticasEstoque(navController: NavHostController, estoque: Estoque) {
    val valorTotalEstoque = estoque.calcularValorTotalEstoque()
    val quantidadeTotalProdutos = estoque.listarProdutos().sumOf { it.quantidade }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "ESTATÍSTICAS DO ESTOQUE", fontSize = 22.sp)

        Text("Valor Total do Estoque: R$ ${String.format("%.2f", valorTotalEstoque)}", fontSize = 18.sp)
        Text("Quantidade Total de Produtos: $quantidadeTotalProdutos", fontSize = 18.sp)

        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}

@Composable
fun MostrarValorTotalEstoque(estoque: Estoque) {
    val valorTotal = estoque.calcularValorTotalEstoque()
    Text(
        text = "Valor Total do Estoque: R$ ${String.format("%.2f", valorTotal)}",
        fontSize = 18.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

@Preview
@Composable
fun PreviewCadastro() {
    LayoutMain(Estoque())
}