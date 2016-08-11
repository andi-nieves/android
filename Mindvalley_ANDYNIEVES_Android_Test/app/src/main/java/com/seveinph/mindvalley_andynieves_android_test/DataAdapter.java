package com.seveinph.mindvalley_andynieves_android_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

/**
 * Created by andi on 8/5/2016.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.PinViewHolder>{

    private List<PinInfo> pin_list;
    private Context context;
    public DataAdapter(Context context, List<PinInfo> pin_info){
        this.pin_list  = pin_info;
        this.context = context;
    }

    @Override
    public PinViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_view, viewGroup, false);

        return new PinViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PinViewHolder holder, int i) {
        PinInfo info = pin_list.get(i);
        Picasso.with(context)
                .load(info.user_profile_img_medium)
                .placeholder(R.drawable.loading)
                .error(R.drawable.warning)
                .into(holder.img_profile_picture);
        Picasso.with(context)
                .load(info.url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.warning)
                .into(holder.img_collection);
        //holder.img_profile_picture.setImageDrawable();
        //holder.tv_name.setTextColor(Color.parseColor(info.color));
        holder.tv_name.setText(info.user_name);
        holder.rtv.setReferenceTime(convert(info.created_at).getTime());
        holder.tv_likes.setText(String.valueOf(info.likes) + " Liked");
        holder.tv_photo_count.setText(String.valueOf(info.categories) + " collection");
        holder.tv_download.setTag(info.download);
        final ProgressDialog pd = new ProgressDialog(context);

        holder.tv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,v.getTag().toString(),Toast.LENGTH_LONG).show();
                pd.setMessage("Downloading image\nPlease wait...");
                //pd.setCancelable(false);
                pd.show();
                AsyncHttpClient client = new AsyncHttpClient();
                final String SourceFilname = v.getTag().toString();
                String[] fileType = {

                        "image/png",
                        "image/jpeg",
                        "image/gif"
                };
                client.get(v.getTag().toString(), new BinaryHttpResponseHandler(fileType) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                        Toast.makeText(context,"Image successfully downloaded",Toast.LENGTH_LONG).show();
                        String DestinationName = SourceFilname.substring(SourceFilname.lastIndexOf('/')+1, SourceFilname.length());
                        //Saving an image into DCIM Folder
                        DestinationName += ".jpg";
                        File _f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),DestinationName);
                        FileOutputStream output = null;
                        try {
                            output = new FileOutputStream(_f);
                            output.write(binaryData);
                            output.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        //Refreshing MediaScanner, so that our downloaded image can be shown in Gallery
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),DestinationName);
                        Uri contentUri = Uri.fromFile(f);
                        mediaScanIntent.setData(contentUri);
                        context.sendBroadcast(mediaScanIntent);
                        pd.dismiss();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                        Toast.makeText(context,"Download Failed!",Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }



                    @Override
                    public void onProgress(long bytesWritten, long totalSize) {
                        long total = (totalSize / bytesWritten ) ;
                        super.onProgress(bytesWritten, totalSize);

                        Log.d("TOTAL", "bytesWritten:" + String.valueOf(bytesWritten) + " - total" + String.valueOf(totalSize) +" - onProgress: " + String.valueOf(total));
                    }


                });
            }
        });
        //holder.rtv.setReferenceTime(new Date().getTime());
    }

    @Override
    public int getItemCount() {
        return pin_list.size();
    }
    public Date convert(String timestamp){
        String dtStart = timestamp;
        //2016-05-29T15:42:02-04:00
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(dtStart);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }


    public static class PinViewHolder extends RecyclerView.ViewHolder {
        ImageView img_profile_picture;
        TextView tv_name;
        ImageView img_collection;
        TextView tv_likes;
        RelativeTimeTextView rtv;
        //TextView tv_collections;
        TextView tv_photo_count;
        TextView tv_download;

        //TextView tv_user_name;
        public PinViewHolder(View v) {
            super(v);
            img_profile_picture = (ImageView) v.findViewById(R.id.img_profile_picture);
            tv_name             = (TextView) v.findViewById(R.id.tv_user_name);
            img_collection      = (ImageView) v.findViewById(R.id.img_collection);
            tv_likes            = (TextView) v.findViewById(R.id.tv_likes);
            rtv                 = (RelativeTimeTextView) v.findViewById(R.id.timestamp);
            //tv_collections      = (TextView) v.findViewById(R.id.tv_collection);
            tv_photo_count      = (TextView) v.findViewById(R.id.tv_photo_count);
            tv_download         = (TextView) v.findViewById(R.id.tv_download);
            // rtv.setReferenceTime(convert());
        }

    }
}
