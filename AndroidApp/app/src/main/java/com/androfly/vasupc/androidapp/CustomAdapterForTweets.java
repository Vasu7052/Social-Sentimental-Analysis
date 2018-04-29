package com.androfly.vasupc.androidapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by VasuPC on 25-03-2018.
 */

public class CustomAdapterForTweets extends ArrayAdapter<TweetModel>{

    private ArrayList<TweetModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvHandle;
        TextView tvDate;
        TextView tvTweet;
    }

    public CustomAdapterForTweets(ArrayList<TweetModel> data, Context context) {
        super(context, R.layout.list_item_for_tweets, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TweetModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item_for_tweets, parent, false);
            viewHolder.tvHandle = (TextView) convertView.findViewById(R.id.tv_handle);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tvTweet = (TextView) convertView.findViewById(R.id.tv_tweet);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.tvHandle.setText(dataModel.getHandle());
        viewHolder.tvDate.setText(dataModel.getDate());
        viewHolder.tvTweet.setText(dataModel.getTweet());

        viewHolder.tvHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to visit : " + dataModel.getLink())
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(dataModel.getLink()));
                                mContext.startActivity(intent);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });

        return convertView;
    }
}