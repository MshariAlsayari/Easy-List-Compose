package com.android.magicrecyclerview


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import com.android.magicrecyclerview.ui.component.magic_recyclerview.GridRecyclerView
import com.android.magicrecyclerview.ui.component.magic_recyclerview.HorizontalRecyclerView
import com.android.magicrecyclerview.ui.component.magic_recyclerview.VerticalRecyclerView
import com.android.magicrecyclerview.ui.component.swippable_item.SwipeDirection

@Composable
fun <T> VerticalMagicRecyclerView(
    list: List<T>,
    views: @Composable LazyItemScope.(item: T) -> Unit,
    dividerView: (@Composable () -> Unit)? = null,
    emptyView: (@Composable () -> Unit)? = null,
    startSwipeItem: (@Composable () -> Unit)? = null,
    endSwipeItem: (@Composable () -> Unit)? = null,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    paddingBetweenItems: Float = Constants.PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = Constants.PADDING_VERTICAL,
    paddingHorizontal: Float = Constants.PADDING_HORIZONTAL,
    scrollTo: Int = 0,
    onSwiped: ((item: T, position: Int) -> Unit)? = null,
    swipeDirection: SwipeDirection = SwipeDirection.NON,
) {
    VerticalRecyclerView(
        list,
        views,
        dividerView,
        emptyView,
        startSwipeItem,
        endSwipeItem,
        isRefreshing,
        onRefresh,
        paddingBetweenItems,
        paddingVertical,
        paddingHorizontal,
        scrollTo,
        onSwiped,
        swipeDirection
    )
}

@Composable
fun <T> HorizontalMagicRecyclerView(
    list: List<T>,
    views: @Composable LazyItemScope.(item: T) -> Unit,
    emptyView: (@Composable () -> Unit)? = null,
    dividerView: (@Composable () -> Unit)? = null,
    paddingBetweenItems: Float = Constants.PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = Constants.PADDING_VERTICAL,
    paddingHorizontal: Float = Constants.PADDING_HORIZONTAL,
    scrollTo: Int = 0,
) {

    HorizontalRecyclerView(
        list,
        views,
        emptyView,
        dividerView,
        paddingBetweenItems,
        paddingVertical,
        paddingHorizontal,
        scrollTo,
    )

}

@ExperimentalFoundationApi
@Composable
fun <T> GridMagicRecyclerView(
    list: List<T>,
    views: @Composable (LazyItemScope.(item: T) -> Unit),
    emptyView: @Composable (() -> Unit)? = null,
    paddingBetweenItems: Float = Constants.PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = Constants.PADDING_VERTICAL,
    paddingHorizontal: Float = Constants.PADDING_HORIZONTAL,
    columnCount: Int = Constants.COLUMN_COUNT,
) {

    GridRecyclerView(
        list,
        views,
        emptyView,
        paddingBetweenItems,
        paddingVertical,
        paddingHorizontal,
        columnCount,
    )


}




