package com.android.magicrecyclerview.ui.component.action_cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.magicrecyclerview.model.Action


@Composable
fun StartActionCard(modifier: Modifier, backgroundColor:Color, actions: List<Action>) {
    Card(modifier = modifier, backgroundColor = backgroundColor) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {

            actions.forEach {
                ActionCompos(it)
            }

        }
    }
}

@Composable
fun EndActionCard(modifier: Modifier, backgroundColor:Color, actions: List<Action>) {
    Card(modifier = modifier, backgroundColor = backgroundColor) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {

            actions.forEach {
                ActionCompos(it)
            }

        }
    }
}



@Composable
fun ActionCompos(action: Action) {


    Column(
        modifier = Modifier.clickable { action.onClicked()}
    ) {

       Image(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            painter = painterResource(id = action.icon),
            contentDescription = "Delete"
        )



        Text(
            modifier = Modifier.padding(16.dp, 0.dp),
            text = action.text ?: "",
            style = MaterialTheme.typography.h6,
            color = Color.White
        )

    }
}