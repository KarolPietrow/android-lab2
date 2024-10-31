package pl.karolpietrow.kp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import pl.karolpietrow.kp2.ui.theme.KP2Theme

class MainActivity : ComponentActivity() {
    private val viewModel: ShoppingListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KP2Theme {
                val productList by viewModel.productList.observeAsState(emptyList())
                ShoppingList(
                    productList = productList,
                    onAddProduct = { viewModel.addProduct(it) },
                    onTogglePurchase = { viewModel.toggleProductPurchased(it) },
                    onDeleteProduct = { viewModel.deleteProduct(it) }
                );
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun MyPreview() {
//    KP2Theme {
//        ShoppingList();
//    }
//}