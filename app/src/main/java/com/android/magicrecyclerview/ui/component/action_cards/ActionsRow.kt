package com.android.magicrecyclerview.ui.component.action_cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.android.magicrecyclerview.model.Action

@Composable
fun ActionsRow(
    modifier: Modifier,
    actions: List<Action>,
    radiusCorner: Dp = Dp(0f),
    backgroundColor: Color,
    onFirstActionClicked: () -> Unit = {},
    onSecondActionClicked: () -> Unit = {},
    onThirdActionClicked: () -> Unit = {},
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
                        0 -> ActionCompos(
                            action = actions[i],
                            onClicked = { onFirstActionClicked() })
                        1 -> ActionCompos(
                            action = actions[i],
                            onClicked = { onSecondActionClicked() })
                        2 -> ActionCompos(
                            action = actions[i],
                            onClicked = { onThirdActionClicked() })

                    }

                }
            }


        }

    }

}
