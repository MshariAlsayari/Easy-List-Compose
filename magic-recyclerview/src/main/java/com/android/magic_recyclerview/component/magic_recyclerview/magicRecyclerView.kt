package com.android.magic_recyclerview.component.magic_recyclerview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.android.magic_recyclerview.Constants.ACTION_HEIGHT
import com.android.magic_recyclerview.Constants.COLUMN_COUNT
import com.android.magic_recyclerview.Constants.PADDING_BETWEEN_ITEMS
import com.android.magic_recyclerview.Constants.PADDING_HORIZONTAL
import com.android.magic_recyclerview.Constants.PADDING_VERTICAL
import com.android.magic_recyclerview.component.action_row.ActionsRow
import com.android.magic_recyclerview.model.Action
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@Composable
fun <T> VerticalRecyclerView(
    modifier: Modifier = Modifier,
    list: List<T>,
    views: @Composable LazyItemScope.(item: T) -> Unit,
    dividerView: (@Composable () -> Unit)? = null,
    emptyView: (@Composable () -> Unit)? = null,
    startActions: List<Action<T>> = listOf(),
    endActions: List<Action<T>> = listOf(),
    startActionBackgroundColor: Color = Color.Transparent,
    endActionBackgroundColor: Color = Color.Transparent,
    actionBackgroundRadiusCorner: Dp = Dp(0f),
    actionBackgroundHeight: Dp = Dp(ACTION_HEIGHT),
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    paddingBetweenItems: Float = PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = PADDING_VERTICAL,
    paddingHorizontal: Float = PADDING_HORIZONTAL,
    scrollTo: Int = 0,
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isArabic = LocalLayoutDirection.current == LayoutDirection.Rtl
    val lacyColumn: @Composable () -> Unit = {
        LazyColumn(
            modifier = modifier,
            state = listState,
            verticalArrangement = Arrangement.spacedBy(paddingBetweenItems.dp),
            contentPadding = PaddingValues(
                horizontal = paddingHorizontal.dp,
                vertical = paddingVertical.dp
            )
        ) {


            itemsIndexed(list) { index, item ->

                Column(Modifier.fillMaxSize()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ActionsRow(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f, true)
                                    .height(actionBackgroundHeight),
                                item = item,
                                position = index,
                                radiusCorner = actionBackgroundRadiusCorner,
                                backgroundColor = startActionBackgroundColor,
                                actions = startActions,
                            )
                            ActionsRow(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f, true)
                                    .height(actionBackgroundHeight),
                                item = item,
                                position = index,
                                radiusCorner = actionBackgroundRadiusCorner,
                                backgroundColor = endActionBackgroundColor,
                                actions = endActions,
                            )

                        }

                        SwappableItem(
                            modifier = modifier,
                            mainItem = { views(item) },
                            enableLTRSwipe = if (isArabic) endActions.isNotEmpty() else startActions.isNotEmpty(),
                            enableRTLSwipe = if (isArabic) startActions.isNotEmpty() else endActions.isNotEmpty(),
                        )
                    }

                    if (index != list.lastIndex && dividerView != null) {
                        Surface(modifier = Modifier.padding(top = paddingBetweenItems.dp)) {
                            dividerView()
                        }

                    }
                }


            }


            coroutineScope.launch {
                listState.animateScrollToItem(scrollTo)
            }


        }
    }


    if (list.isNotEmpty()) {

        if (onRefresh == null) {

            lacyColumn()


        } else { // with refresh layout


            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { onRefresh() },
            ) {

                lacyColumn()
            }


        }
    } else { // if list is empty
        emptyView(emptyView)
    }

}


@Composable
fun <T> HorizontalRecyclerView(
    modifier: Modifier = Modifier,
    list: List<T>,
    views: @Composable LazyItemScope.(item: T) -> Unit,
    emptyView: (@Composable () -> Unit)? = null,
    dividerView: (@Composable () -> Unit)? = null,
    paddingBetweenItems: Float = PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = PADDING_VERTICAL,
    paddingHorizontal: Float = PADDING_HORIZONTAL,
    scrollTo: Int = 0,
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    if (list.isNotEmpty()) {
        LazyRow(
            modifier = modifier,
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(paddingBetweenItems.dp),
            contentPadding = PaddingValues(
                horizontal = paddingHorizontal.dp,
                vertical = paddingVertical.dp
            )
        ) {
            itemsIndexed(list) { index, item ->
                views(item)
                if (index != list.lastIndex) {
                    Surface(modifier = Modifier.padding(start = paddingBetweenItems.dp)) {
                        dividerView?.invoke()
                    }

                }


            }

            coroutineScope.launch {
                listState.animateScrollToItem(scrollTo)
            }

        }

    } else {
        emptyView(emptyView)

    }

}


@ExperimentalFoundationApi
@Composable
fun <T> GridRecyclerView(
    modifier: Modifier = Modifier,
    list: List<T>,
    views: @Composable (LazyItemScope.(item: T) -> Unit),
    emptyView: @Composable (() -> Unit)? = null,
    paddingBetweenItems: Float = PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = PADDING_VERTICAL,
    paddingHorizontal: Float = PADDING_HORIZONTAL,
    columnCount: Int = COLUMN_COUNT,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    scrollTo: Int = 0,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val gridList: @Composable () -> Unit = {
        LazyVerticalGrid(
            modifier = modifier,
            state = listState,
            cells = GridCells.Fixed(columnCount),
            verticalArrangement = Arrangement.spacedBy(paddingBetweenItems.dp),
            horizontalArrangement = Arrangement.spacedBy(paddingBetweenItems.dp),
            contentPadding = PaddingValues(
                vertical = paddingVertical.dp,
                horizontal = paddingHorizontal.dp
            )
        ) {
            items(
                items = list,
                itemContent = {
                    views(it)
                })
        }
    }

    if (list.isNotEmpty()) {

        if (onRefresh == null) {
            gridList()
        } else {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { onRefresh() },
            ) {
                gridList()

            }
        }

        coroutineScope.launch {
            listState.animateScrollToItem(scrollTo)
        }


    } else {
        emptyView(emptyView)

    }

}

@Composable
fun emptyView(view: (@Composable () -> Unit)? = null) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        view?.invoke()
    }
}

