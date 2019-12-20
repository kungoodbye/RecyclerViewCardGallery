package com.view.jameson.library

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * 控制fling速度的RecyclerView
 *
 * Created by jameson on 9/1/16.
 */
class SpeedRecyclerView  @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    override fun fling(velocityX: Int, velocityY: Int): Boolean {

        return super.fling(solveVelocity(velocityX), solveVelocity(velocityY))
    }

    private fun solveVelocity(velocity: Int): Int {
        return if (velocity > 0) {
            velocity.coerceAtMost(FLING_MAX_VELOCITY)
        } else {
            velocity.coerceAtMost(-FLING_MAX_VELOCITY)
        }
    }

    companion object {
        private const val FLING_SCALE_DOWN_FACTOR = 0.5f // 减速因子
        private const val FLING_MAX_VELOCITY = 8000 // 最大顺时滑动速度
    }
}