package com.android.magicrecyclerview

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.magic_recyclerview.component.magic_recyclerview.GridRecyclerView
import com.android.magic_recyclerview.component.magic_recyclerview.HorizontalRecyclerView
import com.android.magic_recyclerview.component.magic_recyclerview.RecyclerType
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalRecyclerView
import com.android.magic_recyclerview.model.Action
import com.android.magicrecyclerview.model.Item
import com.android.magicrecyclerview.ui.defaultEmptyView
import com.android.magicrecyclerview.ui.magicRecyclerViewItem
import com.android.magicrecyclerview.ui.theme.MagicRecyclerViewTheme


var DEFAULT_LIST = DataProvider.itemList

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
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

@ExperimentalMaterialApi
@Composable
fun VerticalList(list: List<Item>) {

    val listItem by remember { mutableStateOf(list) }
    VerticalRecyclerView(
        modifier = Modifier,
        list = listItem,
        views = { magicRecyclerViewItem(item = it) },
        emptyView = { defaultEmptyView() },
        paddingBetweenItems = 8f,
        startActions = listOf(
            Action(
                { actionText("Delete1") },
                { actionIcon(R.drawable.ic_edit) },
                onClicked = { position, item ->
                    Log.i("Mshari", "$position")
                    Log.i("Mshari", "${item.name}")

                }),
            Action(
                { actionText("Delete1") },
                { actionIcon(R.drawable.ic_edit) },
                onClicked = { position, item ->
                    Log.i("Mshari", "$position")
                    Log.i("Mshari", "${item.name}")

                }),
            Action(
                { actionText("Delete1") },
                { actionIcon(R.drawable.ic_edit) },
                onClicked = { position, item ->
                    Log.i("Mshari", "$position")
                    Log.i("Mshari", "${item.name}")

                }),
            Action(
                { actionText("Delete1") },
                { actionIcon(R.drawable.ic_edit) },
                onClicked = { position, item ->
                    Log.i("Mshari", "$position")
                    Log.i("Mshari", "${item.name}")

                }),
        ),
        endActions = listOf(
            Action({ actionText("Fav1") }, { actionIcon(R.drawable.ic_favorite) }),
            Action({ actionText("Fav2") }, { actionIcon(R.drawable.ic_favorite) })
        ),
        startActionBackgroundColor = Color.Red,
        endActionBackgroundColor = Color.Green,
        actionBackgroundHeight = 65f.dp,
    )
}

@Composable
fun actionIcon(@DrawableRes id: Int) {
    Icon(

        painter = painterResource(id = id),
        contentDescription = "Icon"
    )


}

@Composable
fun actionText(text: String?) {


    Text(
        text = text ?: "",
        style = MaterialTheme.typography.button,
        color = Color.Black
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
        columnCount = 3,
        scrollTo = 3
    )
}

