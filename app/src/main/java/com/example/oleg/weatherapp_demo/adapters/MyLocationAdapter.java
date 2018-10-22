package com.example.oleg.weatherapp_demo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.oleg.weatherapp_demo.R;
import com.example.oleg.weatherapp_demo.data.entities.MyLocation;
import com.example.oleg.weatherapp_demo.utils.BitmapTransforamationHelper;

import java.util.List;

public class MyLocationAdapter extends RecyclerView.Adapter<MyLocationAdapter.MyLocationViewHolder> {

    private final LayoutInflater mInflater;
    private List<MyLocation> mMyLocations; // Cached copy of MyLocations
    private Context mContext;

    public MyLocationAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public MyLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.weather_app_location_list_item, parent, false);
        return new MyLocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLocationViewHolder holder, int position) {
        if (mMyLocations != null) {
            MyLocation current = mMyLocations.get(position);
            holder.locationName.setText(current.getLocationName());

            // set background image

            // Convert byte array to bitmap
//            int width = mMyLocations.get(position).getmBitmapWidth();
//            int height = mMyLocations.get(position).getmBitmapHeight();
//            byte[]byteArray = mMyLocations.get(position).getImageString();
//
//            Bitmap.Config configBmp = Bitmap.Config.valueOf(null);
//            Bitmap bitmap_tmp = Bitmap.createBitmap(width, height, configBmp);
//            ByteBuffer buffer = ByteBuffer.wrap(byteArray);
//            bitmap_tmp.copyPixelsFromBuffer(buffer);

//            Bitmap bitmap = null;
//            try {
//                byte[] encodeByte = Base64.decode(mMyLocations.get(position).getImageString(), Base64.DEFAULT);
//                bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            } catch (Exception e) {
//                e.getMessage();
//            } finally {
//                if (bitmap != null) {
//                    // Transform original bitmap to resized bitmap
//                    int widthOfLayout = holder.constraintLayout.getWidth();
//                    int heightOfLayout = holder.constraintLayout.getHeight();
//                    Drawable drawable = new BitmapDrawable(mContext.getResources(),
//                            BitmapTransforamationHelper.transformWithSavedProportions(bitmap, widthOfLayout, heightOfLayout));
//                    holder.constraintLayout.setBackground(drawable);
//                    holder.constraintLayout.getBackground().setAlpha(51); // Setting opacity (scale is 0 - 255)
//                }
//            }

        } else {
            throw new IllegalArgumentException("Some error with binding data for navigation recycler view");
        }
    }

    public void setMyLocations(List<MyLocation> list) {
        mMyLocations = list;
        notifyDataSetChanged();
    }

    public List<MyLocation> getAll() {
        if (mMyLocations != null) return mMyLocations;
        else return null;
    }

    @Override
    public int getItemCount() {
        if (mMyLocations != null) return mMyLocations.size();
        return 0;
    }

    public MyLocation getItem(int position) {
        return mMyLocations.get(position);
    }

    class MyLocationViewHolder extends RecyclerView.ViewHolder {

        TextView locationName;
        ImageButton imageButton;
        //ConstraintLayout constraintLayout;

        MyLocationViewHolder(View view) {
            super(view);

            locationName = view.findViewById(R.id.tv_location_item_name);
            imageButton = view.findViewById(R.id.ib_delete_location);
            //constraintLayout = view.findViewById(R.id.cl_activity_main);
        }
    }
}
