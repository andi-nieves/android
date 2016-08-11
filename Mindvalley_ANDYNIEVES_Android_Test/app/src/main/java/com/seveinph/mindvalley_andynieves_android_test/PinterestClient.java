package com.seveinph.mindvalley_andynieves_android_test;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;


/**
 * Created by andi on 8/5/2016.
 */

public class PinterestClient {
    ProgressDialog pd;
    RecyclerView xrv;
    public PinterestClient(final Context context, RecyclerView rv){
        Config config = new Config();
        pd = new ProgressDialog(context);
        xrv = rv;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(config.uri,new JsonHttpResponseHandler(){
            @Override
            public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                super.onPreProcessResponse(instance, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                super.onSuccess(statusCode, headers, response);
                List<PinInfo> pilist = new ArrayList<PinInfo>();

                for (int i =0;i<response.length();i++){
                    try {
                        JSONObject  jo          = response.getJSONObject(i);
                        JSONObject  jo_user     = jo.getJSONObject("user");
                        JSONObject  jo_profile_pictures = jo_user.getJSONObject("profile_image");
                        JSONArray   jo_user_current_collection = jo.getJSONArray("current_user_collections");
                        JSONObject  jo_url      = jo.getJSONObject("urls");
                        JSONObject  jo_links    = jo.getJSONObject("links");
                        final PinInfo pi = new PinInfo();
                        Iterator<String> keys = jo.keys();
                        pi.user_id = jo.getString("id");
                        pi.created_at = jo.getString("created_at");
                        pi.user_name = jo_user.getString("name");
                        pi.likes = Integer.parseInt(jo.getString("likes"));
                        pi.user_profile_img_medium = jo_profile_pictures.getString("medium");
                        pi.categories = jo_user_current_collection.length();
                        pi.url = jo_url.getString("regular");
                        pi.download  = jo_links.getString("download");
                        pi.color = jo.getString("color");
                        pilist.add(pi);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                DataAdapter dx = new DataAdapter(context,pilist);
                xrv.setAdapter(dx);

            }
        });
    }

}
