package com.android.magic_recyclerview.component.magic_recyclerview

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Velocity
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
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

/***
 * modifier - the modifier to apply to this layout.
 * list -  list of data.
 * view - the data view holder.
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
    modifier                    : Modifier = Modifier,
    list                        : List<T>,
    view                        : @Composable (T) -> Unit,
    dividerView                 : (@Composable () -> Unit)? = null,
    emptyView                   : (@Composable () -> Unit)? = null,
    loadingProgress             : (@Composable () -> Unit)? = null,
    onItemClicked               : (item: T, position: Int) -> Unit,
    onItemCollapsed             : ((item: T, position: Int) -> Unit)? = null,
    onItemExpanded              : ((item: T, position: Int, type: ActionRowType) -> Unit)? = null,
    startActions                : List<Action<T>> = listOf(),
    endActions                  : List<Action<T>> = listOf(),
    actionBackgroundRadiusCorner: Float = 0f,
    isLoading                   : Boolean = false,
    isRefreshing                : Boolean = false,
    onRefresh                   : (() -> Unit)? = null,
    paddingBetweenItems         : Float = PADDING_BETWEEN_ITEMS,
    paddingVertical             : Float = PADDING_VERTICAL,
    paddingHorizontal           : Float = PADDING_HORIZONTAL,
    scrollTo                    : Int = 0,
) {

    if (startActions.size > 3) {
        throw Exception("the start list action length is > 3 it must be 3 or less")
    }

    if (endActions.size > 3) {
        throw Exception("the end list action length is > 3 it must be 3 or less")
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        swipeEnabled = onRefresh != null,
        onRefresh = { onRefresh?.invoke() },
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {

            if (isLoading)
                LoadingView(loadingProgress)
            else{
                if (list.isNotEmpty()) {
                    LazyList(
                    modifier                    = modifier,
                    list                        = list,
                    view                        = view,
                    dividerView                 = dividerView,
                    startActions                = startActions,
                    endActions                  = endActions,
                    onItemClicked               = onItemClicked,
                    onItemCollapsed             = onItemCollapsed,
                    onItemExpanded              = onItemExpanded,
                    actionBackgroundRadiusCorner= actionBackgroundRadiusCorner,
                    paddingBetweenItems         = paddingBetweenItems,
                    paddingVertical             = paddingVertical,
                    paddingHorizontal           = paddingHorizontal,
                    scrollTo                    = scrollTo,
                    )
                } else {
                    EmptyView(emptyView)
                }
            }


        }
    }



}



@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun <T>LazyList(modifier                    : Modifier,
                list                        : List<T>,
                view                        : @Composable (T) -> Unit,
                dividerView                 : (@Composable () -> Unit)? = null,
                paginationProgress          : (@Composable () -> Unit)? = null,
                startActions                : List<Action<T>> = listOf(),
                endActions                  : List<Action<T>> = listOf(),
                onLastReached               : (() -> Unit)?=null,
                onItemClicked               : (item: T, position: Int) -> Unit,
                onItemCollapsed             : ((item: T, position: Int) -> Unit)? = null,
                onItemExpanded              : ((item: T, position: Int, type: ActionRowType) -> Unit)? = null,
                actionBackgroundRadiusCorner: Float = 0f,
                paddingBetweenItems         : Float = PADDING_BETWEEN_ITEMS,
                paddingVertical             : Float = PADDING_VERTICAL,
                paddingHorizontal           : Float = PADDING_HORIZONTAL,
                scrollTo                    : Int = 0,){


    val listState       = rememberLazyListState()
    val coroutineScope  = rememberCoroutineScope()
    val isRTL           = LocalLayoutDirection.current == LayoutDirection.Rtl
    val isActionClicked = remember { mutableStateOf(false) }



    LazyColumn(
        modifier            = modifier,
        state               = listState,
        verticalArrangement = Arrangement.spacedBy(paddingBetweenItems.dp),
        contentPadding      = PaddingValues(
        horizontal          = paddingHorizontal.dp,
        vertical            = paddingVertical.dp
        )
    ) {


        itemsIndexed(list) {  index , item ->
            Column(modifier = modifier,
                   horizontalAlignment = Alignment.CenterHorizontally,
                   verticalArrangement = Arrangement.Center
            ) {

                ConstraintLayout {
                    val (actionContainer, swappableItemContainer, divider) = createRefs()
                    Row(
                        modifier = Modifier.constrainAs(actionContainer) {
                            top    .linkTo(swappableItemContainer.top)
                            bottom .linkTo(swappableItemContainer.bottom)
                            start  .linkTo(parent.start)
                            end    .linkTo(parent.end)
                            height = Dimension.fillToConstraints
                        },

                        ) {
                        ActionsRow(
                            modifier        = Modifier.weight(1f),
                            item            = item,
                            position        = index,
                            radiusCorner    = actionBackgroundRadiusCorner,
                            actions         = startActions,
                            isActionClicked = {
                                isActionClicked.value = true
                                Handler(Looper.getMainLooper()).postDelayed({
                                    isActionClicked.value = false
                                }, 1000)
                            }
                        )
                        ActionsRow(
                            modifier     = Modifier.weight(1f),
                            item         = item,
                            position     = index,
                            radiusCorner = actionBackgroundRadiusCorner,
                            actions      = endActions,
                            isActionClicked = {
                                isActionClicked.value = true
                                Handler(Looper.getMainLooper()).postDelayed({
                                    isActionClicked.value = false
                                }, 1000)
                            }
                        )

                    }


                    SwappableItem<T>(
                        modifier = modifier.constrainAs(swappableItemContainer) {
                            top   .linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start .linkTo(parent.start)
                            end   .linkTo(parent.end)
                        },
                        item            = item,
                        mainItem        = { view(item) },
                        isActionClicked = isActionClicked.value,
                        onCollapsed     = { item -> onItemCollapsed?.invoke(item, index) },
                        onExpanded      = { type, item -> onItemExpanded?.invoke(item, index, type) },
                        enableLTRSwipe  = if (isRTL) endActions.isNotEmpty() else startActions.isNotEmpty(),
                        enableRTLSwipe  = if (isRTL) startActions.isNotEmpty() else endActions.isNotEmpty(),
                        onItemClicked   = { onItemClicked(it, index) },
                    )

                    if (index != list.lastIndex && dividerView != null) {
                        Surface(
                            modifier = Modifier
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




        }


        coroutineScope.launch {
            listState.animateScrollToItem(scrollTo)
        }


    }

}

fun isLastItemVisible(lazyListState: LazyListState): Boolean {
    val lastItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastItem == null || lastItem.size + lastItem.offset <= lazyListState.layoutInfo.viewportEndOffset
}

/***
 * modifier - the modifier to apply to this layout.
 * list -  list of data.
 * view - the data view holder.
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
    view: @Composable (T) -> Unit,
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
                view(item)
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

    Box(contentAlignment = Alignment.Center) {
        if (isLoading)
            LoadingView(loadingProgress)
        else {
            if (list.isNotEmpty()) {
                lazyRow()
            } else {
                EmptyView(emptyView)
            }
        }
    }


}

/***
 * modifier - the modifier to apply to this layout.
 * list -  list of data.
 * view - the data view holder.
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
    view: @Composable (T) -> Unit,
    emptyView: @Composable (() -> Unit)? = null,
    paddingBetweenItems: Float = PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = PADDING_VERTICAL,
    paddingHorizontal: Float = PADDING_HORIZONTAL,
    columnCount: Int = COLUMN_COUNT,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    isLoading: Boolean = false,
    loadingProgress: (@Composable () -> Unit)? = null,
    onItemClicked               : (item: T, position: Int) -> Unit,
    scrollTo: Int = 0,
) {


            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                swipeEnabled = onRefresh != null,
                onRefresh = { onRefresh?.invoke() },
            ) {
                Box(contentAlignment = Alignment.Center) {

                    if (isLoading)
                        LoadingView(loadingProgress)
                    else{
                        if (list.isNotEmpty()){
                            LazyGridList(
                                modifier = modifier,
                                list = list,
                                view = view,
                                paddingBetweenItems = paddingBetweenItems,
                                paddingVertical = paddingVertical,
                                paddingHorizontal = paddingHorizontal,
                                columnCount = columnCount,
                                onItemClicked = onItemClicked,
                                scrollTo = scrollTo
                            )

                        }else
                            EmptyView(emptyView)
                    }
                }

            }





}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T>LazyGridList(   modifier: Modifier = Modifier,
                       list: List<T>,
                       view: @Composable (T) -> Unit,
                       paddingBetweenItems: Float = PADDING_BETWEEN_ITEMS,
                       paddingVertical: Float = PADDING_VERTICAL,
                       paddingHorizontal: Float = PADDING_HORIZONTAL,
                       columnCount: Int = COLUMN_COUNT,
                       onItemClicked               : (item: T, position: Int) -> Unit,
                       scrollTo: Int = 0,){
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
        itemsIndexed(list) { index, item ->
            Column(modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(modifier = modifier.clickable {
                    onItemClicked(item, index)
                }, contentAlignment = Alignment.Center,
                ) {
                    view(item)
                }

            }




        }

        coroutineScope.launch {
            listState.animateScrollToItem(scrollTo)
        }
    }

}



@Composable
fun PaginationLoadingView(view: (@Composable () -> Unit)?=null) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (view != null ){
            view()
        }else{
            CircularProgressIndicator(modifier = Modifier
                .padding(vertical = 20.dp)
                .size(20.dp), strokeWidth = 2.dp)
        }
    }
}

@Composable
fun LoadingView(view: (@Composable () -> Unit)?=null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (view != null ){
            view()
        }else{
            CircularProgressIndicator()
        }
    }
}

@Composable
fun EmptyView(view: (@Composable () -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        view?.invoke()
    }
}

