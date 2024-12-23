package pl.karolpietrow.kp2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

@Composable
fun ShoppingList (
    productList: List<Product>,
    onAddProduct: (Product) -> Unit,
    onTogglePurchase: (String) -> Unit,
    onDeleteProduct: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var productName by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        if (productName.isNotBlank()) {
                            val newProduct = Product(
                                id = UUID.randomUUID().toString(),
                                productName,
                                productQuantity.toIntOrNull() ?: 1
                            )
                            onAddProduct(newProduct)
                            showDialog = false
                            productName = ""
                            productQuantity = ""
                        }
                    }
                ) { Text("Dodaj") }
            },
            dismissButton = { Button(onClick = { showDialog = false }) { Text("Anuluj") } },
            title = { Text("Dodaj produkt do listy") },
            text = {
                Column {
                    TextField(
                        label = { Text("Nazwa produktu") },
                        value = productName,
                        onValueChange = { productName = it }
                    )
                    TextField(
                        label = { Text("Ilość") },
                        value = productQuantity,
                        onValueChange = { productQuantity = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Lista zakupów",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add icon",
                modifier = Modifier.padding(end=8.dp)
            )
            Text("Dodaj produkt")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Nazwa produktu",
                modifier = Modifier.weight(1.5f),
                fontWeight = FontWeight.Bold

            )
            Text(
                text = "Ilość",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Akcje",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold

            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)) {
            items(productList) { product ->
                ProductRow(
                    product = product,
                    onPurchaseClick = { onTogglePurchase(product.id) },
                    onDeleteClick = { onDeleteProduct(product.id) }
                )
            }
        }
    }
}

@Composable
fun ProductRow(
    product: Product,
    onPurchaseClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        AlertDialog(
             icon = { Icon(Icons.Default.Delete, contentDescription = "Delete icon") },

            onDismissRequest = { showDeleteConfirmation = false},
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClick()
                        showDeleteConfirmation = false
                    }
                ) { Text("Usuń")}
            },
            dismissButton = { Button(onClick = { showDeleteConfirmation = false }) { Text("Anuluj") } },
            title = { Text("Potwierdzenie usunięcia") },
            text = { Text("Czy na pewno chcesz usunąć produkt \"${product.name}\"?")}
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
//        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text=product.name,
            modifier = Modifier.weight(1.5f)
        )
        Text(
            text=product.quantity.toString(),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick=onPurchaseClick,modifier = Modifier.weight(0.5f)) {
            Icon(imageVector =
            if (product.isPurchased)
                Icons.Default.Done
            else
                Icons.Default.Clear, "Purchase icon"
            )
        }
        IconButton(onClick = { showDeleteConfirmation=true },modifier = Modifier.weight(0.5f)) {
            Icon(imageVector = Icons.Default.Delete, "Delete icon")
        }
    }
}