package com.m4ykey.core.views

import android.view.View

fun <T, I> getLargestImageFromUrl(
    item : T,
    getImageList : (T) -> List<I>,
    getHeight : (I) -> Int,
    getWidth : (I) -> Int,
    getUrl : (I) -> String
) : String? {
    return getImageList(item).maxByOrNull { getHeight(it) * getWidth(it) }?.let(getUrl)
}

fun <T, A> getArtistsList(
    item : T,
    getArtistList : (T) -> List<A>,
    getArtistName : (A) -> String
) : String {
    return getArtistList(item).joinToString(", ") { getArtistName(it) }
}

fun View.hide() {
    visibility = View.GONE
}
fun View.show() {
    visibility = View.VISIBLE
}