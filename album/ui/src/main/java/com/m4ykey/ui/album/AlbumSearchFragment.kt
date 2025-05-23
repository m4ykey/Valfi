package com.m4ykey.ui.album

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.m4ykey.album.ui.R
import com.m4ykey.album.ui.databinding.FragmentAlbumSearchBinding
import com.m4ykey.core.Constants.SPACE_BETWEEN_ITEMS
import com.m4ykey.core.observeUiState
import com.m4ykey.core.views.BaseFragment
import com.m4ykey.core.views.hide
import com.m4ykey.core.views.recyclerview.CenterSpaceItemDecoration
import com.m4ykey.core.views.recyclerview.convertDpToPx
import com.m4ykey.core.views.recyclerview.scrollListener
import com.m4ykey.core.views.utils.showToast
import com.m4ykey.data.local.model.SearchResult
import com.m4ykey.ui.album.adapter.SearchAlbumAdapter
import com.m4ykey.ui.album.adapter.SearchResultAdapter
import com.m4ykey.ui.album.helpers.createGridLayoutManager
import com.m4ykey.ui.album.viewmodel.AlbumViewModel
import com.m4ykey.ui.album.viewmodel.SearchResultViewModel
import com.m4ykey.ui.navigation.AlbumNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class AlbumSearchFragment : BaseFragment<FragmentAlbumSearchBinding>(
    FragmentAlbumSearchBinding::inflate
) {

    private var isClearButtonVisible = false
    private val viewModel by viewModels<AlbumViewModel>()
    private val searchAdapter by lazy { createSearchAdapter() }
    private val searchResultAdapter by lazy { createSearchResultAdapter() }
    private val searchResultViewModel by viewModels<SearchResultViewModel>()

    @Inject
    lateinit var navigator : AlbumNavigator

    private val speechRecognizerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                val spokenText = matches[0]
                binding.etSearch.setText(spokenText)
                lifecycleScope.launch { viewModel.searchAlbums(spokenText) }
            }
        }
    }

    private lateinit var requestPermissionLauncher : ActivityResultLauncher<String>

    private var originalWidth : Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        searchAlbums()
        handleRecyclerViewButton()
        observeViewModelStates()

        bottomNavigationVisibility?.hideBottomNavigation()

        searchResultViewModel.getSearchResult()

        lifecycleScope.launch {
            viewModel.hasSearchResults.collect { hasResults ->
                if (hasResults) showSearchResults() else showSearchHistory()
            }
        }

        lifecycleScope.launch {
            searchResultViewModel.searchResult.collect { result ->
                searchResultAdapter.submitList(result)
                binding.txtClearList.isVisible = result.isNotEmpty()
            }
        }

        lifecycleScope.launch {
            searchResultViewModel.searchResult.collect { result ->
                val isSearchActive = viewModel.hasSearchResults.value

                binding.txtClearList.isVisible = result.isNotEmpty()

                if (!isSearchActive) {
                    searchResultAdapter.submitList(result)
                    binding.rvSearchAlbums.isVisible = false
                    binding.linearLayoutSearchResult.isVisible = true
                }
            }
        }

        binding.etSearch.post {
            originalWidth = binding.etSearch.width
        }

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted : Boolean ->
            if (isGranted) {
                recordAudio()
            } else {
                showToast(requireContext(), getString(R.string.permission_not_granted))
            }
        }
    }

    private fun showSearchResults() {
        binding.apply {
            rvSearchAlbums.isVisible = true
            linearLayoutSearchResult.isVisible = false
        }
    }

    private fun showSearchHistory() {
        binding.apply {
            linearLayoutSearchResult.isVisible = false
            rvSearchAlbums.isVisible = false
        }
    }

    private fun observeViewModelStates() {
        observeUiState(
            progressBar = binding.progressBar,
            lifecycleScope = lifecycleScope,
            context = requireContext(),
            flow = viewModel.search,
            onSuccess = { search ->
                search.let {
                    lifecycleScope.launch {
                        searchAdapter.submitData(it)
                    }
                }
            }
        )
    }

    private fun createSearchResultAdapter() : SearchResultAdapter {
        return SearchResultAdapter(
            onSearchClick = { item ->
                val searchResult = item.name
                binding.etSearch.setText(searchResult)

                lifecycleScope.launch {
                    viewModel.searchAlbums(searchResult)
                    showSearchResults()
                }
            }
        )
    }

    private fun requestPermission() {
        if (isRecordAudioPermissionGranted()) {
            recordAudio()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun isRecordAudioPermissionGranted() : Boolean {
        return ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun createSearchAdapter() : SearchAlbumAdapter {
        return SearchAlbumAdapter(
            onAlbumClick = { album ->
                val searchResult = SearchResult(name = album.name)
                searchResultViewModel.insertSearchResult(searchResult)

                navigator.navigateToAlbumDetail(album.id)
            }
        )
    }

    private fun handleRecyclerViewButton() {
        binding.apply {
            rvSearchAlbums.addOnScrollListener(scrollListener(btnToTop))
            btnToTop.setOnClickListener {
                rvSearchAlbums.smoothScrollToPosition(0)
            }
        }
    }

    private fun resetSearchWidth() {
        binding.apply {
            val params = etSearch.layoutParams
            params.width = originalWidth
            etSearch.layoutParams = params
        }
    }

    private fun searchAlbums() {
        binding.apply {
            etSearch.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        val searchQuery = etSearch.text?.toString()?.trim()
                        if (searchQuery?.isNotEmpty() == true) {
                            binding.linearLayoutSearchResult.isVisible = false
                            binding.rvSearchAlbums.isVisible = true
                            viewModel.clearSearch()
                            lifecycleScope.launch { viewModel.searchAlbums(searchQuery) }
                        } else {
                            showToast(requireContext(), getString(R.string.empty_search))
                        }
                    }
                }
                actionId == EditorInfo.IME_ACTION_SEARCH
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            val currentScrollPosition = (rvSearchAlbums.layoutManager as? GridLayoutManager)
                ?.findFirstVisibleItemPosition()

            recyclerViewSearchResult.apply {
                if (itemDecorationCount == 0) {
                    addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))
                }
                adapter = searchResultAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            rvSearchAlbums.apply {
                if (itemDecorationCount == 0) {
                    addItemDecoration(CenterSpaceItemDecoration(convertDpToPx(SPACE_BETWEEN_ITEMS)))
                }
                adapter = searchAdapter
                layoutManager = createGridLayoutManager(requireContext())
                currentScrollPosition?.let { position ->
                    layoutManager?.scrollToPosition(position)
                }
            }
        }
    }

    private fun setupToolbar() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            etSearch.doOnTextChanged { text, _, _, _ ->
                val isSearchEmpty = text.isNullOrBlank()
                handleClearButtonVisibility(isSearchEmpty)
            }
            imgMicrophone.setOnClickListener { requestPermission() }

            imgClear.setOnClickListener {
                etSearch.setText(getString(R.string.empty_string))
                hideClearButtonWithAnimation()
            }
        }
    }

    private fun recordAudio() {
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            showToast(requireContext(), getString(R.string.speech_not_recognition))
        } else {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.enter_album_name))
            }
            speechRecognizerLauncher.launch(intent)
        }
    }

    private fun handleClearButtonVisibility(isSearchEmpty: Boolean) {
        if (!isSearchEmpty && !isClearButtonVisible) {
            showClearButtonWithAnimation()
            isClearButtonVisible = true
        } else if (isSearchEmpty && isClearButtonVisible) {
            hideClearButtonWithAnimation()
            isClearButtonVisible = false
        }
    }

    private fun View.animationProperties(
        translationXValue: Float,
        alphaValue: Float,
        interpolator: Interpolator
    ) {
        animate()
            .translationX(translationXValue)
            .alpha(alphaValue)
            .setInterpolator(interpolator)
            .start()
    }

    private fun showClearButtonWithAnimation() {
        binding.imgClear.apply {
            translationX = 100f
            alpha = 0f
            isVisible = true

            animationProperties(0f, 1f, DecelerateInterpolator())
        }
    }

    private fun hideClearButtonWithAnimation() {
        binding.imgClear.apply {
            animationProperties(width.toFloat(), 0f, AccelerateInterpolator())
            hide()
        }
        resetSearchWidth()
    }

    private fun resetSearchState() {
        binding.apply {
            if (etSearch.text.isNullOrBlank()) {
                imgClear.isVisible = false
                isClearButtonVisible = false

                if (!viewModel.hasSearchResults.value) {
                    showSearchHistory()
                }
            } else {
                imgClear.isVisible = true
                isClearButtonVisible = true

                if (viewModel.hasSearchResults.value) {
                    showSearchResults()
                } else {
                    showSearchHistory()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resetSearchState()
    }
}