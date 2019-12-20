package com.view.jameson.androidrecyclerviewcard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import com.view.jameson.library.CardAdapterHelper



/**
 * Created by jameson on 8/30/16.
 */
class CardAdapter(private val mList: List<Int>, mPagePadding: Int, mShowLeftCardWidth: Int) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {


    private val mCardAdapterHelper = CardAdapterHelper()

    init {
        mCardAdapterHelper.setPagePadding(mPagePadding)
        mCardAdapterHelper.setShowLeftCardWidth(mShowLeftCardWidth)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_card_item, parent, false)
        mCardAdapterHelper.onCreateViewHolder(parent, itemView)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, itemCount)
        holder.mImageView.setImageResource(mList[position])
        holder.mImageView.setOnClickListener {
//            ToastUtils.show(holder.mImageView.context, "" + position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImageView: ImageView

        init {
            mImageView = itemView.findViewById(R.id.imageView)
        }

    }

}
