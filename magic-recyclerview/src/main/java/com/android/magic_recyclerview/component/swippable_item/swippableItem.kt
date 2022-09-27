package com.android.magic_recyclerview.component.swippable_item


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.offset
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.android.magic_recyclerview.Constants
import com.android.magic_recyclerview.component.action_row.ActionRowType
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun <T> SwappableItem(
    modifier: Modifier = Modifier,
    item: T,
    mainItem: @Composable () -> Unit,
    enableLTRSwipe: Boolean = false,
    enableRTLSwipe: Boolean = false,
    isActionClicked: Boolean = false,
    onItemClicked: (item: T) -> Unit,
    animationSpec: AnimationSpec<Float> = tween(Constants.SWIPE_ANIMATION_DURATION),
    thresholds: (from: SwipeDirection, to: SwipeDirection) -> ThresholdConfig = { _, _ ->
        FractionalThreshold(
            0.6f
        )
    },
    velocityThreshold: Dp = Constants.VELOCITY.dp,
    onCollapsed: (item: T) -> Unit,
    onExpanded: (type: ActionRowType, item: T) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val swappableState = rememberSwipeableState(
        initialValue = SwipeDirection.NON,
        animationSpec = animationSpec
    )

    val isRTL = LocalLayoutDirection.current == LayoutDirection.Rtl
    val swipeEnabled = remember { mutableStateOf(true) }
    val wasExtendState = remember { mutableStateOf(false) }
    val swappableItemOffset = remember { mutableStateOf(0) }
    val isActionClickedState = remember { mutableStateOf(false) }
    isActionClickedState.value = isActionClicked
    val maxWidthInPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }

    val middleSwipeItem = remember { mutableStateOf(0) }
    val anchors = hashMapOf(0f to SwipeDirection.NON)
    anchors[maxWidthInPx] = SwipeDirection.LEFT_TO_RIGHT
    anchors[-maxWidthInPx] = SwipeDirection.RIGHT_TO_LEFT


    coroutineScope.launch {
        if (isActionClickedState.value) {
            swappableState.animateTo(SwipeDirection.NON)
        }
    }

    Surface(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                middleSwipeItem.value = coordinates.size.width / 2
            }
            .offset {
                var offset = swappableState.offset.value.roundToInt()
                if (offset < 0) {
                    if (enableRTLSwipe) {
                        if (offset * -1 >= middleSwipeItem.value)
                            offset = -middleSwipeItem.value
                    } else {
                        offset = 0
                    }

                }

                if (offset > 0) {
                    if (enableLTRSwipe) {
                        if (offset >= middleSwipeItem.value)
                            offset = middleSwipeItem.value
                    } else {
                        offset = 0
                    }

                }

                swappableItemOffset.value = offset
                if (isRTL) {
                    IntOffset(-offset, 0)
                } else {
                    IntOffset(offset, 0)
                }


            }
            .swipeable(
                state = swappableState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
                enabled = swipeEnabled.value,
                thresholds = thresholds,
                velocityThreshold = velocityThreshold
            )
            .clickable {
                coroutineScope.launch {
                    if (swappableState.currentValue == SwipeDirection.NON ){
                        onItemClicked(item)
                    }
                    swappableState.animateTo(SwipeDirection.NON)

                }
            }
    ) {

        if ((swappableState.currentValue == SwipeDirection.LEFT_TO_RIGHT || swappableState.currentValue == SwipeDirection.RIGHT_TO_LEFT) && !swappableState.isAnimationRunning && swappableItemOffset.value.absoluteValue == middleSwipeItem.value) {

            if (swappableState.currentValue == SwipeDirection.LEFT_TO_RIGHT) {

                if (isRTL) {
                    onExpanded.invoke(ActionRowType.END, item)
                } else {
                    onExpanded.invoke(ActionRowType.START, item)
                }

            }

            if (swappableState.currentValue == SwipeDirection.RIGHT_TO_LEFT) {
                if (isRTL) {
                    onExpanded.invoke(ActionRowType.START, item)
                } else {
                    onExpanded.invoke(ActionRowType.END, item)
                }
            }



            wasExtendState.value = true
        }

        if (swappableState.currentValue == SwipeDirection.NON && !swappableState.isAnimationRunning && swappableItemOffset.value.absoluteValue == 0 && !isActionClickedState.value && wasExtendState.value) {
            onCollapsed.invoke(item)
            wasExtendState.value = false
        }
        mainItem()
    }

}




