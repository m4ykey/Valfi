package com.m4ykey.ui

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.m4ykey.core.Constants.ALBUM
import com.m4ykey.core.Constants.COMPILATION
import com.m4ykey.core.Constants.EP
import com.m4ykey.core.Constants.SINGLE
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.ui.databinding.FragmentAlbumStatisticsBinding
import com.m4ykey.ui.helpers.CircleComponent
import com.patrykandpatrick.vico.core.cartesian.CartesianDrawContext
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasureContext
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.VerticalLegend
import com.patrykandpatrick.vico.core.common.component.TextComponent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumStatisticsFragment : BaseFragment<FragmentAlbumStatisticsBinding>(
    FragmentAlbumStatisticsBinding::inflate
) {

    private val viewModel by viewModels<AlbumViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()

        binding?.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            lifecycleScope.launch {
                val album = viewModel.getAlbumTypeCount(ALBUM).firstOrNull() ?: 0
                val single = viewModel.getAlbumTypeCount(SINGLE).firstOrNull() ?: 0
                val compilation = viewModel.getAlbumTypeCount(COMPILATION).firstOrNull() ?: 0
                val ep = viewModel.getAlbumTypeCount(EP).firstOrNull() ?: 0

                val modelProvider = CartesianChartModelProducer.build()
                chart.modelProducer = modelProvider
                modelProvider.tryRunTransaction { columnSeries { series(album, ep, single, compilation) } }
            }

            val textComponent = TextComponent.build()
            val icon = CircleComponent(
                15f,
                color = Color.RED,
                requireContext()
            )

            val legendItems = listOf(
                LegendItem(
                    labelText = ALBUM,
                    label = textComponent,
                    icon = icon
                ),
                LegendItem(
                    labelText = EP,
                    label = textComponent,
                    icon = icon
                ),
                LegendItem(
                    labelText = SINGLE,
                    label = textComponent,
                    icon = icon
                ),
                LegendItem(
                    labelText = COMPILATION,
                    label = textComponent,
                    icon = icon
                )
            )

            val horizontalLegend = VerticalLegend<CartesianMeasureContext, CartesianDrawContext>(
                items = legendItems,
                iconSizeDp = 5f,
                iconPaddingDp = 10f,
                spacingDp = 0f,
                padding = Dimensions.Empty
            )

            chart.chart?.legend = horizontalLegend

            lifecycleScope.launch {
                val albumCount = viewModel.getAlbumCount().firstOrNull() ?: 0
                val tracksCount = viewModel.getTotalTracksCount().firstOrNull() ?: 0
                startAnimation(albumCount, tracksCount)
            }
        }
    }

    private fun startAnimation(albumCount : Int, tracksCount : Int) {
        val animator = ValueAnimator.ofInt(albumCount, tracksCount).apply {
            duration = 2000
            addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                binding?.txtAlbumCount?.text = if (animatedValue <= albumCount) animatedValue.toString() else albumCount.toString()
                binding?.txtTotalSongsPlayed?.text = if (animatedValue <= tracksCount) animatedValue.toString() else tracksCount.toString()
            }
        }
        animator.start()
    }
}