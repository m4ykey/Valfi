package com.m4ykey.valfi.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m4ykey.valfi.R

@Composable
fun EmptyLaterListScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.clock_emoji),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.there_nothing_here),
            fontSize = 28.sp,
            color = colorResource(id = R.color.textColor),
            fontFamily = FontFamily(Font(R.font.cabin_semibold))
        )
        Text(
            text = stringResource(id = R.string.you_have_nothing_for_later),
            fontSize = 18.sp,
            color = colorResource(id = R.color.textGray),
            fontFamily = FontFamily(Font(R.font.cabin))
        )
    }
}

@Composable
fun EmptyListScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.thinking_face_emoji),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = stringResource(id = R.string.something_empty_here),
            fontSize = 28.sp,
            color = colorResource(id = R.color.textColor),
            fontFamily = FontFamily(Font(R.font.cabin_semibold))
        )
        Text(
            text = stringResource(id = R.string.quickly_add_something_to_your_list),
            fontSize = 18.sp,
            color = colorResource(id = R.color.textGray),
            fontFamily = FontFamily(Font(R.font.cabin))
        )
    }
}