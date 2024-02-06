package com.example.rmtestapp.ui.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.example.rmtestapp.ui.fonts.CirceFamily

@Composable
fun TabMenu(
    tabIndex: MutableState<Int>,
    tabs: List<String>,
    modifier: Modifier = Modifier
) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomIndicator(
            color = Color(3, 169, 244),
            modifier = Modifier.tabIndicatorOffset(tabPositions[tabIndex.value])
        )
    }

    TabRow(
        modifier = modifier,
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
