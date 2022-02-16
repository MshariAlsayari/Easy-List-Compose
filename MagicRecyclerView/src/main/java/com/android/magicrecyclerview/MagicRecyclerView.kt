package com.android.magicrecyclerview


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.android.magicrecyclerview.model.Action
import com.android.magicrecyclerview.ui.component.magic_recyclerview.GridRecyclerView
import com.android.magicrecyclerview.ui.component.magic_recyclerview.HorizontalRecyclerView
import com.android.magicrecyclerview.ui.component.magic_recyclerview.VerticalRecyclerView

@ExperimentalMaterialApi
@Composable
fun <T> VerticalMagicRecyclerView(
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
    actionBackgroundHeight: Dp = Dp(Constants.ACTION_HEIGHT),
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    paddingBetweenItems: Float = Constants.PADDING_BETWEEN_ITEMS,
    paddingVertical: Float = Constants.PADDING_VERTICAL,
    paddingHorizontal: Float = Constants.PADDING_HORIZONTAL,
    scrollTo: Int = 0,
) {
    VerticalRecyclerView(
        modifier,
        list,
        views,
        dividerView,
        emptyView,
        startActions,
        endActions,
        startActionBackgroundColor,
        endActionBackgroundColor,
        actionBackgroundRadiusCorner,
        actionBackgroundHeight,
        isRefreshing,
        onRefresh,
        paddingBetweenItems,
        paddingVertical,
        paddingHorizontal,
        scrollTo,
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




