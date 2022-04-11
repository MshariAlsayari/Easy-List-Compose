package com.android.magic_recyclerview.component.action_row

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.magic_recyclerview.Constants
import com.android.magic_recyclerview.model.Action

@Composable
fun <T> ActionsRow(
    modifier: Modifier,
    item: T,
    position: Int,
    actions: List<Action<T>>,
    radiusCorner: Float = 0f,
    actionHeight: Float,
    isActionClicked: (() -> Unit)? = null
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(radiusCorner.dp),
    ) {

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {


            actions.forEach {
                ActionItem(
                    modifier = Modifier
                        .weight(1f, true)
                        .background(it.backgroundColor)
                        .height (actionHeight.dp)
                    . size (it.actionSize),
                action = it,
                onClicked = { position, item ->
                    isActionClicked?.invoke()
                    Handler(Looper.getMainLooper()).postDelayed({
                        it.onClicked?.invoke(
                            position,
                            item
                        )
                    }, Constants.SWIPE_ANIMATION_DURATION.toLong())


                },
                item = item,
                position = position
                )
            }


        }

    }

}
