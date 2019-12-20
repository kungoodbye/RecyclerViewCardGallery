package com.view.jameson.androidrecyclerviewcard

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.view.jameson.androidrecyclerviewcard.util.BlurBitmapUtils
import com.view.jameson.androidrecyclerviewcard.util.ViewSwitchUtils
import com.view.jameson.library.CardScaleHelper

import java.util.ArrayList

class MainActivity : Activity() {

    private var mRecyclerView: RecyclerView? = null
    private var mBlurView: ImageView? = null
    private val mList = ArrayList<Int>()
    private var mCardScaleHelper: CardScaleHelper? = null
    private var mBlurRunnable: Runnable? = null
    private var mLastPos = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        for (i in 0..9) {
            mList.add(R.drawable.a)
            mList.add(R.drawable.a)
            mList.add(R.drawable.a)
        }

        mRecyclerView = findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView!!.layoutManager = linearLayoutManager

        //图片间距(dp)
        val mPagePadding = 25
        //左右侧显示宽度(dp)
        val mShowLeftCardWidth = 65
        //缩放倍率
        val mScale = 0.7f
        mRecyclerView!!.adapter = CardAdapter(mList, mPagePadding, mShowLeftCardWidth)
        mCardScaleHelper = CardScaleHelper()
        mCardScaleHelper!!.setPagePadding(mPagePadding)
        mCardScaleHelper!!.setShowLeftCardWidth(mShowLeftCardWidth)

        mCardScaleHelper!!.setScale(mScale)
        mCardScaleHelper!!.currentItemPos = 5
        mCardScaleHelper!!.attachToRecyclerView(mRecyclerView!!)

        initBlurBackground()
    }

    private fun initBlurBackground() {
        mBlurView = findViewById(R.id.blurView)
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange()
                }
            }
        })

        notifyBackgroundChange()
    }

    private fun notifyBackgroundChange() {
        if (mLastPos == mCardScaleHelper!!.currentItemPos) return
        mLastPos = mCardScaleHelper!!.currentItemPos
        val resId = mList[mCardScaleHelper!!.currentItemPos]
        mBlurView!!.removeCallbacks(mBlurRunnable)
        mBlurRunnable = Runnable {
            val bitmap = BitmapFactory.decodeResource(resources, resId)
            ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView!!, BlurBitmapUtils.getBlurBitmap(mBlurView!!.context, bitmap, 15))
        }
        mBlurView!!.postDelayed(mBlurRunnable, 500)
    }

}
