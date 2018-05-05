package com.example.oleg.weatherapp_demo;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oleg.weatherapp_demo.data.WeatherContract;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;


    // Item click stuff
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, Arrays.toString(mCursor.getColumnNames()), Toast.LENGTH_SHORT).show();
        }
    };

    public WeatherAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }


    @Override
    public WeatherAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.weather_app_list_item, viewGroup, false);

        // Item click stuff
        view.setOnClickListener(mOnClickListener);

        return new WeatherAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final WeatherAdapterViewHolder weatherAdapterViewHolder, final int position) {
        // Move the cursor to the passed in position, return if moveToPosition returns false
        if(!mCursor.moveToPosition(position))
            return;


        /****************
         * Weather Icon *
         ****************/

        //implement getting icon, a dummy for now
        weatherAdapterViewHolder.weatherIcon.setImageResource(R.drawable.ic_brightness_medium_black_48dp);

        /****************
         * Weather Date *
         ****************/
        long dbDate = mCursor.getLong(mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE));

        Date date = new java.util.Date(dbDate*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+3"));
        String formattedDate = sdf.format(date);

        weatherAdapterViewHolder.weatherDate.setText(formattedDate);

        /***********************
         * Weather Summary *
         ***********************/
        String summary = mCursor.getString(mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SUMMARY_ID));
        weatherAdapterViewHolder.weatherSummary.setText(summary);

        /**************************
         * High (max) temperature *
         **************************/
        double temperatureMax = mCursor.getDouble(mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP));
        weatherAdapterViewHolder.weatherTemperatureHigh.setText(String.valueOf(Math.round(temperatureMax))+ "\u00b0");

        /*************************
         * Low (min) temperature *
         *************************/
        double temperatureMin = mCursor.getDouble(mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP));
        weatherAdapterViewHolder.weatherTemperatureLow.setText(String.valueOf(Math.round(temperatureMin)) + "\u00b0");
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        // Inside, check if the current cursor is not null, and close it if so
        if(mCursor != null) mCursor.close();
        // Update the local mCursor to be equal to  newCursor
        mCursor = newCursor;
        // Check if the newCursor is not null, and call this.notifyDataSetChanged() if so
        if(newCursor != null){
            //Force RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class WeatherAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView weatherIcon;
        TextView weatherDate;
        TextView weatherSummary;
        TextView weatherTemperatureHigh;
        TextView weatherTemperatureLow;

        WeatherAdapterViewHolder(View view) {
            super(view);

            weatherIcon = view.findViewById(R.id.weather_icon);
            weatherDate = view.findViewById(R.id.tv_weather_date);
            weatherSummary = view.findViewById(R.id.tv_weather_summary);
            weatherTemperatureHigh = view.findViewById(R.id.tv_high_temperature);
            weatherTemperatureLow = view.findViewById(R.id.tv_low_temperature);
        }
    }
}