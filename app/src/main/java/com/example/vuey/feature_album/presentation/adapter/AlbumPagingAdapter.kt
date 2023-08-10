package com.example.vuey.feature_album.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.vuey.R
import com.example.vuey.core.common.utils.toAlbumEntity
import com.example.vuey.core.common.utils.toListenLaterEntity
import com.example.vuey.databinding.LayoutAlbumBinding
import com.example.vuey.feature_album.data.remote.model.spotify.album.AlbumList
import com.example.vuey.feature_album.presentation.ui.SearchAlbumFragmentDirections

class AlbumPagingAdapter :
    PagingDataAdapter<AlbumList, AlbumPagingAdapter.AlbumViewHolder>(AlbumDiffUtil) {

    object AlbumDiffUtil : DiffUtil.ItemCallback<AlbumList>() {
        override fun areItemsTheSame(oldItem: AlbumList, newItem: AlbumList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlbumList, newItem: AlbumList): Boolean {
            return oldItem == newItem
        }
    }

    suspend fun submitAlbum(pagingData: PagingData<AlbumList>) {
        submitData(pagingData)
    }

    override fun onBindViewHolder(holder: AlbumPagingAdapter.AlbumViewHolder, position: Int) {
        getItem(position)?.let { album ->
            holder.bind(album)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlbumPagingAdapter.AlbumViewHolder {
        val binding = LayoutAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumViewHolder(binding)
    }

    inner class AlbumViewHolder(private val binding: LayoutAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumList) {
            with(binding) {
                val image = album.imageList.find { it.height == 640 && it.width == 640 }
                txtAlbum.text = album.albumName
                txtArtist.text = album.artistList.joinToString(separator = ", ") { it.artistName }
                imgAlbum.load(image?.url) { error(R.drawable.album_error) }

                layoutAlbum.setOnClickListener {
                    val action =
                        SearchAlbumFragmentDirections.actionSearchAlbumFragmentToAlbumDetailFragment(
                            album = album,
                            albumEntity = album.toAlbumEntity(),
                            listenLaterEntity = album.toListenLaterEntity(),
                            albumId = album.id,
                            isFromAlbumListenLaterFragment = false
                        )
                    it.findNavController().navigate(action)
                }
            }
        }
    }
}