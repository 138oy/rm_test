package com.example.rmtestapp.screens.doors

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rmtestapp.R
import com.example.rmtestapp.ui.fonts.CirceFamily
import com.example.rmtestapp.viewmodels.DoorsViewModel

@Composable
fun DoorDialogWindow(
    showDialog: MutableState<Boolean>,
    viewModel: DoorsViewModel,
    id: Int,
    name: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val newName = rememberSaveable { mutableStateOf("") }

    Dialog(
        onDismissRequest = { showDialog.value = false }
    ) {
        Card(
            shape = RoundedCornerShape(size = 12.dp),
            modifier = modifier
        ) {
            Box(
                modifier = modifier.defaultMinSize()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.main_dialog_text),
                        fontSize = TextUnit(17F, TextUnitType.Sp),
                        fontFamily = CirceFamily,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .padding(vertical = 15.dp)
                    )

                    OutlinedTextField(
                        value = newName.value,
                        onValueChange = { input: String -> newName.value = input }
                    )
                }

                Button(
                    onClick = {
                        viewModel.onNameChange(id, newName.value)
                        showDialog.value = false
                        name.value = newName.value
                    },
                    colors = ButtonDefaults.buttonColors(
                        Color(3, 169, 244)
                    ),
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.main_dialog_button_confirm),
                        modifier = Modifier.padding(5.dp)
                    )
                }

                Button(
                    onClick = { showDialog.value = false },
                    colors = ButtonDefaults.buttonColors(
                        Color.Gray
                    ),
                    modifier = Modifier
                        .wrapContentWidth()
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.main_dialog_button_cancel),
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}
