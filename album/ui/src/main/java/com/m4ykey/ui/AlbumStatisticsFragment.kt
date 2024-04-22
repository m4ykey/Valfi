package com.m4ykey.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.m4ykey.core.Constants.ALBUM
import com.m4ykey.core.Constants.COMPILATION
import com.m4ykey.core.Constants.EP
import com.m4ykey.core.Constants.SINGLE
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.ui.databinding.FragmentAlbumStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumStatisticsFragment : Fragment() {

    private var _binding : FragmentAlbumStatisticsBinding? = null
    private val binding get() = _binding!!
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private lateinit var navController: NavController
    private val viewModel : AlbumViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationVisibility) {
            bottomNavigationVisibility = context
        } else {
            throw RuntimeException("$context ${getString(R.string.must_implement_bottom_navigation)}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        bottomNavigationVisibility?.hideBottomNavigation()

        with(binding) {
            toolbar.setNavigationOnClickListener { navController.navigateUp() }

            lifecycleScope.launch {
                val album = viewModel.getAlbumTypeCount(ALBUM).firstOrNull() ?: 0
                val single = viewModel.getAlbumTypeCount(SINGLE).firstOrNull() ?: 0
                val compilation = viewModel.getAlbumTypeCount(COMPILATION).firstOrNull() ?: 0
                val ep = viewModel.getAlbumTypeCount(EP).firstOrNull() ?: 0
                val albumTypes = listOf(album, ep, single, compilation)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}