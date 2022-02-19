package com.android.magic_recyclerview.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.magic_recyclerview.Constants.ACTION_ICON_SIZE

/***
 * text - a composable text.
 * icon -  a composable icon c.
 * onClicked - calback to handle onClick.
 * actionSize - action size default is 56.
 */
data class Action<T>(
    val text: (@Composable () -> Unit)? = null,
    val icon: (@Composable () -> Unit)? = null,
    val onClicked: ((position: Int, item: T) -> Unit)? = null,
    val actionSize: Dp = ACTION_ICON_SIZE.dp
)