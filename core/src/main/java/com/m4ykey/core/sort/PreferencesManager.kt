package com.m4ykey.core.sort

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {

    suspend fun setRecyclerViewType(newViewType : Int, iconResId : Int)
    fun getRecyclerViewViewType() : Flow<Pair<Int, Int>>

}