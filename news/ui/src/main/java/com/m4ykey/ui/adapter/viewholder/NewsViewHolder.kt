package com.m4ykey.ui.adapter.viewholder

import com.m4ykey.core.views.loadImage
import com.m4ykey.core.views.recyclerview.BaseViewHolder
import com.m4ykey.data.domain.model.Article
import com.m4ykey.ui.R
import com.m4ykey.ui.databinding.LayoutNewsListBinding
import com.m4ykey.ui.logos.Logos.BILLBOARD_LOGO
import com.m4ykey.ui.logos.Logos.CONSEQUENCE_LOGO
import com.m4ykey.ui.logos.Logos.NME_LOGO
import com.m4ykey.ui.logos.Logos.PITCHFORK_LOGO
import com.m4ykey.ui.logos.Logos.ROLLING_STONE_LOGO
import com.m4ykey.ui.logos.Logos.STEREOGUM_LOGO
import com.m4ykey.ui.logos.Logos.THE_FADER_LOGO
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class NewsViewHolder(
    binding: LayoutNewsListBinding,
    private val onNewsClick : (Article) -> Unit
) : BaseViewHolder<Article, LayoutNewsListBinding>(binding) {

    override fun bind(item : Article) {
        with(binding) {
            linearLayoutArticle.setOnClickListener { onNewsClick(item) }

            val logos = mapOf(
                "Rolling Stone" to ROLLING_STONE_LOGO,
                "Pitchfork" to PITCHFORK_LOGO,
                "Billboard" to BILLBOARD_LOGO,
                "Stereogum" to STEREOGUM_LOGO,
                "NME" to NME_LOGO,
                "Consequence.net" to CONSEQUENCE_LOGO,
                "The FADER" to THE_FADER_LOGO
            )

            loadImage(imgArticle, item.urlToImage, imgArticle.context)
            loadImage(imgLogo, logos[item.source.name].orEmpty(), imgLogo.context)
            txtTitle.text = item.title
            txtSourceNameDate.text = txtSourceNameDate.context.getString(R.string.source_date, item.source.name, formatDate(item.publishedAt))
        }
    }

    private fun formatDate(date : String?) : String? {
        return date?.takeIf { it.isNotEmpty() }?.let {
            try {
                val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

                val parsedDate = LocalDateTime.parse(it, inputFormatter)
                outputFormatter.format(parsedDate)
            } catch (e : DateTimeParseException) {
                e.printStackTrace()
                null
            }
        }
    }
}