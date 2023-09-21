package com.m4ykey.valfi.presentation.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.m4ykey.common.Constants.TMDB_IMAGE_ORIGINAL
import com.m4ykey.common.utils.DiffUtils
import com.m4ykey.remote.movie.model.MovieCast
import com.m4ykey.valfi.databinding.LayoutCastBinding

class CastAdapter : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private var cast = emptyList<MovieCast.CastDetail>()

    fun submitCast(newData : List<MovieCast.CastDetail>) {
        val oldData = cast.toList()
        cast = newData
        DiffUtil.calculateDiff(DiffUtils(oldData, newData)).dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class CastViewHolder(private val binding : LayoutCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cast : MovieCast.CastDetail) {
            with(binding) {
                txtActorName.text = cast.name
                imgCast.load(TMDB_IMAGE_ORIGINAL + cast.profilePath) {
                    crossfade(true)
                    crossfade(500)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = LayoutCastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(cast[position])
    }

    override fun getItemCount(): Int {
        return cast.size
    }
}