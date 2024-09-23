package com.example.atv_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.atv_kotlin.ui.theme.AtvKotlinTheme

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

}

@Composable
fun CadastroProdutos(){

}

@Composable
fun ListaProdutos(){
}

@Composable
fun DetalhesProduto(){
    
}
@Preview(showBackground = true)
@Composable
fun Preview(){
    LayoutMain()
}



