package com.m4ykey.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.core.views.loadImage
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.ui.AlbumViewModel
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchAlbumViewHolder(
    private val binding: LayoutAlbumGridBinding,
    private val viewModel: AlbumViewModel,
    private val onAlbumClick : (AlbumItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(
            parent: ViewGroup,
            onAlbumClick: (AlbumItem) -> Unit,
            viewModel: AlbumViewModel
        ) : SearchAlbumViewHolder {
            return SearchAlbumViewHolder(
                binding = LayoutAlbumGridBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onAlbumClick = onAlbumClick,
                viewModel = viewModel
            )
        }
    }

    fun bind(item: AlbumItem) {
        with(binding) {
            layoutAlbum.setOnClickListener { onAlbumClick(item) }

            val image = item.images.maxByOrNull { it.height * it.width }?.url
            val artistList = item.artists.joinToString(", ") { it.name }
            loadImage(imgAlbum, image.toString(), imgAlbum.context)
            txtAlbum.text = item.name
            txtArtist.text = artistList

            viewModel.viewModelScope.launch {
                val albumWithStates = viewModel.getAlbumWithStates(item.id)
                val isSaved = isAlbumSaved(albumWithStates)
                val isListenLaterSaved = isListenLaterSaved(albumWithStates)
                withContext(Dispatchers.Main) {
                    imgSavedAlbum.isVisible = isSaved
                    imgListenLater.isVisible = isListenLaterSaved
                }
            }
        }
    }

    private fun isAlbumSaved(albumWithStates: AlbumWithStates?) : Boolean {
        return albumWithStates?.isAlbumSaved != null
    }

    private fun isListenLaterSaved(albumWithStates: AlbumWithStates?) : Boolean {
        return albumWithStates?.isListenLaterSaved != null
    }
}