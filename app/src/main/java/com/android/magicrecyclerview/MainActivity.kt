package com.android.magicrecyclerview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.android.magic_recyclerview.component.magic_recyclerview.GridEasyList
import com.android.magic_recyclerview.component.magic_recyclerview.HorizontalEasyList
import com.android.magic_recyclerview.component.magic_recyclerview.RecyclerType
import com.android.magic_recyclerview.component.magic_recyclerview.VerticalEasyList
import com.android.magic_recyclerview.model.Action
import com.android.magicrecyclerview.model.Anime
import com.android.magicrecyclerview.ui.AnimeCard
import com.android.magicrecyclerview.ui.AnimeGridCard
import com.android.magicrecyclerview.ui.emptyView
import com.android.magicrecyclerview.ui.theme.MagicRecyclerViewTheme


var DEFAULT_LIST = DataProvider.itemList

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val testList = mutableListOf<Anime>()
        for (i in 1..1){
            testList.addAll(DEFAULT_LIST.map { it })
        }

        setContent {
            MagicRecyclerViewTheme {
                var recyclerType by remember { mutableStateOf(RecyclerType.VERTICAL) }

                Scaffold(

                    topBar = {
                        TopAppBar(title = { Text(text = "Easy List") })

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
                                RecyclerType.VERTICAL -> VerticalList(testList)
                                RecyclerType.HORIZONTAL -> HorizontalList(testList)
                                RecyclerType.GRID -> GridList(testList)
                            }
                        }

                    }

                }


            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun VerticalList(list: List<Anime>) {

    var listItem by remember { mutableStateOf(list) }
    var isLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }
    val listStateAble = remember { mutableStateListOf<Anime>() }
    var test by remember { mutableStateOf(1) }
    LaunchedEffect(key1 = Unit ){
        listStateAble.addAll(list)
    }


    val deleteAction1 = Action<Anime>(
        { actionText("Delete") },
        { actionIcon(R.drawable.ic_delete) },
        backgroundColor = colorResource(R.color.color_action_4),
        onClicked = { position, item ->
            listItem = listItem.filter {
                it.anime_id != item.anime_id
            }

        })


    val deleteAction2 = Action<Anime>(
        { actionText("Delete") },
        { actionIcon(R.drawable.ic_delete) },
        backgroundColor = colorResource(R.color.color_action_5),
        onClicked = { position, item ->
            listItem = listItem.filter {
                it.anime_id != item.anime_id
            }

        })


    val deleteAction3 = Action<Anime>(
        { actionText("Delete") },
        { actionIcon(R.drawable.ic_delete) },
        backgroundColor = colorResource(R.color.color_action_6),
        onClicked = { position, item ->
            listItem = listItem.filter {
                it.anime_id != item.anime_id
            }

        })

    val archiveAction1 = Action<Anime>(
        { actionText("Archive") },
        { actionIcon(R.drawable.ic_archive) },
        backgroundColor = colorResource(R.color.color_action_1),
        onClicked = { position, item ->
            listItem = listItem.filter {
                it.anime_id != item.anime_id
            }

        })

    val archiveAction2 = Action<Anime>(
        { actionText("Archive") },
        { actionIcon(R.drawable.ic_archive) },
        backgroundColor = colorResource(R.color.color_action_2),
        onClicked = { position, item ->
            listItem = listItem.filter {
                it.anime_id != item.anime_id
            }

        })

    val archiveAction3 = Action<Anime>(
        { actionText("Archive") },
        { actionIcon(R.drawable.ic_archive) },
        backgroundColor = colorResource(R.color.color_action_3),
        onClicked = { position, item ->
            listItem = listItem.filter {
                it.anime_id != item.anime_id
            }

        })


    VerticalEasyList(
        modifier = Modifier,
        list = listStateAble,
        onItemClicked = { item, position ->
            Log.i("VerticalList", "Clicked")
        },
//        onLoadingNextPage = {
//            Log.i("VerticalList", "Last Reached")
//
//            Handler(Looper.getMainLooper()).postDelayed({
//                if (test  <= 4) {
//                    listStateAble.addAll(list.subList(0, 3))
//                    test++
//                }else
//                    listStateAble.addAll(emptyList())
//            }, 200)
//
//        },
        view = { AnimeCard(anime = it) },
        emptyView = { emptyView() },
        isLoading = isLoading,
        isRefreshing = isRefreshing,
        onRefresh={
                  isRefreshing = true
            Handler(Looper.getMainLooper()).postDelayed({
                isRefreshing = false
            }, 2000)
        },
        startActions = listOf(deleteAction1, deleteAction2, deleteAction3),
        endActions = listOf(archiveAction1, archiveAction2, archiveAction3),
        paddingVertical = 8f
    )

    Handler(Looper.getMainLooper()).postDelayed({
        isLoading = false
    }, 2000)
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
    var isLoading by remember { mutableStateOf(true) }
    HorizontalEasyList(
        list = listItem,
        view = { AnimeCard(anime = it) },
        emptyView = { emptyView() },
        isLoading = isLoading,
        paddingBetweenItems = 8f,
        paddingVertical = 8f,
        dividerView = {

        }
    )

    Handler(Looper.getMainLooper()).postDelayed({
        isLoading = false
    }, 1000)

}


@ExperimentalFoundationApi
@Composable
fun GridList(list: List<Anime>) {
    val listItem by remember { mutableStateOf(list) }
    var isLoading by remember { mutableStateOf(true) }
    val listStateAble = remember { mutableStateListOf<Anime>() }
    LaunchedEffect(key1 = Unit ){
        listStateAble.addAll(list)
    }
    GridEasyList(
        list = listItem,
        view = { AnimeGridCard(anime = it) },
        emptyView = { emptyView() },
        isLoading = isLoading,
        onRefresh = {

        },
        paddingBetweenItems = 8f,
        paddingVertical = 8f,
        columnCount = 2,
        scrollTo = 0,
        onItemClicked = {item,position->
            Log.i("Mshari"," item Clicked")

        },


    )

    Handler(Looper.getMainLooper()).postDelayed({
        isLoading = false
    }, 1000)
}

@Composable
fun tabs() {

}

