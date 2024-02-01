package com.example.rmtestapp.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.common.doNothing
import com.example.domain.entities.Camera
import com.example.domain.entities.Door
import com.example.rmtestapp.R
import com.example.rmtestapp.ui.UIState
import com.example.rmtestapp.ui.components.ImageCard
import com.example.rmtestapp.ui.components.TabMenu
import com.example.rmtestapp.ui.components.icons.FilledStarIcon
import com.example.rmtestapp.ui.components.icons.PlayIcon
import com.example.rmtestapp.ui.components.icons.RecIcon
import com.example.rmtestapp.ui.fonts.CirceFamily
import com.example.rmtestapp.viewmodels.CamerasViewModel
import com.example.rmtestapp.viewmodels.DoorsViewModel
import com.kevinnzou.compose.swipebox.SwipeBox
import com.kevinnzou.compose.swipebox.SwipeDirection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyHomeScreen(
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
                fontSize = TextUnit(value = 21F, type = TextUnitType.Sp),
                fontFamily = CirceFamily,
                fontWeight = FontWeight.Normal,
            )
        }

        item {
            TabMenu(
                tabIndex = tabIndex,
                tabs = listOf(
                    stringResource(id = R.string.main_tab_cameras),
                    stringResource(id = R.string.main_tab_doors)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        MyHomeScreen(
            tabIndex = tabIndex.value,
            state = state,
            camerasViewModel = camerasViewModel,
            doorsViewModel = doorsViewModel
        )
    }
}


fun LazyListScope.MyHomeScreen(
    tabIndex: Int,
    state: UIState,
    camerasViewModel: CamerasViewModel,
    doorsViewModel: DoorsViewModel
) {
    val swipeBoxModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 21.dp, vertical = 11.dp)

    when (state) {
        is UIState.Success<*> -> {
            when (tabIndex) {
                0 -> {
                    val items = state.data as List<Camera>
                    items(items) { item ->
                        CameraSwipeBox(
                            data = item,
                            viewModel = camerasViewModel,
                            modifier = swipeBoxModifier
                        )
                    }
                }

                1 -> {
                    val items = state.data as List<Door>
                    items(items) { item ->
                        DoorSwipeBox(
                            data = item,
                            viewModel = doorsViewModel,
                            modifier = swipeBoxModifier
                        )
                    }
                }
            }
        }

        is UIState.Error -> item { Text(state.errorMsg) }
        is UIState.Loading -> doNothing()
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CameraSwipeBox(
    data: Camera,
    viewModel: CamerasViewModel,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val isLiked = remember { mutableStateOf(data.isFavorites) }

    val imgVector =
        if (isLiked.value) ImageVector.vectorResource(id = R.drawable.ic_star)
        else ImageVector.vectorResource(id = R.drawable.ic_star_line)

    SwipeBox(modifier = modifier,
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
                        .size(36.dp)
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

        ImageCard(
            snapshot = data.snapshot,
            modifier = Modifier
                .height(279.dp)
                .fillMaxWidth()
        )

        Box(modifier = Modifier.fillMaxSize()) {
            PlayIcon(
                color = Color.White,
                modifier = Modifier
                    .padding(vertical = 73.dp, horizontal = 143.dp)
                    .size(60.dp)
            )

            if (data.isRec) {
                RecIcon(
                    color = Color(250, 48, 48),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                        .align(Alignment.TopStart)
                )
            }

            if (isLiked.value) {
                FilledStarIcon(
                    color = Color(224, 190, 53),
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorSwipeBox(
    data: Door,
    viewModel: DoorsViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val isLiked = remember { mutableStateOf(data.isFavorites) }

    val imgVector =
        if (isLiked.value) ImageVector.vectorResource(id = R.drawable.ic_star)
        else ImageVector.vectorResource(id = R.drawable.ic_star_line)

    SwipeBox(modifier = modifier,
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
                            .size(36.dp)
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
                            .size(36.dp)
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

        val snapshot = data.snapshot

        if (snapshot != null) {
            ImageCard(snapshot = snapshot)

            Box(modifier = Modifier.fillMaxSize()) {
                PlayIcon(
                    color = Color.White,
                    modifier = Modifier
                        .padding(vertical = 73.dp, horizontal = 143.dp)
                        .size(60.dp)
                )

                if (isLiked.value) {
                    FilledStarIcon(
                        color = Color(224, 190, 53),
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
