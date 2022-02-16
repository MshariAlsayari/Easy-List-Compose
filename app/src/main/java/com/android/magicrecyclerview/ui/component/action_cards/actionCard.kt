package com.android.magicrecyclerview.ui.component.action_cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.magicrecyclerview.model.Action


@Composable
fun ActionCompos(action: Action, onClicked: () -> Unit) {

    IconButton(
        modifier = Modifier.size(action.actionSize),
        onClick = {
            onClicked()
        },
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                action.icon?.invoke()
                action.text?.invoke()

            }
        }
    )


}