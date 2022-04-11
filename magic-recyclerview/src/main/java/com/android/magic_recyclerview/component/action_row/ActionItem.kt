package com.android.magic_recyclerview.component.action_row

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.android.magic_recyclerview.model.Action


@Composable
fun <T> ActionItem(
    modifier: Modifier,
    action: Action<T>,
    onClicked: (position: Int, item: T) -> Unit,
    item: T,
    position: Int
) {

    IconButton(
        modifier = modifier,
        onClick = {
            onClicked(position, item)
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