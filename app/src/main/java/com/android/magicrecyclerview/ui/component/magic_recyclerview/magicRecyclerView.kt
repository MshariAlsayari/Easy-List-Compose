package com.android.magicrecyclerview.ui.component.magic_recyclerview

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
import com.android.magicrecyclerview.Constants.ACTION_HEIGHT
import com.android.magicrecyclerview.Constants.COLUMN_COUNT
import com.android.magicrecyclerview.Constants.PADDING_BETWEEN_ITEMS
import com.android.magicrecyclerview.Constants.PADDING_HORIZONTAL
import com.android.magicrecyclerview.Constants.PADDING_VERTICAL
import com.android.magicrecyclerview.model.Action
import com.android.magicrecyclerview.ui.component.action_cards.ActionsRow
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
    startActions: List<Action> = listOf(),
    endActions: List<Action> = listOf(),
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
            state = listState,
            verticalArrangement = Arrangement.spacedBy(paddingBetweenItems.dp),
            contentPadding = PaddingValues(
                horizontal = paddingHorizontal.dp,
                vertical = paddingVertical.dp
            )
        ) {


            itemsIndexed(list) { index, item ->

                Column {
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
                                radiusCorner = actionBackgroundRadiusCorner,
                                backgroundColor = startActionBackgroundColor,
                                actions = startActions,
                                onFirstActionClicked = {},
                                onSecondActionClicked = {},
                                onThirdActionClicked = {}
                            )
                            ActionsRow(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f, true)
                                    .height(actionBackgroundHeight),
                                radiusCorner = actionBackgroundRadiusCorner,
                                backgroundColor = endActionBackgroundColor,
                                actions = endActions,
                                onFirstActionClicked = {},
                                onSecondActionClicked = {},
                                onThirdActionClicked = {}
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
    list: List<T>,
    views: @Composable (LazyItemScope.(item: T) -> Unit),
    emptyView: @Composable (() -> Unit)? = null,
    paddingBetweenItems: Float = PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = PADDING_VERTICAL,
    paddingHorizontal: Float = PADDING_HORIZONTAL,
    columnCount: Int = COLUMN_COUNT,
) {

    if (list.isNotEmpty()) {

        LazyVerticalGrid(
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

