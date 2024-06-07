package com.m4ykey.ui.helpers

import android.content.Context
import android.graphics.Paint
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.DrawContext
import com.patrykandpatrick.vico.core.common.component.Component

class CircleComponent(private val radius: Float, val color: Int, val context : Context) :
    Component {
    override val margins: Dimensions
        get() = Dimensions(
            startDp = 0f,
            endDp = 0f,
            bottomDp = 0f,
            topDp = 0f
        )

    override fun draw(
        context: DrawContext,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        opacity: Float
    ) {
        val canvas = context.canvas
        val centerX = (left + right) / 2
        val centerY = (top + bottom) / 2

        val paint = Paint().apply {
            this.color = color
            this.style = Paint.Style.FILL
        }
        canvas.drawCircle(centerX, centerY, radius, paint)
    }
}