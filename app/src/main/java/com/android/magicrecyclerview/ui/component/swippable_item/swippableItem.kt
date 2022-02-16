package com.android.magicrecyclerview.ui.component.magic_recyclerview


import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.android.magicrecyclerview.Constants
import com.android.magicrecyclerview.ui.component.swippable_item.SwipeDirection
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun SwappableItem(
    mainItem: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enableLTRSwipe: Boolean = false,
    enableRTLSwipe: Boolean = false,
    animationSpec: AnimationSpec<Float> = tween(Constants.SWIPE_ANIMATION_DURATION),
    thresholds: (from: SwipeDirection, to: SwipeDirection) -> ThresholdConfig = { _, _ ->
        FractionalThreshold(
            0.6f
        )
    },
    velocityThreshold: Dp = Constants.VELOCITY.dp
) {

    ConstraintLayout(modifier = modifier) {
        val (mainCardRef, actionCardRef) = createRefs()
        val swappableState = rememberSwipeableState(
            initialValue = SwipeDirection.NON,
            animationSpec = animationSpec
        )
        val coroutineScope = rememberCoroutineScope()

        /* Tracks if swipeItem action to be shown */


        val isArabic = LocalLayoutDirection.current == LayoutDirection.Rtl
        /* Disable swipe when card is animating back to default position */
        val swipeEnabled = remember { mutableStateOf(true) }

        val maxWidthInPx = with(LocalDensity.current) {
            LocalConfiguration.current.screenWidthDp.dp.toPx()
        }

        val middleSwipeItem = remember { mutableStateOf(0) }

        val anchors = hashMapOf(0f to SwipeDirection.NON)
        anchors[maxWidthInPx] = SwipeDirection.LEFT_TO_RIGHT
        anchors[-maxWidthInPx] = SwipeDirection.RIGHT_TO_LEFT


        /* This surface is for action card which is below the main card */


        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    middleSwipeItem.value = coordinates.size.width / 2
                }
                .offset {
                    var offset = swappableState.offset.value.roundToInt()
                    Log.i("Mshari first", offset.toString())
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
                    Log.i("Mshari second", offset.toString())


                    if (isArabic) {
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
                .constrainAs(mainCardRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {


            mainItem()
        }
    }
}




