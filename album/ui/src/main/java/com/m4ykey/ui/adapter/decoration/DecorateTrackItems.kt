package com.m4ykey.ui.adapter.decoration

import androidx.paging.PagingData
import androidx.paging.map
import com.m4ykey.data.domain.model.track.TrackItem

fun decorateTrackItems(trackItems : PagingData<TrackItem>) : PagingData<DecoratedTrackItem> {
    var previousDiscNumber : Int? = null

    return trackItems.map { trackItem ->
        val showDiscNumber = trackItem.discNumber != previousDiscNumber
        previousDiscNumber = trackItem.discNumber
        DecoratedTrackItem(showDiscNumber, trackItem)
    }
}