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
import androidx.constraintlayout.compose.Dimension
import com.android.magicrecyclerview.Constants
import com.android.magicrecyclerview.ui.component.swippable_item.SwipeDirection
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun SwappableItem(
    mainItem: @Composable () -> Unit,
    startSwipeItem: (@Composable () -> Unit)? = null,
    endSwipeItem: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    onStartSwiped: () -> Unit = {},
    onEndSwiped: () -> Unit = {},
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
        val swipeItemVisible = remember { mutableStateOf(false) }

        val isArabic =  LocalLayoutDirection.current == LayoutDirection.Rtl
        /* Disable swipe when card is animating back to default position */
        val swipeEnabled = remember { mutableStateOf(true) }

        val maxWidthInPx = with(LocalDensity.current) {
            LocalConfiguration.current.screenWidthDp.dp.toPx()
        }

        val middleSwipeItem = remember { mutableStateOf(0) }

        val anchors = hashMapOf(0f to SwipeDirection.NON)
        if (endSwipeItem != null) {
            anchors[maxWidthInPx] = SwipeDirection.LEFT_TO_RIGHT
        }
        if (startSwipeItem != null) {
            anchors[-maxWidthInPx] = SwipeDirection.RIGHT_TO_LEFT

        }


        /* This surface is for action card which is below the main card */
        Surface(
            color = Color.Transparent,
            content = {
                if (swipeItemVisible.value) {
                    startSwipeItem?.invoke()
                } else {
                    endSwipeItem?.invoke()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    middleSwipeItem.value = coordinates.size.width / 2
                }
                .constrainAs(actionCardRef) {
                    top.linkTo(mainCardRef.top)
                    bottom.linkTo(mainCardRef.bottom)
                    height = Dimension.fillToConstraints
                }
        )

        Surface(
            color = Color.Transparent,
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    var offset = swappableState.offset.value.roundToInt()
                    if (offset < 0 && startSwipeItem == null) offset = 0
                    if (offset > 0 && endSwipeItem == null) offset = 0
                    Log.i("Mshari first", offset.toString())
                    if (offset < 0){
                        if(offset*-1 >= middleSwipeItem.value)
                            offset = -middleSwipeItem.value
                    }

                    if (offset > 0){
                        if(offset >= middleSwipeItem.value)
                            offset = middleSwipeItem.value
                    }
                   Log.i("Mshari second", offset.toString())


                    if (isArabic){
                        IntOffset(-offset, 0)
                    }else{
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

//            if (swappableState.currentValue == SwipeDirection.LEFT_TO_RIGHT && !swappableState.isAnimationRunning) {
//                if (isArabic){
//                    onEndSwiped()
//                }else{
//                    onStartSwiped()
//                }
//
//                   swipeItemVisible.value = false
//                coroutineScope.launch {
//                    swipeEnabled.value = false
//                    swappableState.animateTo(SwipeDirection.LEFT_TO_RIGHT)
//                    swipeEnabled.value = true
//                }
//            } else if (swappableState.currentValue == SwipeDirection.RIGHT_TO_LEFT && !swappableState.isAnimationRunning) {
//                if (isArabic){
//                    onStartSwiped()
//                }else{
//                    onEndSwiped()
//                }
//                 swipeItemVisible.value = false
//                coroutineScope.launch {
//                    swipeEnabled.value = false
//                    swappableState.animateTo(SwipeDirection.RIGHT_TO_LEFT)
//                    swipeEnabled.value = true
//                }
//            }

            swipeItemVisible.value = swappableState.offset.value <= 0





            mainItem()
        }
    }
}




