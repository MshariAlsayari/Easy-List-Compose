package com.android.magicrecyclerview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.android.magic_recyclerview.component.magic_recyclerview.GridRecyclerView
import com.android.magic_recyclerview.component.magic_recyclerview.HorizontalRecyclerView
import com.android.magic_recyclerview.component.magic_recyclerview.RecyclerType
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalRecyclerView
import com.android.magic_recyclerview.model.Action
import com.android.magicrecyclerview.model.Anime
import com.android.magicrecyclerview.ui.AnimeCard
import com.android.magicrecyclerview.ui.AnimeGridCard
import com.android.magicrecyclerview.ui.emptyView
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

                Scaffold(

                    topBar = {
                        TopAppBar(title = { Text(text = "Magic Recycler View") })

                    }

                ) {

                    Surface(color = MaterialTheme.colors.background) {

                        Column {

                            var tabIndex by remember { mutableStateOf(0) } // 1.
                            val tabTitles = listOf("Vertical", "Horizontal", "Grid")
                            Column { // 2.
                                TabRow(
                                    selectedTabIndex = tabIndex
                                ) { // 3.
                                    tabTitles.forEachIndexed { index, title ->
                                        Tab(
                                            modifier = Modifier.background(Color.White),
                                            selectedContentColor = colorResource(id = R.color.purple_200),
                                            selected = tabIndex == index, // 4.
                                            onClick = { tabIndex = index },
                                            text = { Text(text = title) }) // 5.
                                    }
                                }
                                when (tabIndex) { // 6.
                                    0 -> recyclerType = RecyclerType.VERTICAL
                                    1 -> recyclerType = RecyclerType.HORIZONTAL
                                    2 -> recyclerType = RecyclerType.GRID
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
}

@ExperimentalMaterialApi
@Composable
fun VerticalList(list: List<Anime>) {

    var listItem by remember { mutableStateOf(list) }
    var isRefresh by remember { mutableStateOf(false) }
    val deleteAction = Action<Anime>(
        { actionText("Delete") },
        { actionIcon(R.drawable.ic_delete) },
        onClicked = { position, item ->
            listItem = listItem.filter {
                it.anime_id != item.anime_id
            }

        })

    val archiveAction = Action<Anime>(
        { actionText("Archive") },
        { actionIcon(R.drawable.ic_archive) },
        onClicked = { position, item ->
            listItem = listItem.filter {
                it.anime_id != item.anime_id
            }

        })


    VerticalRecyclerView(
        modifier = Modifier,
        list = listItem,
        onItemClicked = { item, position ->
        },
        views = { AnimeCard(anime = it) },
        emptyView = { emptyView() },
        isRefreshing = isRefresh,
        onRefresh = {
            isRefresh = true
            Handler(Looper.getMainLooper()).postDelayed({
                isRefresh = false
                listItem = DEFAULT_LIST
            }, 1000)

        },
        paddingBetweenItems = 8f,
        startActions = listOf(deleteAction),
        endActions = listOf(archiveAction),
        startActionBackgroundColor = Color.Red,
        endActionBackgroundColor = Color.Green,
        actionBackgroundHeight = 100f,
        paddingVertical = 8f
    )
}

@Composable
fun actionIcon(@DrawableRes id: Int) {
    Icon(
        painter = painterResource(id = id),
        tint = Color.White,
        contentDescription = "Icon"
    )


}

@Composable
fun actionText(text: String?) {
    Text(
        text = text ?: "",
        style = MaterialTheme.typography.button,
        color = Color.White
    )
}

@Composable
fun HorizontalList(list: List<Anime>) {
    val listItem by remember { mutableStateOf(list) }
    HorizontalRecyclerView(
        list = listItem,
        views = { AnimeCard(anime = it) },
        emptyView = { emptyView() },
        paddingBetweenItems = 8f,
        paddingVertical = 8f,
        dividerView = {

        }
    )

}


@ExperimentalFoundationApi
@Composable
fun GridList(list: List<Anime>) {
    val listItem by remember { mutableStateOf(list) }
    GridRecyclerView(
        list = listItem,
        views = { AnimeGridCard(anime = it) },
        emptyView = { emptyView() },
        paddingBetweenItems = 8f,
        paddingVertical = 8f,
        columnCount = 2,
        scrollTo = 3

    )
}

@Composable
fun tabs() {

}

