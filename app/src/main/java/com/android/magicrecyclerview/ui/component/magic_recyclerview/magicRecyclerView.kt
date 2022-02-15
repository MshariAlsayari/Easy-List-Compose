package com.android.magicrecyclerview.ui.component.magic_recyclerview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.magicrecyclerview.Constants.COLUMN_COUNT
import com.android.magicrecyclerview.Constants.PADDING_BETWEEN_ITEMS
import com.android.magicrecyclerview.Constants.PADDING_HORIZONTAL
import com.android.magicrecyclerview.Constants.PADDING_VERTICAL
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
    swipeItem: (@Composable () -> Unit)? = null,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    paddingBetweenItems: Float = PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = PADDING_VERTICAL,
    paddingHorizontal: Float = PADDING_HORIZONTAL,
    scrollTo: Int = 0,
    onSwiped: ((item: T, position: Int) -> Unit)? = null,
    swipeDirection: SwipeDirection = SwipeDirection.NON,
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
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
                        swipeItem = { swipeItem?.invoke() },
                        swipeDirection = swipeDirection,
                        onSwiped = {
                            onSwiped?.invoke(item, index)
                        })
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

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun MagicRecyclerViewPreview() {

}