package com.m4ykey.ui.album.helpers

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import com.m4ykey.core.views.animation.animationPropertiesY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

fun showSearchEditText(
    isSearchEditTextVisible : BooleanWrapper,
    linearLayout: LinearLayout,
    translationY : Float
) : BooleanWrapper {
    if (!isSearchEditTextVisible.value) {
        linearLayout.apply {
            this.translationY = translationY
            isVisible = true
            animationPropertiesY(0f, 1f, DecelerateInterpolator())
        }
        isSearchEditTextVisible.value = true
    }
    return isSearchEditTextVisible
}

fun hideSearchEditText(
    isSearchEditTextVisible : BooleanWrapper,
    isHidingAnimationRunning : BooleanWrapper,
    linearLayout: LinearLayout,
    coroutineScope: CoroutineScope,
    translationYValue : Float
) {
    if (isSearchEditTextVisible.value && !isHidingAnimationRunning.value) {
        isHidingAnimationRunning.value = true
        linearLayout.apply {
            translationY = 0f
            animationPropertiesY(translationYValue, 0f, DecelerateInterpolator())
        }
        coroutineScope.launch {
            delay(400)
            linearLayout.isVisible = false
            isSearchEditTextVisible.value = false
            isHidingAnimationRunning.value = false
        }
    }
}

data class BooleanWrapper(var value : Boolean)
