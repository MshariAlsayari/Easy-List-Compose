@file:OptIn(ExperimentalCoilApi::class)

package com.android.magicrecyclerview.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.android.magicrecyclerview.model.Anime



@Composable
fun AnimeCard(anime: Anime) {
    Card(
        modifier= Modifier.height(100.dp).fillMaxWidth(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(0.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Image(
                painter = rememberImagePainter(anime.anime_img),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = anime.anime_name,
                    style = MaterialTheme.typography.h6,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun AnimeGridCard(anime: Anime) {
    Card(
        modifier= Modifier.size(200.dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(0.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Image(
                painter = rememberImagePainter(anime.anime_img),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )


        }
    }
}

