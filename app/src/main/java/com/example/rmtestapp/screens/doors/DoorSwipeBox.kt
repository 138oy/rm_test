package com.example.rmtestapp.screens.doors

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.domain.entities.Door
import com.example.rmtestapp.R
import com.example.rmtestapp.ui.components.ImageCard
import com.example.rmtestapp.ui.components.icons.FilledStarIcon
import com.example.rmtestapp.ui.components.icons.LockIcon
import com.example.rmtestapp.ui.components.icons.PlayIcon
import com.example.rmtestapp.ui.fonts.CirceFamily
import com.example.rmtestapp.viewmodels.DoorsViewModel
import com.kevinnzou.compose.swipebox.SwipeBox
import com.kevinnzou.compose.swipebox.SwipeDirection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorSwipeBox(
    data: Door,
    viewModel: DoorsViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val isLiked = remember { mutableStateOf(data.isFavorites) }
    val name = remember { mutableStateOf(data.name) }

    val imgVector =
        if (isLiked.value) ImageVector.vectorResource(id = R.drawable.ic_star)
        else ImageVector.vectorResource(id = R.drawable.ic_star_line)

    val showDialog = rememberSaveable { mutableStateOf(false) }

    SwipeBox(
        modifier = modifier,
        swipeDirection = SwipeDirection.EndToStart,
        endContentWidth = 110.dp,
        endContent = { swipeableState, _ ->

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    IconButton(
                        onClick = {
                            showDialog.value = true
                            coroutineScope.launch { swipeableState.animateTo(0) }
                        },
                        modifier = Modifier
                            .padding(horizontal = 9.dp)
                            .size(36.dp)
                            .border(1.dp, Color(230, 230, 230), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit name",
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
        }
    ) { _, _, _ ->

        val snapshot = data.snapshot

        if (snapshot != null) {
            ImageCard(
                snapshot = snapshot,
                modifier = Modifier
                    .height(279.dp)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                PlayIcon(
                    color = Color.White,
                    modifier = Modifier
                        .padding(vertical = 73.dp, horizontal = 143.dp)
                        .size(60.dp)
                        .align(Alignment.TopCenter)
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
                    Box(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = name.value,
                            fontSize = TextUnit(17F, TextUnitType.Sp),
                            fontFamily = CirceFamily,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .padding(vertical = 20.dp, horizontal = 16.dp)
                                .align(Alignment.CenterStart)
                        )
                        LockIcon(
                            color = Color(3, 169, 244),
                            modifier = Modifier
                                .padding(24.dp)
                                .size(24.dp)
                                .align(Alignment.CenterEnd)
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = name.value,
                    fontSize = TextUnit(17F, TextUnitType.Sp),
                    fontFamily = CirceFamily,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                        .align(Alignment.CenterStart)
                )
                LockIcon(
                    color = Color(3, 169, 244),
                    modifier = Modifier
                        .padding(24.dp)
                        .size(24.dp)
                        .align(Alignment.CenterEnd)
                )
            }
        }

        if (showDialog.value) DoorDialogWindow(
            showDialog = showDialog,
            viewModel = viewModel,
            id = data.doorId,
            name = name,
            modifier = Modifier.size(height = 210.dp, width = 280.dp)
        )
    }
}