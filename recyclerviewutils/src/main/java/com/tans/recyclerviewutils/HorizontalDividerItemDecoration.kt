package com.tans.recyclerviewutils

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * author: pengcheng.tan
 * date: 2019-12-23
 */

typealias ShowDividerController = (child: View, parent: RecyclerView, state: RecyclerView.State) -> Boolean

val ignoreLastDividerController: ShowDividerController = { child, parent, state ->
    val holder = parent.getChildViewHolder(child)
    holder.layoutPosition != state.itemCount - 1
}

class HorizontalDividerItemDecoration(
    val divider: Divider,
    val marginStart: Int,
    val marginEnd: Int,
    val showDividerController: ShowDividerController
) : RecyclerView.ItemDecoration() {


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount: Int = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            if (!showDividerController(child, parent, state)) {
                continue
            }
            val bounds = Rect()
            parent.layoutManager?.getDecoratedBoundsWithMargins(child, bounds) ?: error("LayoutManager is null")
            c.save()
            val left = marginStart
            val top = bounds.bottom - divider.size
            val right = bounds.right - marginEnd
            val bottom = bounds.bottom
            c.clipRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
            c.translate(left.toFloat(), top.toFloat())
            divider.onDraw(canvas = c)
            c.restore()
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (showDividerController(view, parent, state)) {
            outRect.set(0, 0, 0, divider.size)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }


    companion object {

        interface Divider {

            // pixel size, divider width or height.
            val size: Int

            fun onDraw(canvas: Canvas)

        }

        class ColorDivider(@ColorInt val color: Int,
                           override val size: Int) : Divider {

            private val paint: Paint = Paint().apply {
                color = this@ColorDivider.color
                style = Paint.Style.FILL
                isAntiAlias = true
            }

            override fun onDraw(canvas: Canvas) {
                val width = canvas.width
                val height = size
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
        }

        class DrawableDivider(val drawable: Drawable,
                              override val size: Int) : Divider {

            override fun onDraw(canvas: Canvas) {
                val width = canvas.width
                val height = size
                drawable.setBounds(0, 0, width, height)
                drawable.draw(canvas)
            }
        }

        data class Builder(
            var divider: Divider = ColorDivider(color = Color.rgb(66, 66, 66), size = 2),
            var marginStart: Int = 0,
            var marginEnd: Int = 0,
            var showDividerController: ShowDividerController = { _, _, _ -> true }) {
            fun divider(divider: Divider): Builder {
                this.divider = divider
                return this
            }

            fun marginStart(marginStart: Int): Builder {
                this.marginStart = marginStart
                return this
            }

            fun marginEnd(marginEnd: Int): Builder {
                this.marginEnd = marginEnd
                return this
            }

            fun showDividerController(showDividerController: ShowDividerController): Builder {
                this.showDividerController = showDividerController
                return this
            }

            fun build(): HorizontalDividerItemDecoration {
                return HorizontalDividerItemDecoration(
                    divider = divider,
                    marginStart = marginStart,
                    marginEnd = marginEnd,
                    showDividerController = showDividerController)
            }

        }
    }

}