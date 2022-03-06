package com.android.magic_recyclerview.component.action_row

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
    backgroundColor: Color,
    isActionClicked: (() -> Unit)? = null
) {

    Card(
        modifier = modifier,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(radiusCorner.dp),
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {


            for (i in 0..2) {
                if (actions.getOrNull(i) != null) {
                    ActionItem(
                        action = actions[i],
                        onClicked = { position, item ->
                            isActionClicked?.invoke()
                            Handler(Looper.getMainLooper()).postDelayed({
                                actions[i].onClicked?.invoke(
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

}
