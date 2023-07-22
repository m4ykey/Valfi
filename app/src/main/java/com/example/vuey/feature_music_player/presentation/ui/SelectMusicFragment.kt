package com.example.vuey.feature_music_player.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.vuey.databinding.FragmentSelectMusicBinding
import com.example.vuey.feature_music_player.data.remote.api.YoutubeApi
import com.example.vuey.feature_music_player.presentation.MusicViewModel
import com.example.vuey.feature_music_player.presentation.adapter.VideoAdapter
import com.example.vuey.util.Constants
import com.example.vuey.util.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@AndroidEntryPoint
class SelectMusicFragment : Fragment() {

    private var _binding : FragmentSelectMusicBinding? = null
    private val binding get() = _binding!!

    private val args : SelectMusicFragmentArgs by navArgs()

    private val viewModel : MusicViewModel by viewModels()

    private val videoAdapter by lazy { VideoAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            //viewModel.getMusicVideo(args.musicName)
        }

        binding.recyclerViewYoutubeVideo.adapter = videoAdapter

        lifecycleScope.launchWhenCreated {
            binding.progressBar.visibility = View.VISIBLE
            val response = try {
                RetrofitInstance.api.getVideoName(args.musicName)
            } catch (e : IOException) {
                showSnackbar(requireView(), e.localizedMessage ?: "No internet connection")
                return@launchWhenCreated
            } catch (e : HttpException) {
                showSnackbar(requireView(), e.localizedMessage ?: "Unknown error")
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                videoAdapter.submitVideo(response.body()!!.items)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    object RetrofitInstance {
        val api : YoutubeApi by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.YOUTUBE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YoutubeApi::class.java)
        }
    }

}