# Magic-RecyclerView-Compose


## Table of contents
- [Introduction](#introduction)
- [Setup](#setup)
- [Examples](#examples)
   - [Vertical](#vertical)
   - [Horizontal](#horizontal)
   - [Grid](#grid)
- [Versions](#versions)


## Introduction
This is an Android Library that's implemented in compose to help you to build a RecyclerView with some features.

## Setup
#### Step1: settings.gradle

```
   repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
```

#### Step2: add the dependency 

```
 dependencies {
     ...
    implementation 'com.github.MshariAlsayari:Magic-RecyclerView-Compose:0.1.1'    
 }
```


## Examples
There are three types of a Recyclerview ***Vertical, Horizontal and Grid.*** and we need to know we have Actions


```
data class Action<T>(
    val text: (@Composable () -> Unit)? = null,
    val icon: (@Composable () -> Unit)? = null,
    val onClicked: ((position: Int, item: T) -> Unit)? = null,
    val actionSize: Dp = ACTION_ICON_SIZE.dp
)
```

Before providing examples let's say we have model, data and composeable view for swippable card, action text and action icone:

Model
```
data class Item(
    val id: Int,
    val name: String,
    val description: String
)
```

Data
```
   val itemList = listOf(
        Item(
            id = 1,
            name = "Eman",
            description = "Android Developer.",

            ),
        Item(
            id = 2,
            name = "Raghad",
            description = "Android Developer.",

            ),
        Item(
            id = 3,
            name = "Nawal",
            description = "Android Developer.",

            ),
        Item(
            id = 4,
            name = "Mshari",
            description = "Android Developer.",

            ),
        Item(
            id = 5,
            name = "Abdurhman",
            description = "Android Developer.",

            ),
        Item(
            id = 6,
            name = "Fahad",
            description = "Android Developer.",
            )
    )
 
```

Composeable view 

```
@Composable
fun magicRecyclerViewItem(item: Item) {
    Card(
        backgroundColor = Color.Blue,
        shape = RoundedCornerShape(0.dp),
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

@Composable
fun actionIcon(@DrawableRes id: Int) {
    Icon(

        painter = painterResource(id = id),
        contentDescription = "Icon"
    )


}

@Composable
fun actionText(text: String?) {


    Text(
        text = text ?: "",
        style = MaterialTheme.typography.button,
        color = Color.Black
    )
}
```



## Vertical


```
   VerticalRecyclerView(
        modifier = Modifier,
        list = itemList,// list of data
        views = { magicRecyclerViewItem(item = it) } // list of composable view,
        emptyView = { // any composeable view if you want to show if the list is empty },
        dividerView = { // any composeable view if want to add a divider between items },
        startActions = listOf(
            Action(
                { actionText("Delete") },
                { actionIcon(R.drawable.ic_delete) },
                onClicked = { position, item ->
                    // do something

                }),
            Action(
                { actionText("Archive") },
                { actionIcon(R.drawable.ic_archive) },
                onClicked = { position, item ->
                    // do something

                })
        ),
        endActions = listOf(
            Action(
                { actionText("Favorite") },
                { actionIcon(R.drawable.ic_favorite) },
                onClicked = { position, item ->
                    // do something

                }),
            Action(
                { actionText("Subscribe") },
                { actionIcon(R.drawable.ic_subscribe) },
                onClicked = { position, item ->
                    // do something

                })
        ),
        startActionBackgroundColor = // set color of start row actions background color ,
        endActionBackgroundColor = // set color of end row actions background color ,
        actionBackgroundHeight = // set height of end row actions background color ,
        actionBackgroundRadiusCorner = set a radius corner of the actions row default value is 0 ,
        isRefreshing = // set false or true to show a swipeLayoutRefresh progress,
        onRefresh: {// it's optional if you want to add swipeRefreshLayout},
        paddingBetweenItems= // to add a padding between items default value is 8f,
        paddingVertical= // to add a padding on top and bottom of thw whole view default value is 0f,
        paddingHorizontal=// to add a padding on right and left of thw whole view default value is 0f,
        scrollTo = // default value is 0,
    )
```



