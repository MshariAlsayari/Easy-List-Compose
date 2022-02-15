package com.android.magicrecyclerview.ui.component.magic_recyclerview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.magicrecyclerview.model.Item
import com.android.magicrecyclerview.ui.theme.MagicRecyclerViewTheme


@Composable
fun magicRecyclerViewItem(item: Item) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.Blue,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = item.name, style = typography.h6, color = Color.White)
                Text(text = item.description, style = typography.caption, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemViewPreview() {
    MagicRecyclerViewTheme {
        magicRecyclerViewItem(Item(id = 1, name = "Mshari", description = "Android developer"))
    }
}