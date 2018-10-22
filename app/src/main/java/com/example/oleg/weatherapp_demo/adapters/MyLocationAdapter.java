package com.example.oleg.weatherapp_demo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.oleg.weatherapp_demo.R;
import com.example.oleg.weatherapp_demo.data.entities.MyLocation;

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

        MyLocationViewHolder(View view) {
            super(view);

            locationName = view.findViewById(R.id.tv_location_item_name);
            imageButton = view.findViewById(R.id.ib_delete_location);
        }
    }
}
