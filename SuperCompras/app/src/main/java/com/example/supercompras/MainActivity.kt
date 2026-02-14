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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import java.text.SimpleDateFormat
import java.util.Locale

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
fun AdicionarItem(aoSalvarItem: (item: ItemCompra) -> Unit, modifier: Modifier = Modifier) {
    var texto by rememberSaveable() { mutableStateOf("") }
    OutlinedTextField(
        value = texto,
        onValueChange = { texto = it },
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
            aoSalvarItem(ItemCompra(texto, getDataHora(), false))
            texto = ""
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

fun getDataHora(): String {
    val dataHoraAtual = System.currentTimeMillis()
    val dataHoraFormatada = SimpleDateFormat("EEEE (dd/MM/yyyy) 'às' HH:mm", Locale("pt", "BR"))
    return dataHoraFormatada.format(dataHoraAtual)
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
        AdicionarItem(aoSalvarItem = { novoItem ->
            listaDeItens = listaDeItens + novoItem
        })
        Spacer(modifier = Modifier.height(48.dp))
        Titulo("Lista de Compras")
        ListaDeItens(
            lista = listaDeItens.filter { !it.foiComprado },
            aoMudarStatus = { itemSelecionado ->
                listaDeItens = listaDeItens.map { itemMap ->
                    if (itemSelecionado == itemMap) {
                        itemSelecionado.copy(foiComprado = !itemSelecionado.foiComprado)
                    } else {
                        itemMap
                    }
                }
            },
            aoRemoverItem = { itemRemovido ->
                listaDeItens = listaDeItens - itemRemovido
            },
            aoEditarItem = { itemEditado, novoTexto ->
                listaDeItens = listaDeItens.map { itemAtual ->
                    if (itemAtual == itemEditado) {
                        itemAtual.copy(texto = novoTexto)
                    } else {
                        itemAtual
                    }
                }
            }
        )
        Titulo("Comprado")

        if (listaDeItens.any{
                it.foiComprado
            }
        ) {
            ListaDeItens(
                lista = listaDeItens.filter { it.foiComprado },
                aoMudarStatus = { itemSelecionado ->
                    listaDeItens = listaDeItens.map { itemMap ->
                        if (itemSelecionado == itemMap) {
                            itemSelecionado.copy(foiComprado = !itemSelecionado.foiComprado)
                        } else {
                            itemMap
                        }
                    }
                },
                aoRemoverItem = { itemRemovido ->
                    listaDeItens = listaDeItens - itemRemovido
                },
                aoEditarItem = { itemEditado, novoTexto ->
                    listaDeItens = listaDeItens.map { itemAtual ->
                        if (itemAtual == itemEditado) {
                            itemAtual.copy(texto = novoTexto)
                        } else {
                            itemAtual
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ListaDeItens(
        lista: List<ItemCompra>,
        aoMudarStatus: (item: ItemCompra) -> Unit = {},
        aoEditarItem: (item: ItemCompra, novoTexto: String) -> Unit = {_, _ -> },
        aoRemoverItem: (item: ItemCompra) -> Unit = {},
        modifier: Modifier = Modifier,
    ) {
    Column(modifier = modifier) {
        lista.forEach { item ->
            ItemDaLista(
                item = item,
                aoMudarStatus = aoMudarStatus,
                aoEditarItem = aoEditarItem,
                aoRemoverItem = aoRemoverItem
            )
        }
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
fun ItemDaLista(
    item: ItemCompra,
    aoMudarStatus: (item: ItemCompra) -> Unit = {},
    aoRemoverItem: (item: ItemCompra) -> Unit = {},
    aoEditarItem: (item: ItemCompra, texto: String) -> Unit = {_, _ -> },
    modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            var textoEditado by rememberSaveable { mutableStateOf(item.texto) }
            var edicao by rememberSaveable { mutableStateOf(false) }

            Checkbox(
                checked = item.foiComprado,
                onCheckedChange = {
                    aoMudarStatus(item)
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(24.dp)
            )
            if (edicao) {
                OutlinedTextField(
                    value = textoEditado,
                    onValueChange = { textoEditado = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp)
                )
                IconButton(
                    onClick = {
                        aoEditarItem(item, textoEditado)
                        edicao = false
                              },
                ) {
                    Icone(
                        Icons.Default.Done,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                Text(
                    text = item.texto,
                    modifier = Modifier.weight(1f),
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
            IconButton(
                onClick = {aoRemoverItem(item)},
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icone(
                    Icons.Default.Delete,
                    modifier = Modifier
                        .size(16.dp)
                )
            }
            IconButton(
                onClick = {
                    edicao = true
                          },
            ) {
                Icone(
                    Icons.Default.Edit,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Text(
            item.datahora,
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
        ItemDaLista(ItemCompra(texto = "Suco", getDataHora(), false))
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
    val texto: String,
    val datahora: String,
    var foiComprado: Boolean = false
)