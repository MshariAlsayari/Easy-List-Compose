package com.android.magicrecyclerview.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.magicrecyclerview.Constants.ACTION_ICON_SIZE

data class Action(
    val text: (@Composable () -> Unit)? = null,
    val icon: (@Composable () -> Unit)? = null,
    val actionSize: Dp = ACTION_ICON_SIZE.dp
)