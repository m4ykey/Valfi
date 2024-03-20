package com.m4ykey.ui.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.core.views.recyclerview.OnItemClickListener
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.data.local.model.relations.AlbumWithStates
import com.m4ykey.ui.AlbumViewModel
import com.m4ykey.ui.databinding.LayoutAlbumGridBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchAlbumViewHolder(
    private val binding: LayoutAlbumGridBinding,
    listener : OnItemClickListener<AlbumItem>?,
    private val viewModel : AlbumViewModel,
    private val context : Context
) : BaseViewHolder<AlbumItem>(listener, binding.root) {

    companion object {
        fun create(view : ViewGroup, listener: OnItemClickListener<AlbumItem>?, viewModel: AlbumViewModel, context: Context) : SearchAlbumViewHolder {
            return SearchAlbumViewHolder(
                listener = listener,
                viewModel = viewModel,
                binding = LayoutAlbumGridBinding.inflate(LayoutInflater.from(view.context), view, false),
                context = context
            )
        }
    }

    private lateinit var currentAlbumItem: AlbumItem

    override fun bind(item: AlbumItem) {
        currentAlbumItem = item
        with(binding) {
            val image = item.images.maxByOrNull { it.height * it.width }?.url
            val artistList = item.artists.joinToString(", ") { it.name }
            loadImage(imgAlbum, image.toString(), context)
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

    override fun getItem(position: Int): AlbumItem = currentAlbumItem
}