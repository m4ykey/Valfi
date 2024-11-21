package com.m4ykey.ui.helpers

import com.m4ykey.core.views.getLargestImageFromUrl
import com.m4ykey.data.domain.model.Artist

fun Artist.getLargestImageUrl() : String? {
    return getLargestImageFromUrl(
        this,
        { it.images },
        { it.height },
        { it.width },
        { it.url }
    )
}