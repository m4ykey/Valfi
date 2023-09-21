package com.m4ykey.valfi.presentation.movie

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
import com.m4ykey.common.utils.hideBottomNavigation
import com.m4ykey.valfi.R
import com.m4ykey.valfi.databinding.FragmentMovieStatisticsBinding
import com.m4ykey.valfi.presentation.movie.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieStatisticsFragment : Fragment() {

    private var _binding: FragmentMovieStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            hideBottomNavigation(R.id.bottomNavigation)
            composeView.setContent {
                Scaffold(
                    topBar = {
                        MovieTopAppBar(navigateUp = { findNavController().navigateUp() })
                    }
                ) { innerPadding ->
                    Modifier.padding(innerPadding)
                    StatisticsScreen(viewModel = viewModel)
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
fun MovieTopAppBar(
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
            IconButton(onClick = { navigateUp(R.id.movieFragment) }) {
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
    viewModel: MovieViewModel
) {

    val movieCount by viewModel.getMovieCount().collectAsState(initial = "")
    val totalLength by viewModel.getTotalLength().collectAsState(initial = 0)

    val hour = totalLength / 60
    val minute = totalLength % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = movieCount.toString(),
            fontSize = 80.sp,
            color = colorResource(id = R.color.textColor),
            fontFamily = FontFamily(Font(R.font.cabin_semibold))
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(id = R.string.watched_movies),
            fontSize = 25.sp,
            fontFamily = FontFamily(Font(R.font.cabin)),
            color = colorResource(id = R.color.textColor)
        )
        Spacer(modifier = Modifier.height(20.dp))
        CalculateMovieTime(
            hour = hour,
            minute = minute
        )
        Text(
            text = stringResource(id = R.string.total_time),
            color = colorResource(id = R.color.textColor),
            fontSize = 17.sp,
            fontFamily = FontFamily(Font(R.font.cabin))
        )
    }
}

@Composable
fun CalculateMovieTime(hour : Int, minute : Int) {
    val text = if (hour == 0) {
        String.format("%d min", minute)
    } else {
        String.format("%d ${stringResource(R.string.hour)} %d min", hour, minute)
    }
    Text(
        text = text,
        fontSize = 40.sp,
        color = colorResource(id = R.color.textColor),
        fontFamily = FontFamily(Font(R.font.cabin_semibold))
    )
}