package com.example.supercompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.supercompras.ui.theme.Coral
import com.example.supercompras.ui.theme.Marinho
import com.example.supercompras.ui.theme.SuperComprasTheme
import com.example.supercompras.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperComprasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaDeCompras(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AdicionarItem(aoSalvarItem: (texto: String) -> Unit, modifier: Modifier = Modifier) {
    var texto = rememberSaveable() { mutableStateOf("") }
    OutlinedTextField(
        value = texto.value,
        onValueChange = { texto.value = it },
        placeholder = {
            Text(text="Digite o item que deseja adicionar",
                color = Color.Gray,
                style = Typography.bodyMedium
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )

    Button(
        onClick = {
            aoSalvarItem(texto.value)
            texto.value = ""
        },
        shape = RoundedCornerShape(24.dp),
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Coral,
        )
    ) {
        Text(text = "Salvar item",
            color = Color.White,
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun ListaDeCompras(modifier: Modifier = Modifier) {
    var listaDeItens by rememberSaveable { mutableStateOf(listOf<ItemCompra>()) }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ImagemTopo()
        AdicionarItem(aoSalvarItem = { textoNovo ->
            listaDeItens = listaDeItens + ItemCompra(textoNovo)
        })
        Spacer(modifier = Modifier.height(48.dp))
        Titulo("Lista de Compras")
        Column {
            listaDeItens.forEach { item ->
                ItemDaLista(item)
            }
        }
        Titulo("Comprado")
    }
}

@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(text = texto, modifier = modifier, style = Typography.headlineLarge)
}

@Composable
fun ImagemTopo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.image_topo),
        contentDescription = null,
        modifier = modifier.size(160.dp)
    )
}

@Composable
fun Icone(icone: ImageVector, modifier: Modifier = Modifier) {
    Icon(
        icone,
        contentDescription = null,
        modifier = modifier,
        tint = Marinho
    )
}

@Composable
fun ItemDaLista(item: ItemCompra, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = {},
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(24.dp)
            )
            Text(
                text = item.texto,
                modifier = Modifier.weight(1f),
                style = Typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Icone(
                Icons.Default.Delete,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(16.dp)
            )
            Icone(
                Icons.Default.Edit,
                modifier = Modifier.size(16.dp)
            )

        }
        Text(
            "Segunda-feira (31/10/2022) às 08:30",
            Modifier.padding(8.dp),
            style = Typography.labelSmall
        )
    }
}

@Preview
@Composable
private fun AdiconarItemPreview() {
    SuperComprasTheme {
        AdicionarItem(aoSalvarItem = {})
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello Leo!",
        modifier = modifier
    )
}

@Preview
@Composable
private fun ItemDaListaPreview() {
    SuperComprasTheme {
        ItemDaLista(ItemCompra(texto = "Suco"))
    }
}

@Preview
@Composable
private fun IconeEditPreview() {
    SuperComprasTheme {
        Icone(Icons.Default.Delete)
    }
}

@Preview
@Composable
private fun ImagemTopoPreview() {
    SuperComprasTheme {
        ImagemTopo()
    }
}

@Preview
@Composable
private fun TituloPreview() {
    SuperComprasTheme {
        Titulo(texto = "Lista de Compras")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SuperComprasTheme {
        Greeting("Android")
    }
}

data class ItemCompra(
    val texto: String
)