package com.android.magicrecyclerview.ui.component.magic_recyclerview


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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
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
    swipeItem: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onSwiped: () -> Unit = {},
    swipeDirection: SwipeDirection = SwipeDirection.RIGHT_TO_LEFT,
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
        val swipeItemVisible = remember { mutableStateOf(true) }

        /* Disable swipe when card is animating back to default position */
        val swipeEnabled = remember { mutableStateOf(true) }

        val maxWidthInPx = with(LocalDensity.current) {
            LocalConfiguration.current.screenWidthDp.dp.toPx()
        }

        val anchors = hashMapOf(0f to SwipeDirection.NON)
        if (swipeDirection == SwipeDirection.LEFT_TO_RIGHT) {
            anchors[maxWidthInPx] = SwipeDirection.LEFT_TO_RIGHT
        }
        if (swipeDirection == SwipeDirection.RIGHT_TO_LEFT) {
            anchors[-maxWidthInPx] = SwipeDirection.RIGHT_TO_LEFT

        }


        /* This surface is for action card which is below the main card */
        Surface(
            color = Color.Transparent,
            content = {
                if (swipeItemVisible.value) {
                    swipeItem()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
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
                    val offset = swappableState.offset.value.roundToInt()
                    if (swipeDirection == SwipeDirection.LEFT_TO_RIGHT && offset > 0)
                        IntOffset(offset, 0)
                    else if (swipeDirection == SwipeDirection.RIGHT_TO_LEFT && offset < 0)
                        IntOffset(offset, 0)
                    else IntOffset(0, 0)
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

            if (swappableState.currentValue == SwipeDirection.LEFT_TO_RIGHT && !swappableState.isAnimationRunning) {
                onSwiped()
                //   swipeItemVisible.value = false
                coroutineScope.launch {
                    swipeEnabled.value = false
                    swappableState.animateTo(SwipeDirection.NON)
                    swipeEnabled.value = true
                }
            } else if (swappableState.currentValue == SwipeDirection.RIGHT_TO_LEFT && !swappableState.isAnimationRunning) {
                onSwiped()
                // swipeItemVisible.value = false
                coroutineScope.launch {
                    swipeEnabled.value = false
                    swappableState.animateTo(SwipeDirection.NON)
                    swipeEnabled.value = true
                }
            }



            mainItem()
        }
    }


}
