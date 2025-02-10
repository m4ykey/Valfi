package com.m4ykey.ui.album.adapter.viewholder

import androidx.core.view.isVisible
import com.m4ykey.album.ui.databinding.LayoutAlbumGridBinding
import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.domain.model.album.AlbumItem
import com.m4ykey.ui.album.helpers.OnAlbumClick
import com.m4ykey.ui.album.helpers.getArtistList
import com.m4ykey.ui.album.helpers.getLargestImageUrl

class SearchAlbumViewHolder(
    binding: LayoutAlbumGridBinding,
    private val onAlbumClick: OnAlbumClick
) : BaseViewHolder<AlbumItem, LayoutAlbumGridBinding>(binding) {

    override fun bind(item: AlbumItem) {
        with(binding) {
            layoutAlbum.setOnClickListener { onAlbumClick(item) }

            loadImage(imgAlbum, item.getLargestImageUrl().toString(), imgAlbum.context)
            txtAlbum.text = item.name
            txtArtist.text = item.getArtistList()

            when (item.albumType) {
                "ep" -> txtLabel.text = "E"
                "single" -> txtLabel.text = "S"
                "compilation" -> txtLabel.text = "C"
                "album" -> txtLabel.text = "A"
            }
            txtLabel.isVisible = true
        }
    }
}