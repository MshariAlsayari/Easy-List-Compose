package com.android.magicrecyclerview.model

import androidx.annotation.DrawableRes

data class Action(val text: String?, @DrawableRes val icon: Int, val onClicked: ()->Unit = {})