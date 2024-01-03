package com.m4ykey.valfi2.album.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.m4ykey.valfi2.album.data.domain.model.Item
import com.m4ykey.valfi2.databinding.LayoutSearchAlbumBinding

class AlbumViewHolder(private val binding : LayoutSearchAlbumBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item : Item) {
        binding.txtAlbumName.text = item.name
    }

    companion object {
        fun create(view : ViewGroup) : AlbumViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = LayoutSearchAlbumBinding.inflate(inflater, view, false)
            return AlbumViewHolder(binding)
        }
    }

}