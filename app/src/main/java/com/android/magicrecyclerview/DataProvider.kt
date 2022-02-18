package com.android.magicrecyclerview

import com.android.magicrecyclerview.model.Anime

object DataProvider {

    val emptyLis = listOf<Anime>()

    val itemList = listOf(
        Anime(
            anime_id = 1,
            anime_name = "Bleach",
            anime_img = "https://m.media-amazon.com/images/M/MV5BZjE0YjVjODQtZGY2NS00MDcyLThhMDAtZGQwMTZiOWNmNjRiXkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_FMjpg_UX1000_.jpg",

            ),
        Anime(
            anime_id = 2,
            anime_name = "Black Clover",
            anime_img = "https://m.media-amazon.com/images/M/MV5BNTAzYTlkMWEtOTNjZC00ZDU0LWI5ODUtYTRmYzY0MTAzYWZlXkEyXkFqcGdeQXVyMzgxODM4NjM@._V1_FMjpg_UX1000_.jpg",

            ),
        Anime(
            anime_id = 3,
            anime_name = "Dragon Ball",
            anime_img = "https://m.media-amazon.com/images/M/MV5BMGMyOThiMGUtYmFmZi00YWM0LWJiM2QtZGMwM2Q2ODE4MzhhXkEyXkFqcGdeQXVyMjc2Nzg5OTQ@._V1_FMjpg_UX1000_.jpg",

            ),
        Anime(
            anime_id = 5,
            anime_name = "Naruto",
            anime_img = "https://m.media-amazon.com/images/M/MV5BZmQ5NGFiNWEtMmMyMC00MDdiLTg4YjktOGY5Yzc2MDUxMTE1XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_.jpg",

            )
    )
}