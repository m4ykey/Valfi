package com.m4ykey.ui

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.m4ykey.core.views.BottomNavigationVisibility
import com.m4ykey.core.views.hide
import com.m4ykey.core.views.isNightMode
import com.m4ykey.core.views.show
import com.m4ykey.ui.databinding.FragmentAlbumSearchBinding

class AlbumSearchFragment : Fragment() {

    private var _binding : FragmentAlbumSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private var bottomNavigationVisibility : BottomNavigationVisibility? = null
    private var isClearButtonVisible = false

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
        _binding = FragmentAlbumSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationVisibility?.hideBottomNavigation()
        navController = findNavController()

        with(binding) {
            setupToolbar()
        }
    }

    private fun FragmentAlbumSearchBinding.setupToolbar() {
        val isNightMode = isNightMode(resources)
        val iconTint = if (isNightMode) {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
        }

        with(toolbar) {
            setNavigationOnClickListener { navController.navigateUp() }
            etSearch.doOnTextChanged { text, _, _, _ ->
                val isSearchEmpty = text.isNullOrBlank()
                if (isSearchEmpty && isClearButtonVisible) {
                    hideClearButtonWithAnimation()
                    isClearButtonVisible = false
                } else if (!isSearchEmpty && !isClearButtonVisible) {
                    showClearButtonWithAnimation()
                    isClearButtonVisible = true
                }
            }

            imgClear.setOnClickListener {
                etSearch.setText("")
                hideClearButtonWithAnimation()
            }
        }
    }

    private fun FragmentAlbumSearchBinding.showClearButtonWithAnimation() {
        imgClear.apply {
            alpha = 0f
            translationX = 100f
            show()

            animate()
                .translationX(0f)
                .alpha(1f)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    alpha = 1f
                    translationX = 0f
                }
                .start()
        }
    }

    private fun FragmentAlbumSearchBinding.hideClearButtonWithAnimation() {
        imgClear.animate()
            .translationX(imgClear.width.toFloat())
            .alpha(0f)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                imgClear.alpha = 0f
                imgClear.translationX = imgClear.width.toFloat()
            }
            .start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}