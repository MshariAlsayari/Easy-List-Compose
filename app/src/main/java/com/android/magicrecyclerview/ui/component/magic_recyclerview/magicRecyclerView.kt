package com.android.magicrecyclerview.ui.component.magic_recyclerview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.android.magicrecyclerview.Constants.COLUMN_COUNT
import com.android.magicrecyclerview.Constants.PADDING_BETWEEN_ITEMS
import com.android.magicrecyclerview.Constants.PADDING_HORIZONTAL
import com.android.magicrecyclerview.Constants.PADDING_VERTICAL
import com.android.magicrecyclerview.model.Action
import com.android.magicrecyclerview.ui.component.action_cards.EndActionCard
import com.android.magicrecyclerview.ui.component.action_cards.StartActionCard
import com.android.magicrecyclerview.ui.component.swippable_item.SwipeDirection
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@Composable
fun <T> VerticalRecyclerView(
    list: List<T>,
    views: @Composable LazyItemScope.(item: T) -> Unit,
    dividerView: (@Composable () -> Unit)? = null,
    emptyView: (@Composable () -> Unit)? = null,
    startActions: List<Action> = listOf(),
    endActions: List<Action> = listOf(),
    swipeModifier: Modifier = Modifier,
    startBackgroundColor: Color = Color.Transparent,
    endBackgroundColor: Color = Color.Transparent,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    paddingBetweenItems: Float = PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = PADDING_VERTICAL,
    paddingHorizontal: Float = PADDING_HORIZONTAL,
    scrollTo: Int = 0,
    onStartSwiped: ((item: T, position: Int) -> Unit)? = null,
    onEndSwiped: ((item: T, position: Int) -> Unit)? = null,
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isArabic =  LocalLayoutDirection.current == LayoutDirection.Rtl
    var startSwipeItem : (@Composable () -> Unit)? = null
    var endSwipeItem : (@Composable () -> Unit)? = null

    if (isArabic){

        if (startActions.isNotEmpty())
        startSwipeItem = { EndActionCard(
            modifier = swipeModifier,
            backgroundColor = startBackgroundColor,
            actions = startActions
        )}

        if (endActions.isNotEmpty())
        endSwipeItem = { StartActionCard(
            modifier = swipeModifier,
            backgroundColor = endBackgroundColor,
            actions = endActions
        )}
    }else{

        if (startActions.isNotEmpty())
        startSwipeItem = { StartActionCard(
            modifier = swipeModifier,
            backgroundColor = endBackgroundColor,
            actions = endActions
        )
        }

        if (endActions.isNotEmpty())
        endSwipeItem = { EndActionCard(
            modifier = swipeModifier,
            backgroundColor = startBackgroundColor,
            actions = startActions
        )
        }

    }
    if (list.isNotEmpty()) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { onRefresh() },
        ) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(paddingBetweenItems.dp),
                contentPadding = PaddingValues(
                    horizontal = paddingHorizontal.dp,
                    vertical = paddingVertical.dp
                )
            ) {


                itemsIndexed(list) { index, item ->
                    SwappableItem(
                        mainItem = { views(item) },
                        startSwipeItem = { startSwipeItem?.invoke() },
                        endSwipeItem = { endSwipeItem?.invoke() },
                        onStartSwiped = {
                            onStartSwiped?.invoke(item, index)
                        },

                        onEndSwiped = {
                            onEndSwiped?.invoke(item, index)
                        },
                    )
                    if (index != list.lastIndex) {
                        Surface(modifier = Modifier.padding(top = paddingBetweenItems.dp)) {
                            dividerView?.invoke()
                        }

                    }


                }



                coroutineScope.launch {
                    listState.animateScrollToItem(scrollTo)
                }

            }
        }


    } else {
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

