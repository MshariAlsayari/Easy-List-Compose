package com.android.magic_recyclerview.component.action_row

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.android.magic_recyclerview.model.Action

@Composable
fun <T> ActionsRow(
    modifier: Modifier,
    item: T,
    position: Int,
    actions: List<Action<T>>,
    radiusCorner: Dp = Dp(0f),
    backgroundColor: Color,
    isActionClicked: (() -> Unit)? = null
) {

    Card(
        modifier = modifier,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(radiusCorner),
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {


            for (i in 0..2) {
                if (actions.getOrNull(i) != null) {

                    when (i) {
                        0 -> ActionItem(
                            action = actions[i],
                            onClicked = { position, item ->
                                isActionClicked?.invoke()
                                actions[i].onClicked?.invoke(
                                    position,
                                    item
                                )


                            },
                            item = item,
                            position = position
                        )
                        1 -> ActionItem(
                            action = actions[i],
                            onClicked = { position, item ->
                                isActionClicked?.invoke()
                                actions[i].onClicked?.invoke(
                                    position,
                                    item
                                )
                            },
                            item = item,
                            position = position
                        )
                        2 -> ActionItem(
                            action = actions[i],
                            onClicked = { position, item ->
                                isActionClicked?.invoke()
                                actions[i].onClicked?.invoke(
                                    position,
                                    item
                                )
                            },
                            item = item,
                            position = position
                        )

                    }

                }
            }


        }

    }

}
