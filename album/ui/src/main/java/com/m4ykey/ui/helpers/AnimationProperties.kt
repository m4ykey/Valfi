package com.m4ykey.ui.helpers

import android.view.View
import android.view.animation.Interpolator

fun View.animationPropertiesY(translationYValue : Float, alphaValue : Float, interpolator : Interpolator) {
    animate()
        .translationY(translationYValue)
        .alpha(alphaValue)
        .setInterpolator(interpolator)
        .start()
}