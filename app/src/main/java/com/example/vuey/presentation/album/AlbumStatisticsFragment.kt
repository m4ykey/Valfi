package com.example.vuey.presentation.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.vuey.R
import com.example.vuey.databinding.FragmentAlbumStatisticsBinding
import com.example.vuey.presentation.album.viewmodel.AlbumViewModel
import com.m4ykey.common.utils.hideBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumStatisticsFragment : Fragment() {

    private var _binding: FragmentAlbumStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            hideBottomNavigation(R.id.bottomNavigation)
            composeView.setContent {
                Scaffold(
                    topBar = {
                        AlbumTopAppBar(navigateUp = { findNavController().navigateUp() })
                    }
                ) { innerPadding ->
                    StatisticsScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumTopAppBar(
    navigateUp: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.statistics),
                color = colorResource(id = R.color.textColor),
                fontFamily = FontFamily(Font(R.font.cabin))
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.background)),
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = { navigateUp(R.id.albumFragment) }) {
                Icon(
                    contentDescription = stringResource(id = R.string.back),
                    imageVector = Icons.Filled.ArrowBack,
                    tint = colorResource(id = R.color.menuIconTint)
                )
            }
        }
    )
}

@Composable
fun StatisticsScreen(
    modifier : Modifier,
    viewModel: AlbumViewModel
) {

    val albumCount by viewModel.getAlbumCount().collectAsState(initial = "")
    val totalTracks by viewModel.getTotalTracks().collectAsState(initial = "")
    val totalLength by viewModel.getTotalLength().collectAsState(initial = 0)

    val hour = totalLength / (1000 * 60 * 60)
    val minute = (totalLength / (1000 * 60)) % 60
    val second = (totalLength / 1000) % 60

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = albumCount.toString(),
            fontSize = 80.sp,
            fontFamily = FontFamily(Font(R.font.cabin_semibold)),
            color = colorResource(id = R.color.textColor)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(id = R.string.listened_albums),
            fontSize = 25.sp,
            fontFamily = FontFamily(Font(R.font.cabin)),
            color = colorResource(id = R.color.textColor)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = totalTracks.toString(),
            fontSize = 40.sp,
            fontFamily = FontFamily(Font(R.font.cabin_semibold)),
            color = colorResource(id = R.color.textColor)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(id = R.string.listened_songs),
            color = colorResource(id = R.color.textColor),
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.cabin))
        )
        Spacer(modifier = Modifier.height(20.dp))
        CalculateAlbumTime(
            hour = hour,
            minute = minute,
            second = second
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            color = colorResource(id = R.color.textColor),
            text = stringResource(id = R.string.total_time),
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.cabin))
        )
    }
}

@Composable
fun CalculateAlbumTime(hour: Int, minute: Int, second: Int) {
    val text = if (hour == 0) {
        String.format("%d min %d ${stringResource(id = R.string.sec)}", minute, second)
    } else if (minute == 0) {
        String.format("%d ${stringResource(id = R.string.hour)}", hour)
    } else {
        String.format("%d ${stringResource(id = R.string.hour)} %d min", hour, minute)
    }
    Text(
        text = text,
        fontSize = 40.sp,
        color = colorResource(id = R.color.textColor),
        fontFamily = FontFamily(Font(R.font.cabin_semibold))
    )
}