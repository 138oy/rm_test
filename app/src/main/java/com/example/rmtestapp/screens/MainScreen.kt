package com.example.rmtestapp.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.example.rmtestapp.screens.cameras.CameraSwipeBox
import com.example.rmtestapp.screens.doors.DoorSwipeBox
import com.example.rmtestapp.ui.UIState
import com.example.rmtestapp.ui.components.TabMenu
import com.example.rmtestapp.ui.fonts.CirceFamily
import com.example.rmtestapp.viewmodels.CamerasViewModel
import com.example.rmtestapp.viewmodels.DoorsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyHomeScreen(
    navController: NavHostController,
    camerasViewModel: CamerasViewModel = hiltViewModel(),
    doorsViewModel: DoorsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
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

    MyHomeScreen(
        pullRefreshState = pullRefreshState,
        state = state,
        tabIndex = tabIndex,
        camerasViewModel = camerasViewModel,
        doorsViewModel = doorsViewModel,
        modifier = modifier
            .safeContentPadding()
            .fillMaxWidth()
            .pullRefresh(pullRefreshState)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyHomeScreen(
    pullRefreshState: PullRefreshState,
    state: UIState,
    tabIndex: MutableState<Int>,
    camerasViewModel: CamerasViewModel,
    doorsViewModel: DoorsViewModel,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
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

    when (tabIndex) {
        0 -> {
            when (state) {
                is UIState.Success<*> -> {
                    val items = state.data as List<Camera>

                    item {
                        val mappedItems = items.groupBy { it.room }

                        mappedItems.forEach { (room, cameras) ->
                            room?.let {
                                Text(
                                    text = room,
                                    modifier = Modifier.padding(
                                        vertical = 16.dp,
                                        horizontal = 20.dp
                                    ),
                                    fontSize = TextUnit(value = 21F, type = TextUnitType.Sp),
                                    fontFamily = CirceFamily,
                                    fontWeight = FontWeight.Light,
                                )

                                cameras.forEach { camera ->
                                    CameraSwipeBox(
                                        data = camera,
                                        viewModel = camerasViewModel,
                                        modifier = swipeBoxModifier
                                    )
                                }
                            }
                        }
                    }
                }

                is UIState.Error -> item { Text(state.errorMsg) }
                is UIState.Loading -> camerasViewModel.getCameras()
            }
        }

        1 -> {
            when (state) {
                is UIState.Success<*> -> {
                    val items = state.data as List<Door>
                    items(items) { item ->
                        DoorSwipeBox(
                            data = item,
                            viewModel = doorsViewModel,
                            modifier = swipeBoxModifier
                        )
                    }
                }

                is UIState.Error -> item { Text(state.errorMsg) }
                is UIState.Loading -> doorsViewModel.getDoors()
            }
        }
    }
}
