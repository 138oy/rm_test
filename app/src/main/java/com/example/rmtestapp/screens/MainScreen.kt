package com.example.rmtestapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.domain.entities.Camera
import com.example.domain.entities.Door
import com.example.rmtestapp.R
import com.example.rmtestapp.ui.UIState
import com.example.rmtestapp.ui.fonts.CirceFamily
import com.example.rmtestapp.viewmodels.CamerasViewModel
import com.example.rmtestapp.viewmodels.DoorsViewModel
import com.kevinnzou.compose.swipebox.SwipeBox
import com.kevinnzou.compose.swipebox.SwipeDirection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    camerasViewModel: CamerasViewModel = hiltViewModel(),
    doorsViewModel: DoorsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    camerasViewModel.getCameras()
    doorsViewModel.getDoors()

    val tabIndex = rememberSaveable { mutableStateOf(0) }

    val state = when (tabIndex.value) {
        0 -> camerasViewModel.state.collectAsState().value
        1 -> doorsViewModel.state.collectAsState().value
        else -> UIState.Loading
    }

    val onRefresh = when (tabIndex.value) {
        0 -> camerasViewModel::getCameras
        1 -> doorsViewModel::getDoors
        else -> ::doNothing
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state is UIState.Loading,
        onRefresh = onRefresh
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .pullRefresh(pullRefreshState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            PullRefreshIndicator(
                refreshing = state is UIState.Loading,
                state = pullRefreshState,
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.main_header_text),
                modifier = Modifier.padding(top = 60.dp, bottom = 20.dp),
                fontSize = TextUnit(21F, TextUnitType.Sp),
                fontFamily = CirceFamily,
                fontWeight = FontWeight.Normal,
            )
        }

        item {
            TabMenu(tabIndex, listOf("Камеры", "Двери"))
        }

        when (state) {
            is UIState.Success<*> -> {
                when (tabIndex.value) {
                    0 -> {
                        val items = state.data as List<Camera>
                        items(items) { item ->
                            CameraSwipeBox(
                                data = item,
                                viewModel = camerasViewModel
                            )
                        }
                    }

                    1 -> {
                        val items = state.data as List<Door>
                        items(items) { item ->
                            DoorSwipeBox(
                                data = item,
                                viewModel = doorsViewModel
                            )
                        }
                    }
                }
            }

            is UIState.Error -> item { Text(state.errorMsg) }
            is UIState.Loading -> item {  }
        }
    }
}


@Composable
fun TabMenu(
    tabIndex: MutableState<Int>,
    tabs: List<String>,
) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomIndicator(
            Modifier.tabIndicatorOffset(tabPositions[tabIndex.value]),
            Color(3, 169, 244)
        )
    }

    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = tabIndex.value,
        indicator = indicator
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        fontSize = TextUnit(17F, TextUnitType.Sp),
                        fontFamily = CirceFamily,
                        fontWeight = FontWeight.Normal,
                    )
                },
                selected = tabIndex.value == index,
                onClick = { tabIndex.value = index },
            )
        }
    }
}

@Composable
fun CustomIndicator(modifier: Modifier, color: Color) {
    Box(
        modifier
            .height(2.dp)
            .fillMaxWidth()
            .border(BorderStroke(2.dp, color), RoundedCornerShape(5.dp))
    )
}

// **CAMERAS** //

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CameraSwipeBox(
    data: Camera,
    viewModel: CamerasViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val isLiked = remember { mutableStateOf(data.isFavorites) }
    val imgVector =
        if (isLiked.value) ImageVector.vectorResource(id = R.drawable.ic_star)
        else ImageVector.vectorResource(id = R.drawable.ic_star_line)

    SwipeBox(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 21.dp, vertical = 11.dp),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 60.dp,
        endContent = { swipeableState, _ ->
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    onClick = {
                        isLiked.value = !isLiked.value
                        viewModel.onFavoritePressed(data.cameraId)
                        coroutineScope.launch { swipeableState.animateTo(0) }
                    },
                    modifier = Modifier
                        .padding(horizontal = 9.dp)
                        .then(Modifier.size(40.dp))
                        .border(1.dp, Color(230, 230, 230), shape = CircleShape)
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = imgVector,
                        contentDescription = "Add to favorite",
                        tint = Color(224, 190, 53)
                    )
                }
            }
        }) { _, _, _ ->

        CameraCard(data = data)

        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
                contentDescription = "Play button",
                tint = Color.White,
                modifier = Modifier
                    .padding(vertical = 73.dp, horizontal = 143.dp)
                    .size(60.dp)
            )

            if (data.isRec) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_rec),
                    contentDescription = "Rec is on",
                    tint = Color(250, 48, 48),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                        .align(Alignment.TopStart)
                )
            }

            if (isLiked.value) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_star),
                    contentDescription = "Is in favorites",
                    tint = Color(224, 190, 53),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 72.dp)
                    .align(Alignment.BottomStart),
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                color = Color.White
            ) {
                Text(
                    text = data.name,
                    fontSize = TextUnit(17F, TextUnitType.Sp),
                    fontFamily = CirceFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun CameraCard(data: Camera) {
    Card(
        modifier = Modifier
            .height(279.dp)
            .fillMaxWidth()
    ) {
        CameraImage(data.snapshot)
    }
}

@Composable
fun CameraImage(url: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(
            LocalContext.current
        )
            .data("https://static.bubble.ru/product/cover/4c2353c1f1d1f777ab5e40deb083f286.jpg")
            .size(Size.ORIGINAL)
            .build()
    )

    when (painter.state) {
        is AsyncImagePainter.State.Loading -> {
            Box {}
        }

        is AsyncImagePainter.State.Error -> {
            Text(text = (painter.state as AsyncImagePainter.State.Error).toString())
        }

        else -> {
            AsyncImage(
                model = painter.request.data,
                contentDescription = "Snapshot",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

// **DOORS** //

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorSwipeBox(
    data: Door,
    viewModel: DoorsViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val isLiked = remember { mutableStateOf(data.isFavorites) }
    val imgVector =
        if (isLiked.value) ImageVector.vectorResource(id = R.drawable.ic_star)
        else ImageVector.vectorResource(id = R.drawable.ic_star_line)

    SwipeBox(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 21.dp, vertical = 11.dp),
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 110.dp,
        endContent = { swipeableState, _ ->
            Box(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.align(Alignment.Center)) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch { swipeableState.animateTo(0) }
                        },
                        modifier = Modifier
                            .padding(horizontal = 9.dp)
                            .then(Modifier.size(40.dp))
                            .border(1.dp, Color(230, 230, 230), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                            contentDescription = "Add to favorite",
                            tint = Color(3, 169, 244)
                        )
                    }

                    IconButton(
                        onClick = {
                            isLiked.value = !isLiked.value
                            viewModel.onFavoritePressed(data.doorId)
                            coroutineScope.launch { swipeableState.animateTo(0) }
                        },
                        modifier = Modifier
                            .padding(horizontal = 9.dp)
                            .then(Modifier.size(40.dp))
                            .border(1.dp, Color(230, 230, 230), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = imgVector,
                            contentDescription = "Add to favorite",
                            tint = Color(224, 190, 53)
                        )
                    }
                }
            }
        }) { _, _, _ ->

        if (data.snapshot != null) {
            DoorCard(data = data)

            Box(modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
                    contentDescription = "Play button",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(vertical = 73.dp, horizontal = 143.dp)
                        .size(60.dp)
                )

                if (isLiked.value) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_star),
                        contentDescription = "Is in favorites",
                        tint = Color(224, 190, 53),
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp)
                            .align(Alignment.TopEnd)
                    )
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 72.dp)
                        .align(Alignment.BottomStart),
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                    color = Color.White
                ) {
                    Text(
                        text = data.name,
                        fontSize = TextUnit(17F, TextUnitType.Sp),
                        fontFamily = CirceFamily,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
                    )
                }
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 72.dp)
                    .align(Alignment.BottomStart),
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Text(
                    text = data.name,
                    fontSize = TextUnit(17F, TextUnitType.Sp),
                    fontFamily = CirceFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun DoorCard(data: Door) {
    Card(
        modifier = Modifier
            .height(279.dp)
            .fillMaxWidth()
    ) {
        DoorImage(data.snapshot!!)
    }
}

@Composable
fun DoorImage(url: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(
            LocalContext.current
        )
            .data("https://static.bubble.ru/product/cover/4c2353c1f1d1f777ab5e40deb083f286.jpg")
            .size(Size.ORIGINAL)
            .build()
    )

    when (painter.state) {
        is AsyncImagePainter.State.Loading -> {
            Box {}
        }

        is AsyncImagePainter.State.Error -> {
            Text(text = (painter.state as AsyncImagePainter.State.Error).toString())
        }

        else -> {
            AsyncImage(
                model = painter.request.data,
                contentDescription = "Snapshot",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

fun doNothing() {}