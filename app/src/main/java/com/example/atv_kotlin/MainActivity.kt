package com.example.atv_kotlin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.atv_kotlin.produto.Produto
import com.google.gson.Gson
import androidx.compose.material3.ListItem as ListItem

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LayoutMain()
        }
    }
}

@Composable
fun LayoutMain(){
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "cadastro") {

        composable("cadastro") { CadastroProdutos(navController) }
        composable("lista") { ListaProdutos(navController )}
        composable("detalhes") { backStackEntry ->

            val produtoJson =
                backStackEntry.arguments?.getString("produtoJson")

            val produto = Gson().fromJson(produtoJson, Produto::class.java)
            DetalhesProduto(navController, produto)
        }
    }
}

@Composable
fun CadastroProdutos(navController: NavHostController){
    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        Text(text = "CADASTRO DE PRODUTOS", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(25.dp))


        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text(text = "Nome do Produto") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text(text = "Categoria") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = preco,
            onValueChange = { preco = it },
            label = { Text(text = "Preço:") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        TextField(
            value = quantidade,
            onValueChange = { quantidade = it },
            label = { Text(text = "Quantidade em Estoque") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(onClick = {
            if (nome.isNotEmpty() && categoria.isNotEmpty() && preco.isNotEmpty() && quantidade.isNotEmpty()) {
                Produto.produtos.add(Produto(nome, categoria, preco.toFloat(), quantidade.toInt()))

                // Limpa os campos após o cadastro
                nome = ""
                categoria = ""
                preco = ""
                quantidade = ""
                Toast.makeText(context, "Produto cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

                navController.navigate("cadastro"){
                    popUpTo("cadastro"){inclusive = true}
                }
            } else {
                Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()

            }
        }) {
            Text(text = "Cadastrar Produtos")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Button(onClick = {
            navController.navigate("lista")
        }) {
            Text(text = "Listar Produtos")
        }
    }

}

@Composable
fun ListaProdutos(navController: NavHostController) {
    // Acesse a lista de produtos da MainActivity
    val produtos = Produto.produtos

    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)){
        items(produtos) { produto ->
            ListItem(
                modifier = Modifier.clickable {
                    // Quando o item é clicado, navegue para a tela de detalhes
                    val produtoJson = Gson().toJson(produto)
                    navController.navigate("detalhes?produtoJson=$produtoJson")
                },
                headlineContent = { Text("${produto.nome} (${produto.quantidade} unidades)")  },
                supportingContent = { Text("Categoria: ${produto.categoria}") },
                trailingContent = {
                    // Adiciona o botão "Detalhes" ao lado do preço
                    Button(onClick = {
                        // Navega para a tela de detalhes ao clicar no botão
                        val produtoJson = Gson().toJson(produto)
                        navController.navigate("detalhes?produtoJson=$produtoJson")
                    }) {
                        Text("Detalhes")
                    }
                }
            )
        }
    }
}

@Composable
fun DetalhesProduto(navController: NavHostController, produto: Produto){
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(15.dp)) {

        Spacer(modifier = Modifier.height(35.dp))

        Text(text = "DETALHES DOS PRODUTOS", fontSize = 22.sp, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Nome: ${produto.nome}\n"+
                "Categoria: ${produto.categoria}\n"+
                "Preço: R$ ${String.format("%.2f",produto.preco)}\n"+
                "Quantidade: ${produto.quantidade}\n",
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth())

        Button(onClick = {

            navController.popBackStack()
        }) {
            Text(text = "VOLTAR")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview(){
    LayoutMain()
}



