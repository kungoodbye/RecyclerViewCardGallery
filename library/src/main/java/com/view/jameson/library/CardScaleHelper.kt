package com.view.jameson.library

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

import jameson.io.library.util.LogUtils
import jameson.io.library.util.ScreenUtil


class CardScaleHelper {
    private lateinit var mRecyclerView: RecyclerView
    private var mContext: Context? = null

    private var mScale = 0.7f // 两边视图scale
    private var mPagePadding = 25 // 卡片的padding, 卡片间的距离等于2倍的mPagePadding
    private var mShowLeftCardWidth = 65   // 左边卡片显示大小

    private var mCardWidth: Int = 0 // 卡片宽度
    private var mOnePageWidth: Int = 0 // 滑动一页的距离
    private var mCardGalleryWidth: Int = 0

    /**
     * 移动到默认位置
     *
     * @param currentItemPos
     */
    var currentItemPos: Int = 0
    private var mCurrentItemOffset: Int = 0

    private val mLinearSnapHelper = CardLinearSnapHelper()

    fun attachToRecyclerView(mRecyclerView: RecyclerView) {
        // 开启log会影响滑动体验, 调试时才开启
        LogUtils.mLogEnable = true
        this.mRecyclerView = mRecyclerView
        mContext = mRecyclerView.context
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLinearSnapHelper.mNoNeedToScroll = mCurrentItemOffset == 0 || mCurrentItemOffset == getDestItemOffset(mRecyclerView.adapter!!.itemCount - 1)
                } else {
                    mLinearSnapHelper.mNoNeedToScroll = false
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // dx>0则表示右滑, dx<0表示左滑, dy<0表示上滑, dy>0表示下滑
                if (dx != 0) {//去掉奇怪的内存疯涨问题
                    mCurrentItemOffset += dx
                    computeCurrentItemPos()
//                    LogUtils.v(String.format("dx=%s, dy=%s, mScrolledX=%s", dx, dy, mCurrentItemOffset))
                    onScrolledChangedCallback()
                }
            }
        })

        initWidth()
        //自动居中
        mLinearSnapHelper.attachToRecyclerView(mRecyclerView)
    }

    /**
     * 初始化卡片宽度
     */
    private fun initWidth() {
        mRecyclerView!!.post {
            mCardGalleryWidth = mRecyclerView!!.width
            mCardWidth = mCardGalleryWidth - ScreenUtil.dip2px(mContext!!, (2 * (mPagePadding + mShowLeftCardWidth)).toFloat())
            mOnePageWidth = mCardWidth
            mRecyclerView!!.smoothScrollToPosition(currentItemPos)
            onScrolledChangedCallback()
        }
    }

    private fun getDestItemOffset(destPos: Int): Int {
        return mOnePageWidth * destPos
    }

    /**
     * 计算mCurrentItemOffset
     */
    private fun computeCurrentItemPos() {
        if (mOnePageWidth <= 0) return
        var pageChanged = false
        // 滑动超过一页说明已翻页
        if (Math.abs(mCurrentItemOffset - currentItemPos * mOnePageWidth) >= mOnePageWidth) {
            pageChanged = true
        }
        if (pageChanged) {
            val tempPos = currentItemPos

            currentItemPos = mCurrentItemOffset / mOnePageWidth
//            LogUtils.d(String.format("=======onCurrentItemPos Changed======= tempPos=%s, mCurrentItemPos=%s", tempPos, currentItemPos))
        }
    }

    /**
     * RecyclerView位移事件监听, view大小随位移事件变化
     */
    private fun onScrolledChangedCallback() {
        val offset = mCurrentItemOffset - currentItemPos * mOnePageWidth
        val percent = Math.max(Math.abs(offset) * 1.0 / mOnePageWidth, 0.0001).toFloat()
        //        if (percent < 1.0e-3) {
        //            percent = (float)1.0e-0;
        //            return;
        //        }
//        LogUtils.d(String.format("offset=%s, percent=%s", offset, percent))
        var leftView: View? = null
        val currentView: View?
        var rightView: View? = null

        var left2View: View? = null
        var right2View: View? = null

        if (currentItemPos > 0) {
            leftView = mRecyclerView.layoutManager!!.findViewByPosition(currentItemPos - 1)
            left2View = mRecyclerView.layoutManager!!.findViewByPosition(currentItemPos - 2)

        }
        currentView = mRecyclerView.layoutManager!!.findViewByPosition(currentItemPos)


        if (currentItemPos < mRecyclerView.adapter!!.itemCount - 1) {
            rightView = mRecyclerView.layoutManager!!.findViewByPosition(currentItemPos + 1)
            right2View = mRecyclerView.layoutManager!!.findViewByPosition(currentItemPos + 2)
        }
        val mScaleY = (1 - mScale) * percent + mScale
        if (leftView != null) {
            // y = (1 - mScale)x + mScale
            leftView.scaleY = mScaleY
            if (left2View != null) {
                left2View.scaleY = mScaleY / 2 + mScale - 0.5f
            }
        }
        if (currentView != null) {
            // y = (mScale - 1)x + 1
            currentView.scaleY = (mScale - 1) * percent + 1
        }
        if (rightView != null) {
            // y = (1 - mScale)x + mScale
            rightView.scaleY = mScaleY
            if (right2View != null) {
                right2View.scaleY = mScaleY / 2 + mScale - 0.5f
            }
        }
    }

    fun setScale(scale: Float) {
        mScale = scale
    }

    fun setPagePadding(pagePadding: Int) {
        mPagePadding = pagePadding
    }

    fun setShowLeftCardWidth(showLeftCardWidth: Int) {
        mShowLeftCardWidth = showLeftCardWidth
    }
}
