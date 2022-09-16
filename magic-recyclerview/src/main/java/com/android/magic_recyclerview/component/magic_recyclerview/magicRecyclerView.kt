package com.android.magic_recyclerview.component.magic_recyclerview

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.android.magic_recyclerview.Constants.COLUMN_COUNT
import com.android.magic_recyclerview.Constants.PADDING_BETWEEN_ITEMS
import com.android.magic_recyclerview.Constants.PADDING_HORIZONTAL
import com.android.magic_recyclerview.Constants.PADDING_VERTICAL
import com.android.magic_recyclerview.component.action_row.ActionRowType
import com.android.magic_recyclerview.component.action_row.ActionsRow
import com.android.magic_recyclerview.component.swippable_item.SwappableItem
import com.android.magic_recyclerview.model.Action
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

/***
 * modifier - the modifier to apply to this layout.
 * list -  list of data.
 * views - the data view holder.
 * onItemClicked - callback when the swappable item's been clicked
 * onItemCollapsed - callback when the swappable item's been collapsed
 * onItemExpanded - callback when the swappable item's been expanded
 * dividerView - (optional) divider between items.
 * emptyView - (optional) emptyview if the list is empty.
 * startActions - list of actions if it is empty no swipe .
 * endActions - list of actions if it is empty no swipe .
 * startActionBackgroundColor - background color of the list of the start actions.
 * endActionBackgroundColor - background color of the list of the end actions.
 * actionBackgroundRadiusCorner - radius corner for both start background and end background actions.
 * actionBackgroundHeight - height of the actions background.
 * isLoading - show loading content progress.
 * loadingProgress - (optional) if null will show CircularProgressIndicator().
 * isRefreshing - show progress of the swipeRefreshLayout.
 * onRefresh - (optional) callback when the swipeRefreshLayout swapped if null the list will wrapped without the swipeRefreshLayout .
 * paddingBetweenItems - padding between items default is 8f.
 * paddingVertical - padding on top and bottom of the whole list default is 0.
 * paddingHorizontal - padding on left and right of the whole list default is 0.
 * scrollTo - scroll to item default is 0.
 */

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun <T> VerticalEasyList(
    modifier: Modifier = Modifier,
    list: List<T>,
    views: @Composable LazyItemScope.(item: T) -> Unit,
    onItemClicked: (item: T, position: Int) -> Unit,
    onItemCollapsed: ((item: T, position: Int) -> Unit)? = null,
    onItemExpanded: ((item: T, position: Int, type: ActionRowType) -> Unit)? = null,
    dividerView: (@Composable () -> Unit)? = null,
    emptyView: (@Composable () -> Unit)? = null,
    startActions: List<Action<T>> = listOf(),
    endActions: List<Action<T>> = listOf(),
    actionBackgroundRadiusCorner: Float = 0f,
    isLoading: Boolean = false,
    loadingProgress: (@Composable () -> Unit)? = null,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    paddingBetweenItems: Float = PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = PADDING_VERTICAL,
    paddingHorizontal: Float = PADDING_HORIZONTAL,
    scrollTo: Int = 0,
) {

    if (startActions.size > 3) {
        throw Exception("the start list action length is > 3 it must be 3 or less")
    }

    if (endActions.size > 3) {
        throw Exception("the end list action length is > 3 it must be 3 or less")
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isArabic = LocalLayoutDirection.current == LayoutDirection.Rtl
    val isActionClicked = remember { mutableStateOf(false) }
    val progress: (@Composable () -> Unit) = loadingProgress ?: { CircularProgressIndicator() }
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


                ConstraintLayout {
                    val (actionContainer, swappableItemContainer, divider) = createRefs()
                    Row(
                        modifier = Modifier.constrainAs(actionContainer) {
                            top.linkTo(swappableItemContainer.top)
                            bottom.linkTo(swappableItemContainer.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints


                        },

                        ) {
                        ActionsRow(
                            modifier = Modifier
                                .weight(1f),

                            item = item,
                            position = index,
                            radiusCorner = actionBackgroundRadiusCorner,
                            actions = startActions,
                            isActionClicked = {
                                isActionClicked.value = true
                                Handler(Looper.getMainLooper()).postDelayed({
                                    isActionClicked.value = false
                                }, 1000)
                            }
                        )
                        ActionsRow(
                            modifier = Modifier
                                .weight(1f),

                            item = item,
                            position = index,
                            radiusCorner = actionBackgroundRadiusCorner,
                            actions = endActions,
                            isActionClicked = {
                                isActionClicked.value = true
                                Handler(Looper.getMainLooper()).postDelayed({
                                    isActionClicked.value = false
                                }, 1000)
                            }
                        )

                    }


                    SwappableItem(
                        modifier = modifier.constrainAs(swappableItemContainer) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)


                        },
                        item = item,
                        mainItem = { views(item) },
                        isActionClicked = isActionClicked.value,
                        onCollapsed = { item ->
                            onItemCollapsed?.invoke(item, index)
                        },
                        onExpanded = { type, item ->
                            onItemExpanded?.invoke(item, index, type)
                        },
                        enableLTRSwipe = if (isArabic) endActions.isNotEmpty() else startActions.isNotEmpty(),
                        enableRTLSwipe = if (isArabic) startActions.isNotEmpty() else endActions.isNotEmpty(),
                        onItemClicked = {
                            onItemClicked(it, index)
                        },
                    )

                    if (index != list.lastIndex && dividerView != null) {
                        Surface(modifier = Modifier
                            .padding(top = paddingBetweenItems.dp)
                            .constrainAs(divider) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(swappableItemContainer.bottom)
                            }
                        ) {
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


            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                swipeEnabled = onRefresh != null,
                onRefresh = {
                    if (onRefresh != null) {
                        onRefresh()
                    }
                },
            ) {

                Box(
                    contentAlignment = Alignment.Center) {

                    if (list.isEmpty()) {
                        EmptyView(emptyView)
                    } else {
                        lacyColumn()

                }



                    if (isLoading)
                        progress()
                }
            }





}

/***
 * modifier - the modifier to apply to this layout.
 * list -  list of data.
 * views - the data view holder.
 * dividerView - (optional) divider between items.
 * emptyView - (optional) emptyview if the list is empty.
 * paddingBetweenItems - padding between items default is 8f.
 * paddingVertical - padding on top and bottom of the whole list default is 0.
 * paddingHorizontal - padding on left and right of the whole list default is 0.
 * isLoading - show loading content progress.
 * loadingProgress - (optional) if null will show CircularProgressIndicator().
 * scrollTo - scroll to item default is 0.
 */
@Composable
fun <T> HorizontalEasyList(
    modifier: Modifier = Modifier,
    list: List<T>,
    views: @Composable LazyItemScope.(item: T) -> Unit,
    emptyView: (@Composable () -> Unit)? = null,
    dividerView: (@Composable () -> Unit)? = null,
    paddingBetweenItems: Float = PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = PADDING_VERTICAL,
    paddingHorizontal: Float = PADDING_HORIZONTAL,
    isLoading: Boolean = false,
    loadingProgress: (@Composable () -> Unit)? = null,
    scrollTo: Int = 0,
) {

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val lazyRow: @Composable () -> Unit = {
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

    }
    val progress: (@Composable () -> Unit) = loadingProgress ?: { CircularProgressIndicator() }

    if (list.isNotEmpty()) {

        Box(contentAlignment = Alignment.Center) {
            lazyRow()
            if (isLoading)
                progress()
        }


    } else {
        EmptyView(emptyView)

    }

}

/***
 * modifier - the modifier to apply to this layout.
 * list -  list of data.
 * views - the data view holder.
 * dividerView - (optional) divider between items.
 * emptyView - (optional) emptyview if the list is empty.
 * actionBackgroundHeight - height of the actions background.
 * isRefreshing - show progress of the swipeRefreshLayout.
 * onRefresh - (optional) callback when the swipeRefreshLayout swapped if null the list will wrapped without the swipeRefreshLayout .
 * paddingBetweenItems - padding between items default is 8f.
 * paddingVertical - padding on top and bottom of the whole list default is 0.
 * paddingHorizontal - padding on left and right of the whole list default is 0.
 * scrollTo - scroll to item default is 0.
 * isLoading - show loading content progress.
 * loadingProgress - (optional) if null will show CircularProgressIndicator().
 * columnCount - number of columns default is 2
 */
@ExperimentalFoundationApi
@Composable
fun <T> GridEasyList(
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
    isLoading: Boolean = false,
    loadingProgress: (@Composable () -> Unit)? = null,
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

            coroutineScope.launch {
                listState.animateScrollToItem(scrollTo)
            }
        }
    }

    val progress: (@Composable () -> Unit) = loadingProgress ?: { CircularProgressIndicator() }

    if (list.isNotEmpty()) {

        if (onRefresh == null) {
            Box(contentAlignment = Alignment.Center) {
                gridList()
                if (isLoading)
                    progress()
            }
        } else {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { onRefresh() },
            ) {
                Box(contentAlignment = Alignment.Center) {
                    gridList()
                    if (isLoading)
                        progress()
                }

            }
        }


    } else {
        EmptyView(emptyView)

    }

}

@Composable
fun EmptyView(view: (@Composable () -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        view?.invoke()
    }
}

