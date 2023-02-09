package ipca.test.musiceditor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class VideoEditorView : View {

    var percentageChanger : PercentageChanger? = null

    val mPaint = Paint()
    val mTotalRect = Rect()
    val mSelectionRect = Rect()

    var mTotalDuration = 130 * 60  // 2 hours and 10 minutes
    var mSelectionStart = 0
    var mSelectionEnd = mTotalDuration

    var currentPosition = 0F
    var currentPositionLeft = 0F
    var currentPositionRight = 0F

    var mIsDraggingStart = false
    var mIsDraggingEnd = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw total rectangle
        mPaint.color = 0xFFFFA500.toInt()
        mTotalRect.set(0, height / 2 - height / 20, width, height / 2 + height / 20)
        canvas.drawRect(mTotalRect, mPaint)

        // Draw selection
        val selectionWidth = (mTotalRect.width() * 0.75f).toInt()
        mSelectionRect.set(currentPositionLeft.toInt(), mTotalRect.top , currentPositionRight.toInt(), mTotalRect.bottom)
        mPaint.color = 0xFF0000FF.toInt()
        canvas.drawRect(mSelectionRect, mPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Check if the touch event is on the left or right side of the rectangle
                if (event.x < mSelectionRect.centerX()) {
                    mIsDraggingStart = true
                    currentPositionLeft = event.x
                } else {
                    mIsDraggingEnd = true
                    currentPositionRight = event.x
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {

                val percentage = getSelectionPercentage()
                percentageChanger?.onSelectionChanged(percentage)

                if (percentage < 0 && mIsDraggingStart) {
                    //make percentage positive
                    percentageChanger?.onSelectionChanged(percentage * -1)
                } else if (percentage < 0 && mIsDraggingEnd) {
                    //make percentage 0
                    percentageChanger?.onSelectionChanged(0f)
                }
                invalidate()

                if (mIsDraggingStart) {
                    currentPositionLeft = event.x
                    if (currentPositionLeft < mTotalRect.left) {
                        currentPositionLeft = mTotalRect.left.toFloat()
                    }
                } else if (mIsDraggingEnd) {
                    currentPositionRight = event.x
                    if (currentPositionRight > mTotalRect.right) {
                        currentPositionRight = mTotalRect.right.toFloat()
                    }
                }


            }
            MotionEvent.ACTION_UP -> {
                mIsDraggingStart = false
                mIsDraggingEnd = false
                return true
            }
        }
        return super.onTouchEvent(event)
    }


    fun getSelectionPercentage(): Float {
        mTotalRect.set(0, height / 2 - height / 20, width, height / 2 + height / 20)

        if (currentPositionLeft > mTotalRect.right) {
            return 0f
        }

        return if (mTotalRect.width() == 0) {
            0f
        } else {
            (mSelectionRect.width().toFloat() / mTotalRect.width().toFloat()) * 100
        }
    }


    fun setSelectionChangedListener(listener: PercentageChanger) {
        percentageChanger = listener
    }

    interface PercentageChanger {
        fun onSelectionChanged(percentage: Float)
    }
}