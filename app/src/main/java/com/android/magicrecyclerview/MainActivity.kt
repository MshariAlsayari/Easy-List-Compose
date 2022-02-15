package com.android.magicrecyclerview

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.magicrecyclerview.model.Action
import com.android.magicrecyclerview.model.Item
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
    val context = LocalContext.current
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
        startActions = listOf(
            Action("Delete1", R.drawable.ic_edit,
            onClicked = {
                Toast.makeText(
                    context,
                    "Delete1",
                    Toast.LENGTH_SHORT
                ).show()
            }), Action("Delete1", R.drawable.ic_edit,
                onClicked = {
                    Toast.makeText(
                        context,
                        "Delete2",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        ),
        endActions = listOf(
            Action("favorite", R.drawable.ic_favorite,
                onClicked = {
                    Toast.makeText(
                        context,
                        "favorite1",
                        Toast.LENGTH_SHORT
                    ).show()
                }), Action("favorite", R.drawable.ic_favorite,  onClicked = {
                Toast.makeText(
                    context,
                    "favorite2",
                    Toast.LENGTH_SHORT
                ).show()
            })
        ),
        startBackgroundColor = Color.Red,
        endBackgroundColor = Color.Green,
        swipeModifier = Modifier.fillMaxWidth(),
        onStartSwiped = { item, position ->
            Log.i("Mshari", "onStartSwiped")
            Log.i("Mshari", "item:${item.id}")
            Log.i("Mshari", "position:${position}")
            listItem = listItem.filter { it != item }
        },

        onEndSwiped = { item, position ->
            Log.i("Mshari", "onEndSwiped")
            Log.i("Mshari", "item:${item.id}")
            Log.i("Mshari", "position:${position}")
        }
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

