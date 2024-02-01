package com.example.rmtestapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun Image(
    url: String,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            //temporary(?) static link, as the one given by api seems to cause error because of some privacy things
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
                modifier = modifier
            )
        }
    }
}