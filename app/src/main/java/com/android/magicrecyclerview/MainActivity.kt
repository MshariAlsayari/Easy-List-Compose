package com.android.magicrecyclerview

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.magicrecyclerview.model.Item
import com.android.magicrecyclerview.ui.component.action_cards.DeleteCard
import com.android.magicrecyclerview.ui.component.magic_recyclerview.*
import com.android.magicrecyclerview.ui.component.swippable_item.SwipeDirection
import com.android.magicrecyclerview.ui.theme.MagicRecyclerViewTheme


var DEFAULT_LIST = DataProvider.itemList

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagicRecyclerViewTheme {
                var recyclerType by remember { mutableStateOf(RecyclerType.VERTICAL) }

                Surface(color = MaterialTheme.colors.background) {

                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            Button(onClick = {
                                recyclerType = RecyclerType.VERTICAL
                            }) {
                                Text(text = "Vertical")
                            }

                            Button(onClick = {
                                recyclerType = RecyclerType.HORIZONTAL
                            }) {
                                Text(text = "Horizontal")
                            }

                            Button(onClick = {
                                recyclerType = RecyclerType.GRID
                            }) {
                                Text(text = "Grid")
                            }

                        }


                        when (recyclerType) {
                            RecyclerType.VERTICAL -> VerticalList(DEFAULT_LIST)
                            RecyclerType.HORIZONTAL -> HorizontalList(DEFAULT_LIST)
                            RecyclerType.GRID -> GridList(DEFAULT_LIST)
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun VerticalList(list: List<Item>) {
    var isRefreshing by remember { mutableStateOf(false) }
    var listItem by remember { mutableStateOf(list) }
    VerticalRecyclerView(
        list = listItem,
        views = { magicRecyclerViewItem(item = it) },
        emptyView = { defaultEmptyView() },
        paddingBetweenItems = 8f,
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = !isRefreshing
            Handler().postDelayed({
                isRefreshing = !isRefreshing
                listItem = DataProvider.itemList
            }, 1000)
        },
        swipeItem = { DeleteCard() },
        onSwiped = { item, position ->
            Log.i("Mshari", "item:${item.id}")
            Log.i("Mshari", "position:${position}")
            listItem = listItem.filter { it != item }
        },
        swipeDirection = SwipeDirection.RIGHT_TO_LEFT,

        )
}

@Composable
fun HorizontalList(list: List<Item>) {
    val listItem by remember { mutableStateOf(list) }
    HorizontalRecyclerView(
        list = listItem,
        views = { magicRecyclerViewItem(item = it) },
        emptyView = { defaultEmptyView() },
        paddingBetweenItems = 8f,
        dividerView = {

        }
    )

}

@ExperimentalFoundationApi
@Composable
fun GridList(list: List<Item>) {
    val listItem by remember { mutableStateOf(list) }
    GridRecyclerView(
        list = listItem,
        views = { magicRecyclerViewItem(item = it) },
        emptyView = { defaultEmptyView() },
        paddingBetweenItems = 8f,
        columnCount = 3
    )
}

