package com.view.jameson.androidrecyclerviewcard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.view.jameson.library.CardAdapterHelper;

import java.util.ArrayList;
import java.util.List;

import jameson.io.library.util.ToastUtils;

/**
 * Created by jameson on 8/30/16.
 */
class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Integer> mList;


    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    CardAdapter(List<Integer> mList, int mPagePadding, int mShowLeftCardWidth) {
        this.mList = mList;
        mCardAdapterHelper.setPagePadding(mPagePadding);
        mCardAdapterHelper.setShowLeftCardWidth(mShowLeftCardWidth);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        holder.mImageView.setImageResource(mList.get(position));
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(holder.mImageView.getContext(), "" + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
        }

    }

}
