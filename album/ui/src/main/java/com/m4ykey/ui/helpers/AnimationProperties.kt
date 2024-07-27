package com.m4ykey.ui.helpers

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator
import com.google.android.material.button.MaterialButton

fun View.animationPropertiesY(translationYValue : Float, alphaValue : Float, interpolator : Interpolator) {
    animate()
        .translationY(translationYValue)
        .alpha(alphaValue)
        .setInterpolator(interpolator)
        .start()
}

fun animateColorTransition(
    startColor: Int,
    endColor: Int,
    vararg buttons: MaterialButton
) {
    val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
    colorAnimator.duration = 2000

    colorAnimator.addUpdateListener { animator ->
        val animatedValue = animator.animatedValue as Int
        buttons.forEach { it.setBackgroundColor(animatedValue) }
    }
    colorAnimator.start()
}