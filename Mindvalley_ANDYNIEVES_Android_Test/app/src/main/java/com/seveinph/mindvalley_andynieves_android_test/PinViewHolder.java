package com.seveinph.mindvalley_andynieves_android_test;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by andi on 8/5/2016.
 */
public class PinViewHolder extends RecyclerView.ViewHolder {
    ImageView img_profile_picture;
    TextView tv_name;
    ImageView img_collection;
    TextView tv_title;
    //TextView tv_user_name;
    public PinViewHolder(View v){
        super(v);
        img_profile_picture = (ImageView) v.findViewById(R.id.img_profile_picture);
        tv_name = (TextView) v.findViewById(R.id.tv_user_name);
        img_collection = (ImageView) v.findViewById(R.id.img_collection);
    }
}
