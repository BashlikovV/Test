package by.bashlikovvv.homescreen.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import by.bashlikovvv.homescreen.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClockFloatingActionButton : FloatingActionButton {

    private val clockBackgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, by.bashlikovvv.core.R.color.black)
    }

    private val clockArrowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, by.bashlikovvv.core.R.color.white)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private val viewCenter: PointF
        get() {
            val rect = Rect()
            this.getLocalVisibleRect(rect)

            return PointF(rect.exactCenterX(), rect.exactCenterX())
        }

    init {
        contentDescription = context.getString(R.string.clock)
        elevation = 0f
        stateListAnimator = null
        setPadding(0)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isLaidOut) {
//            canvas.drawCircle(viewCenter.x.toFloat(), viewCenter.y.toFloat(), 5f, clockBackgroundPaint)
            canvas.drawClock()
        }
    }

    private fun Canvas.drawClock() {
        /* background */
        val radius = VIEW_DIAMETER / 2
        var lineRectF = RectF(
            (viewCenter.x - radius) - ITEM_OFFSET,
            viewCenter.y - LINE_HEIGHT,
            (viewCenter.x + radius) + ITEM_OFFSET,
            viewCenter.y + LINE_HEIGHT
        )
        drawRoundRect(lineRectF, ROUND_RADIUS, ROUND_RADIUS, clockBackgroundPaint)
        lineRectF = RectF(
            viewCenter.x - LINE_HEIGHT,
            viewCenter.y - radius - ITEM_OFFSET,
            viewCenter.x + LINE_HEIGHT,
            viewCenter.y + radius + ITEM_OFFSET
        )
        drawRoundRect(lineRectF, ROUND_RADIUS, ROUND_RADIUS, clockBackgroundPaint)
        drawCircle(viewCenter.x, viewCenter.y, VIEW_DIAMETER, clockBackgroundPaint)
        /* Arrows */
        lineRectF = RectF(
            viewCenter.x - ARROW_HEIGHT,
            viewCenter.y - ARROW_HEIGHT,
            viewCenter.x + ARROW_HEIGHT,
            viewCenter.y + ARROW_HEIGHT
        )
        drawRect(lineRectF, clockArrowPaint)
        lineRectF = RectF(
            viewCenter.x,
            viewCenter.y - ARROW_HEIGHT,
            viewCenter.x + SHORT_ARROW_LENGTH,
            viewCenter.y + ARROW_HEIGHT
        )
        drawRoundRect(lineRectF, ROUND_RADIUS, ROUND_RADIUS, clockArrowPaint)
        lineRectF = RectF(
            viewCenter.x - ARROW_HEIGHT,
            viewCenter.y,
            viewCenter.x + ARROW_HEIGHT,
            viewCenter.y - LONG_ARROW_LENGTH
        )
        drawRoundRect(lineRectF, ROUND_RADIUS, ROUND_RADIUS, clockArrowPaint)
    }

    companion object {
        // 100 / 150 = 60 / 90
        const val VIEW_DIAMETER = 90f
        // 150 / 117 = 90 / 70
        const val ITEM_OFFSET = 70f
        const val LINE_HEIGHT = 7f
        const val ROUND_RADIUS = 10f
        const val ARROW_HEIGHT = 5
        const val SHORT_ARROW_LENGTH = 40
        const val LONG_ARROW_LENGTH = 50
    }

}